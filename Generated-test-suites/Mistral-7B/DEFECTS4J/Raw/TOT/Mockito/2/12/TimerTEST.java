package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_TimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();

        // Typical use case: Timer is counting down and has not expired yet
        Assert.assertTrue(timer.isCounting());
        Assert.assertTrue(System.currentTimeMillis() - currentTime < 1000);
    }

    @Test
    public void testIsCounting_TimerHasExpired() {
        Timer timer = new Timer(1000);
        timer.start();

        // Edge case: Timer has expired, but the system clock may be slightly faster or slower
        long startTime = System.currentTimeMillis();
        timer.isCounting(); // This call may change the startTime
        long elapsedTime = System.currentTimeMillis() - startTime;
        long expectedDuration = 1000;

        // Check if the timer has expired within a reasonable tolerance
        Assert.assertFalse(timer.isCounting());
        Assert.assertTrue(elapsedTime >= expectedDuration - 10);
        Assert.assertTrue(elapsedTime <= expectedDuration + 10);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_TimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);

        // Error scenario: Timer has not been started, should throw an AssertionError
        timer.isCounting();
    }

    @Test
    public void testStart_TimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();

        // Error scenario: Timer should not allow starting if it's already running
        timer.start();
    }
}