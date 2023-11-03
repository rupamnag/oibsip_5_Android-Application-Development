package com.example.timepulse;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView stopwatchText;
    private Button startResumeButton;
    private Button pauseButton;
    private Button resetButton;

    private boolean isRunning = false;
    private boolean isPaused = false;
    private long startTime = 0L;
    private long pausedTime = 0L;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopwatchText = findViewById(R.id.stopwatchText);
        startResumeButton = findViewById(R.id.startResumeButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        startResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    startTimer();
                } else if (!isPaused) {
                    pauseTimer();
                } else {
                    resumeTimer();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning && !isPaused) {
                    pauseTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds;
            if (isPaused) {
                timeInMilliseconds = pausedTime - startTime;
            } else {
                timeInMilliseconds = System.currentTimeMillis() - startTime;
            }

            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds %= 60;
            int milliseconds = (int) (timeInMilliseconds % 1000);
            stopwatchText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 0);
        }
    };

    private void startTimer() {
        startResumeButton.setText("Pause");
        pauseButton.setEnabled(true);
        isRunning = true;
        isPaused = false;
        startTime = System.currentTimeMillis();
        handler.post(updateTimerThread);
    }

    private void pauseTimer() {
        startResumeButton.setText("Resume");
        isPaused = true;
        pausedTime = System.currentTimeMillis();
        handler.removeCallbacks(updateTimerThread);
    }

    private void resumeTimer() {
        startResumeButton.setText("Pause");
        isPaused = false;
        long timeSpentPaused = System.currentTimeMillis() - pausedTime;
        startTime += timeSpentPaused;
        handler.post(updateTimerThread);
    }

    private void resetTimer() {
        startResumeButton.setText("Start");
        pauseButton.setEnabled(false);
        isRunning = false;
        isPaused = false;
        handler.removeCallbacks(updateTimerThread);
        stopwatchText.setText("00:00:00.000");
        startTime = 0L;
        pausedTime = 0L;
    }
}
