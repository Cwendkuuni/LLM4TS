package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - (timer.startTime + 500); // give some time for the timer to start
        assertTrue(timer.isCounting());
        assertFalse(timer.isCounting()); // check that timer has stopped after the duration has elapsed
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenThrowsAssertionError() {
        Timer timer = null;
        timer.isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenStartsTheTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_whenTimerIsAlreadyRunning_thenThrowsIllegalStateException() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testStart_whenTimerIsStartedAndThenReset_thenResetsTheTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start(); // this should throw IllegalStateException
        timer.start(); // reset the timer
        assertTrue(timer.isCounting());
    }
}