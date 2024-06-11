package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testTimerIsCounting_whenStartTimeIsNegative() {
        Timer timer = new Timer(1000);
        timer.startTime = -1;
        timer.isCounting();
    }

    @Test
    public void testTimerStart_whenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        timer.start();
    }

    @Test
    public void testTimerStart_whenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerStart_whenTimerDurationIsNegative() {
        Timer timer = new Timer(-1000);
        timer.start();
    }

    @Test
    public void testTimerElapsedTime_whenElapsedTimeIsLessThanDuration() {
        Timer timer = new Timer(2000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        Assert.assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test
    public void testTimerElapsedTime_whenElapsedTimeIsGreaterThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long waitTime = timer.durationMillis + 100;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        Assert.assertTrue(elapsedTime >= timer.durationMillis);
    }

    @Test(expected = IllegalThreadStateException.class)
    public void testTimerElapsedTime_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        timer.isCounting();
    }

}