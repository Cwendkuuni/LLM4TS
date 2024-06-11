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
        // since duration is 0, it should finish counting immediately
        assertTrue(timer.isCounting()); // this should pass since it's checking the state immediately after start()
        assertFalse(timer.isCounting()); // this should fail since the timer should have finished counting already
    }

    @Test
    public void testTimer_whenDurationIsNegative() {
        Timer timer = new Timer(-1000);
        timer.start();
        assertFalse(timer.isCounting()); // since the duration is negative, it should never be counting
    }

}