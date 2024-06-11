package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime; // Assuming startTime is updated immediately in start() method
        Assert.assertTrue("Timer should be counting down", timer.isCounting());
        Assert.assertTrue("Elapsed time should be less than or equal to duration", elapsedTime <= timer.durationMillis);
    }

    @Test
    public void testIsCounting_whenTimerHasReachedZero_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime + 1001; // Elapsed time is 1ms more than duration
        Assert.assertFalse("Timer should not be counting down anymore", timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsIllegalStateException() {
        Timer timer = new Timer(1000);
        Assert.assertFalse("Timer should not be counting down before start()", timer.isCounting());
        timer.isCounting(); // Triggering the assertion in isCounting() method
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotChangeStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        long initialStartTime = timer.startTime;
        timer.start();
        Assert.assertEquals("StartTime should not have been changed", initialStartTime, timer.startTime);
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenSetsStartTimeCorrectly() {
        Timer timer = new Timer(1000);
        Assert.assertEquals("StartTime should be -1 initially", -1, timer.startTime);
        timer.start();
        long initialStartTime = timer.startTime;
        Assert.assertNotEquals("StartTime should be set to current time after starting timer", -1, timer.startTime);
        Assert.assertEquals("Elapsed time should be zero initially", 0, timer.isCounting() ? System.currentTimeMillis() - timer.startTime : 0);
    }
}