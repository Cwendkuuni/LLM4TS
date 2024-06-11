package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        long elapsedTime = currentTime - (timer.startTime + 500); // let the timer run for some time
        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() - timer.startTime <= timer.durationMillis);
        assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenThrowsAssertionError() {
        Timer timer = null;
        timer.isCounting();
    }

    @Test
    public void testIsCounting_whenTimerIsExpired_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        timer.durationMillis = elapsedTime - 1; // make the timer expired
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenStartsIt() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        assertTrue(timer.startTime != -1);
        assertTrue(System.currentTimeMillis() - timer.startTime <= timer.durationMillis);
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_whenTimerIsAlreadyRunning_thenThrowsIllegalStateException() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }
}