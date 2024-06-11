package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_WhenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue("Timer should be counting", timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerIsCounting_WhenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        Assert.assertFalse("Timer should not be counting before start", timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_WhenTimerHasElapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.waitForElapsed(1001); // Wait for 1ms more than the duration
        Assert.assertFalse("Timer should not be counting after elapsed", timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructor_WhenDurationIsNegative() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testTimerStart_WhenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue("Timer should be counting after start", timer.isCounting());
    }

    @Test
    public void testTimerStart_WhenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        Assert.assertTrue("Timer should still be counting after repeated start", timer.isCounting());
    }

    @Test
    public void testTimerWaitForElapsed_WhenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.waitForElapsed(500);
        Assert.assertTrue("Timer should still be counting after partial wait", timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerWaitForElapsed_WhenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        timer.waitForElapsed(1000);
    }
}