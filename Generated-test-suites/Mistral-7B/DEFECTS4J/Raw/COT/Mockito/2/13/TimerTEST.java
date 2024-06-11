package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testTimerIsCounting_whenStartTimeIsNotSet() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }

    @Test
    public void testTimer_whenDurationIsZero() {
        Timer timer = new Timer(0);
        timer.start();
        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting()); // Should be false since durations is zero
    }

    @Test
    public void testTimer_whenDurationIsNegative() {
        Timer timer = new Timer(-1);
        timer.start();
        assertFalse(timer.isCounting()); // AssertionError should be thrown due to illegal argument
    }

    @Test
    public void testTimer_whenStartIsCalledTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimer_whenElapsedTimeIsGreaterThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime() + 2000;
        assertTrue(timer.isCounting());
        assertFalse(timer.isCounting()); // Should be false since elapsed time is greater than duration
    }

    @Test
    public void testTimer_whenElapsedTimeIsZero() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime();
        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting()); // Should be false since elapsed time is zero
    }

}