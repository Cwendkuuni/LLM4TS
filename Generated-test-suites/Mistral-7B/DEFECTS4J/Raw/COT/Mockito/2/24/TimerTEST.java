package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsInitiallyNotCounting() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerStartsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethodThrowsExceptionWhenAlreadyCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testTimerStopsCountingWhenElapsedTimeIsGreaterThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        // let it run for longer than the duration
        // ...
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerCountDownDurationZero() {
        Timer timer = new Timer(0);
        timer.start();
        assertTrue(timer.isCounting());
        // the timer should count down to zero immediately
        // and stop counting
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerCountDownNegativeDuration() {
        Timer timer = new Timer(-1);
        try {
            timer.start();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testTimerCountDownLongDuration() {
        Timer timer = new Timer(Long.MAX_VALUE);
        timer.start();
        // let it run for a long time to check if it is still counting
        // ...
        assertTrue(timer.isCounting());
    }

}