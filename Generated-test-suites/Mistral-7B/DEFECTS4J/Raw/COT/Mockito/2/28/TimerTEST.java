package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstructionWithValidDuration() {
        Timer timer = new Timer(1000);
        // Basic test for constructor
    }

    @Test
    public void testIsCountingWhenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        // Basic test for isCounting() when timer is not running
    }

    @Test
    public void testIsCountingWhenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
        // Basic test for isCounting() when timer is running

        long currentTime = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(timer.isCounting());
        // Basic test for isCounting() when timer is still running after some time
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructionWithNegativeDuration() {
        Timer timer = new Timer(-1000);
        // Test exception handling for constructor with negative duration
    }

    @Test
    public void testIsCountingWithZeroDuration() {
        Timer timer = new Timer(0);
        assertFalse(timer.isCounting());
        // Test for isCounting() with zero duration
    }

    @Test
    public void testStartMethodWhenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        // Test for starting a timer that is already running
    }

    @Test
    public void testIsCountingAfterStartAndEnd() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        timer.stop();
        assertFalse(timer.isCounting());
        // Test for isCounting() after start, elapse time, and stop
    }

}