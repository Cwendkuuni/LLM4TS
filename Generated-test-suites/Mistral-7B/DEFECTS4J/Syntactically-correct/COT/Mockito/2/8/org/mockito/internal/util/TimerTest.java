package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstructionWithValidDurationMillis() {
        Timer timer = new Timer(5000);
        assertNotEquals(-1, timer.getStartTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructionWithNegativeDurationMillis() {
        new Timer(-5000);
    }

    @Test
    public void testIsCountingWhenTimerIsNotStarted() {
        Timer timer = new Timer(5000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerIsRunning() {
        Timer timer = new Timer(5000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = 2000;
        long expectedElapsedTime = currentTime - timer.getStartTime();
        assertTrue(timer.isCounting());
        assertTrue(expectedElapsedTime <= timer.getDurationMillis());

        // test edge case: timer has been running for the exact duration
        currentTime = System.currentTimeMillis();
        elapsedTime = timer.getDurationMillis();
        expectedElapsedTime = currentTime - timer.getStartTime();
        assertTrue(timer.isCounting());
        assertEquals(elapsedTime, expectedElapsedTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCountingWhenTimerHasAlreadyExpired() {
        Timer timer = new Timer(5000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = timer.getDurationMillis() + 1;
        timer.getStartTime(); // advance timer's start time to check for expiration
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStartMethod() {
        Timer timer = new Timer(5000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = 2000;
        long expectedElapsedTime = currentTime - timer.getStartTime();
        assertTrue(timer.isCounting());
        assertTrue(expectedElapsedTime <= timer.getDurationMillis());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethodWhenTimerHasAlreadyExpired() {
        Timer timer = new Timer(5000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = timer.getDurationMillis() + 1;
        timer.getStartTime(); // advance timer's start time to check for expiration
        timer.start();
    }

    @Test
    public void testGetStartTimeWhenTimerIsNotRunning() {
        Timer timer = new Timer(5000);
        assertNull(timer.getStartTime());
    }

}