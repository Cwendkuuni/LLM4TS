package org.junit.test;

import org.junit.Assert;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class TimerTest {

    @ org.junit.Before
    public void setUp() {
        // Initialize tests with a duration of 500 milliseconds
        timer = new Timer(500);
    }

    private Timer timer;

    @org.junit.Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @org.junit.Test
    public void testIsCounting_whenTimerHasFinished_thenReturnsFalse() {
        timer.start();
        try {
            TimeUnit.MILLISECONDS.sleep(1001);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsAssertionError() {
        Assert.assertFalse(timer.isCounting());
    }

    @org.junit.Test
    public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
        Assert.assertFalse(timer.isCounting());
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @org.junit.Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotAffectTimer() {
        timer.start();
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }
}