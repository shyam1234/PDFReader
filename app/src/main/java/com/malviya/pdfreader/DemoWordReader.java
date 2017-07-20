package com.malviya.pdfreader;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by 23508 on 7/20/2017.
 */

public class DemoWordReader extends AppCompatActivity {

    private static final int DURATION = 1000; //in millisec
    private static final int NOOFWORDS = 20;
    private String mPdfText;
    private StringTokenizer mStringToken;
    private TextView mTextViewHolder;
    private Handler mHandler;
    private Runnable mRunnable;
    private String mTempString;
    private int page = 1; //start pdf reading from page number
    private PdfReader mReader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demowordreader);
        init();
        initView();
    }


    private void init() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                readWordFromTokenAndDisplay();
            }
        };
    }


    private void initView() {
        mTextViewHolder = (TextView) findViewById(R.id.textview_holder);
        readTextFromPDF();
        showWord();
    }


    private void readTextFromPDF() {
        try {
            mReader = new PdfReader(getAssets().open("test.pdf"));
            //int n = mReader.getNumberOfPages();
            incrementPdfPage(page);

        } catch (Exception e) {
            Log.e("TEST", e.getMessage());
        }
    }


    private void showWord() {
        mHandler.postDelayed(mRunnable, DURATION);
    }


    private void readWordFromTokenAndDisplay() {
        mTempString = "";
        for (int i = 0; i < NOOFWORDS; i++) {
            if (mStringToken.hasMoreTokens()) {
                mTempString = mTempString + " " + mStringToken.nextToken();
            } else {
                incrementPdfPage(++page);
                break;
            }
            Log.d("TEST", mTextViewHolder.getText().toString());
        }
        mTextViewHolder.setText(mTempString);
        mHandler.postDelayed(mRunnable, DURATION);
    }

    private void incrementPdfPage(int page) {
        try {
            if (page <= mReader.getNumberOfPages()) {
                mPdfText = PdfTextExtractor.getTextFromPage(mReader, page); //Extracting the content from a particular page.
                mStringToken = new StringTokenizer(mPdfText);
                Log.d("TEST", "mStringToken " + mStringToken.countTokens());
                Toast.makeText(this, "Reading page : " + page, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "All pages of PDF are completed", Toast.LENGTH_SHORT).show();
                mHandler.removeCallbacks(mRunnable);
                mRunnable = null;
            }
        } catch (IOException e) {
            Log.e("TEST", "incrementPdfPage: " + e.getMessage());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReader.close();
    }
}
