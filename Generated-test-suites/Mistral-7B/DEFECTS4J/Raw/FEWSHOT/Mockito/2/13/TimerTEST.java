package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerStartedLessThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test
    public void testIsCounting_timerStartedMoreThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime;
        timer.start(); // This should not change the state of the timer
        assertTrue(timer.isCounting());
        assertFalse(elapsedTime <= timer.durationMillis);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerNotInitialized() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.startTime = -1; // Reset start time to uninitialized state
        timer.isCounting();
    }
}