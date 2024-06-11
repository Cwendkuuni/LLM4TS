package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCountingWithinDuration() {
        Timer timer = new Timer(1000); // Duration of 1 second
        timer.start();

        // Assert that the timer is still counting down (within the duration) during the first second
        Assert.assertTrue(timer.isCounting());
        // Allow some slack as System.currentTimeMillis() is not 100% accurate
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime();
        Assert.assertTrue(elapsedTime <= timer.getDurationMillis());
    }

    @Test
    public void testIsCountingBeyondDuration() {
        Timer timer = new Timer(1000); // Duration of 1 second
        timer.start();

        // Allow the timer to count down for longer than the duration before checking
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assert that the timer is no longer counting down after the duration has passed
        Assert.assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCountingBeforeStarting() {
        Timer timer = new Timer(1000);

        // Assert that the timer is not counting down before it has been started
        Assert.assertFalse(timer.isCounting());
        timer.start();
        timer.isCounting(); // This call should start the countdown and then pass the test
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration() {
        Timer timer = new Timer(-1000);

        // Invalid constructor call
        try {
            timer.start();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("durationMillis should be greater than zero", e.getMessage());
            throw e;
        }
    }
}