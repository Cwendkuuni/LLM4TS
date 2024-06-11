package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingAfterExpiration() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.wait(1100); // wait for 1.1 seconds to make the timer expired
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testAssertionErrorWhenTimerIsNotStarted() {
        Timer timer = new Timer(1000);
        timer.isCounting(); // should throw AssertionError since start() was not called
    }

    @Test
    public void testTimerCanCountDownWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        assertTrue(timer.isCounting());
        timer.wait(1); // make sure the timer has expired
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNegativeDuration() {
        Timer timer = new Timer(-1);
    }

    @Test
    public void testTimerCanCountDownWithLongDuration() {
        long duration = 10 * 60 * 1000; // 10 minutes
        Timer timer = new Timer(duration);
        timer.start();
        timer.wait(duration + 1000); // wait for 10 minutes plus some extra time to make sure it's expired
        assertFalse(timer.isCounting());
    }

}