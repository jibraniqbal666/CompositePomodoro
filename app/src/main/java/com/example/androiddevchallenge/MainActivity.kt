/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

var totalSeconds = 1500

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        PomodoroScreen()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

@Composable
fun PomodoroScreen() {
    var secondsLeft by remember { mutableStateOf(totalSeconds) }
    var isTimerRunning by remember { mutableStateOf(false) }
    val timer = object : CountDownTimer(totalSeconds * 1000L, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            secondsLeft -= 1
            if (secondsLeft <= 0) {
                secondsLeft = totalSeconds
                isTimerRunning = false
                this.cancel()
            }
        }

        override fun onFinish() {
            isTimerRunning = false
        }
    }
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            Text("Pomodoro", color = primaryColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(76.dp))
            PomodoroTimer(secondsLeft)
            Spacer(modifier = Modifier.height(56.dp))
            PlayPauseButton(isTimerRunning) {
                isTimerRunning = !isTimerRunning
                if (isTimerRunning) timer.start() else timer.cancel()
            }
        }
    }
}

@Composable
fun PlayPauseButton(isPlaying: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(23.dp),
        modifier = Modifier
            .size(46.dp)
            .clickable(onClick = { onClick() }),
        backgroundColor = secondaryColor
    ) {
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = if (isPlaying) Icons.Outlined.PauseCircleOutline else Icons.Outlined.PlayCircleOutline,
            tint = primaryColor,
            contentDescription = null
        )
    }
}

@Composable
fun PomodoroTimer(secondsLeft: Int) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ProgressDial(secondsLeft * 100 / totalSeconds)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${secondsLeft / 60}",
                color = primaryTextColor,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "minutes",
                color = secondaryTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProgressDial(progress: Int) {
    println("progress: $progress")
    val animatedProgress = animateFloatAsState(progress * 3.6f).value
    Box(contentAlignment = Alignment.Center) {
        Donut(color = secondaryColor, sweepAngle = 360f)
        Donut(color = primaryColor, sweepAngle = 0f - animatedProgress)
    }
}

@Composable
fun Donut(color: Color, sweepAngle: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .then(Modifier.aspectRatio(1f)),
        onDraw = {
            drawArc(
                color,
                startAngle = 270f,
                sweepAngle = sweepAngle,
                false,
                style = Stroke(this.size.width / 16, cap = StrokeCap.Round)
            )
        }
    )
}

val primaryColor = Color(248, 89, 89)
val secondaryColor = Color(255, 228, 228)
val primaryTextColor = Color.Black
val secondaryTextColor = Color(134, 149, 169)
