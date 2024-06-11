package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testNewTimerWithZeroDuration() {
        Timer timer = new Timer(0);
        assertTrue(timer.isCounting());
    }

    @Test
    public void testNewTimerWithPositiveDuration() {
        Timer timer = new Timer(1000);
        assertTrue(timer.isCounting());
    }

    @Test
    public void testNewTimerWithNegativeDuration() {
        Timer timer = new Timer(-1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWithExpiredTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start(); // Start twice to exceed the duration
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWithRunningTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStartedTimer() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting()); // Should not throw an assertion error
        timer.assertStarted(); // Throws an assertion error
    }

    @Test
    public void testStartWithoutRunningTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStartWithRunningTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start(); // Should not start a second time
        assertTrue(timer.isCounting());
    }

}