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
    public void testTimerIsCounting_whenTimerHasElapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1500); // Let the timer elapse by 500ms more than its duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting()); // Should throw an AssertionError as the precondition is not met
    }

    @Test
    public void testTimerStart_whenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
        timer.start(); // Start should not throw an exception if the timer is already running
    }

}