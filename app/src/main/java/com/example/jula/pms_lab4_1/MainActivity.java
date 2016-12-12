package com.example.jula.pms_lab4_1;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final int BOUND = 100;

    private static final int UNKNOWN = -1;
    private static final int RESET = -2;
    private static final int ACCEPT = -3;

    private TextView numberTextView;
    private Button enterNumber;
    private int secretNumber;
    private RelativeLayout relativeLayout;
    private GestureLibrary gLib;
    private GestureOverlayView gestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.numberTextView = (TextView) findViewById(R.id.enterNumberTextView);
        this.enterNumber = (Button) findViewById(R.id.enterNumberBtn);
        this.relativeLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        guessNumber();

        //Загрузка жестов (gestures) из res/raw/gestures
        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            //Если жесты не загружены, то выход из приложения
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
        gestures.addOnGesturePerformedListener(this);
    }

    public void onClickBtn(View v) {
        acceptBtn();
    }

    private void acceptBtn() {
        String text = numberTextView.getText().toString();
        if (text.isEmpty()) {
            showToast(R.string.empty_string);
            return;
        }
        int expected = Integer.parseInt(text);
        boolean isWon = false;
        if (expected > BOUND || expected < 0) {
            showToast(getResources().getText(R.string.bound_fmt).toString() + " " + BOUND);
        } else if (secretNumber < expected) {
            showToast(R.string.less);
        } else if (secretNumber > expected) {
            showToast(R.string.more);
        } else {
            isWon = true;
            showToast(getResources().getText(R.string.congrats) + " " + secretNumber);
        }

        if (!isWon) {
            numberTextView.setText("");
        }
    }



    public void onClickReset(View v) {
        resetBtn();
    }

    private void resetBtn() {
        guessNumber();
        numberTextView.setText("");
        showToast(R.string.reset_number_toast);
    }


    private void showToast(final String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showToast(int id) {
        showToast(getString(id));
    }

    private void guessNumber() {
        Random rnd = new Random();
        secretNumber = rnd.nextInt(BOUND + 1);
    }



    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        List<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {

            Prediction prediction = predictions.get(0);
            System.out.println(predictions.size() + " " + prediction.score + " " + prediction.name);
            if (prediction.score > 1.0) {
                int res = getNumber(prediction.name);
                if (res >= 0 && res <= 9) {
                    numberTextView.append(String.valueOf(res));
                } else if (res == ACCEPT) {
                    acceptBtn();
                } else if (res == RESET) {
                    resetBtn();
                } else {
                    showToast(R.string.unknown);
                }
            } else {
                showToast(R.string.unknown);
            }
        }
    }

    public int getNumber(String text) {
        int res;
        switch (text) {
            case "zero": {
                res = 0;
                break;
            }
            case "one": {
                res = 1;
                break;
            }
            case "two": {
                res = 2;
                break;
            }
            case "three": {
                res = 3;
                break;
            }
            case "four": {
                res = 4;
                break;
            }
            case "five": {
                res = 5;
                break;
            }
            case "six": {
                res = 6;
                break;
            }
            case "seven": {
                res = 7;
                break;
            }
            case "eight": {
                res = 8;
                break;
            }
            case "nine": {
                res = 9;
                break;
            }
            case "reset": {
                res = RESET;
                break;
            }
            case "accept": {
                res = ACCEPT;
                break;
            }
            default: {
                res = UNKNOWN;
            }
        }
        return res;
    }

}