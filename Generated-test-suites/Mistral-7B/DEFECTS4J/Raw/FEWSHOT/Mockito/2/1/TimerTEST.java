package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {
    @Test
    public void testTimerIsCountingWhenStartedAndDurationHasNotExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingWhenStartedAndDurationHasExpired() {
        Timer timer = new Timer(100);
        timer.start();
        Thread.sleep(110); // Sleep for 110ms to let the timer expire
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testAssertionErrorThrownWhenTimerIsNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting()); // Should throw an AssertionError as start() was not called
    }

    @Test
    public void testTimerCanBeStartedMultipleTimes() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingWhenNullDurationGiven() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingWhenNegativeDurationGiven() {
        Timer timer = new Timer(-1);
        timer.start();
        assertFalse(timer.isCounting());
    }
}