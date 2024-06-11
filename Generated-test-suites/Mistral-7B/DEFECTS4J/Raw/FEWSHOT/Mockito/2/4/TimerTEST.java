package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerStartedAndCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - (timer.getStartTime() + 500); // start counting 500ms after creation

        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testIsCounting_timerStartedButExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - (timer.getStartTime() + 1500); // let it run for 1500ms

        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerNotInitialized() {
        Timer timer = new Timer(1000);
        timer = null; // simulate null pointer exception
        timer.isCounting();
    }

    @Test
    public void testStart_startTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = timer.getStartTime();
        long currentTime = System.currentTimeMillis();

        assertTrue(timer.isCounting());
        assertTrue(startTime >= 0);
        assertTrue(currentTime > startTime);
    }

    @Test
    public void testStart_startTimerTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();

        assertTrue(timer.isCounting());
        long startTime = timer.getStartTime();
        long currentTime = System.currentTimeMillis();

        assertTrue(startTime >= 0);
        assertTrue(currentTime > startTime);
    }

    // Add more test cases as needed, for example, testing edge cases and negative durationMillis.

    private long getStartTime(Timer timer) {
        return timer.startTime;
    }

    // Add any helper methods as needed.
}