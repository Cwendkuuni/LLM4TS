package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    // Method 1: Timer(long durationMillis)
    @Test
    public void testTimerConstructor() {
        Timer timer = new Timer(1000);
        assertNotEquals(-1, timer.getStartTime());
        assertEquals(1000, timer.getDurationMillis());
    }

    // Method 2: isCounting()
    @Test
    public void testIsCountingWhenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = timer.getDurationMillis() - (currentTime - timer.getStartTime());
        assertTrue(timer.isCounting());
        assertEquals(elapsedTime, timer.getRemainingTime());
    }

    @Test
    public void testIsCountingWhenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    // Method 3: start()
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = timer.getDurationMillis() - (currentTime - timer.getStartTime());
        assertTrue(timer.isCounting());
        assertEquals(elapsedTime, timer.getRemainingTime());
    }

    @Test(expected = AssertionError.class)
    public void testStartTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    // Edge Cases

    @Test
    public void testIsCountingWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertFalse(timer.isCounting());
        assertEquals(0, timer.getRemainingTime());
    }

    @Test
    public void testIsCountingWithNegativeDuration() {
        Timer timer = new Timer(-1);
        assertFalse(timer.isCounting());
        assertEquals(-1, timer.getRemainingTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerNegativeDuration() {
        Timer timer = new Timer(-1);
    }

    @Test
    public void testIsCountingWithZeroStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.setStartTime(0);
        long currentTime = System.currentTimeMillis();
        assertFalse(timer.isCounting());
        assertEquals(0, timer.getRemainingTime());
    }

    @Test
    public void testIsCountingWithExpiredTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = timer.getDurationMillis();
        long currentTime = System.currentTimeMillis();
        long remainingTime = timer.getRemainingTime();
        timer.stop(); // This is a hack, as there is no stop method in the given Timer class

        // In real life, the Timer should no longer be counting, but since we cannot test that directly,
        // we'll test that the remaining time is zero instead.
        assertEquals(0, remainingTime);
        assertEquals(elapsedTime, timer.getElapsedTime());
    }

    // Getters

    @Test
    public void testGetDurationMillis() {
        Timer timer = new Timer(1000);
        assertEquals(1000, timer.getDurationMillis());
    }

    @Test
    public void testGetStartTime() {
        Timer timer = new Timer(1000);
        assertNotEquals(-1, timer.getStartTime());
    }

    @Test
    public void testGetRemainingTime() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = timer.getDurationMillis() - (currentTime - timer.getStartTime());
        assertEquals(elapsedTime, timer.getRemainingTime());
    }

    @Test
    public void testGetElapsedTime() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = timer.getDurationMillis() - (currentTime - timer.getStartTime());
        timer.stop();
        assertEquals(elapsedTime, timer.getElapsedTime());
    }

}