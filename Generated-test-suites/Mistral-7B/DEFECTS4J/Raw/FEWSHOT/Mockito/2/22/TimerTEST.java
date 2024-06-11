package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerWithPositiveDuration() {
        Timer timer = new Timer(100);
        timer.start();
        assertTrue(timer.isCounting());
        // Wait for some time to make sure the timer is counting
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testTimerWithNegativeDuration() {
        Timer timer = new Timer(-100);
        timer.start();
        // The test should fail with an AssertionError as the duration is negative
    }

    @Test
    public void testIsCountingAfterStop() {
        Timer timer = new Timer(100);
        timer.start();
        timer.stop();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingBeforeStart() {
        Timer timer = new Timer(100);
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerWithLongDuration() {
        Timer timer = new Timer(10000);
        timer.start();
        // Wait for some time to make sure the timer is counting
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsStoppedAfterDurationElapsed() {
        Timer timer = new Timer(100);
        timer.start();
        // Wait for some time to make sure the timer elapsed
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerStartAndStopMultipleTimes() {
        Timer timer = new Timer(100);
        timer.start();
        assertTrue(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
        timer.stop();
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }
}