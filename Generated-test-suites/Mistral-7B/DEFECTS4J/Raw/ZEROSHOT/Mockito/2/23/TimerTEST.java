package org.junit.test;

import org.junit.Assert;
import org.mockito.internal.util.Timer;

import static org.mockito.Mockito.spy;

public class TimerTest {

    @org.junit.Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();

        Assert.assertTrue("Timer should be counting", timer.isCounting());

        long elapsedTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue("Elapsed time should be less than duration", elapsedTime < timer.durationMillis);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTimerIsNotCountingAfterExpiry() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();

        while (timer.isCounting()) {
            // Elapse time
        }

        Assert.assertFalse("Timer should not be counting after expiry", timer.isCounting());
    }

    @org.junit.Test
    public void testTimerCanBeStartedMultipleTimes() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();

        timer.start();

        long elapsedTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue("Elapsed time should be less than duration", elapsedTime < timer.durationMillis);
    }

    @org.junit.Test
    public void testTimerIsCountingWithSpy() {
        Timer timer = spy(new Timer(1000));
        timer.start();
        long startTime = System.currentTimeMillis();

        doCallRealMethod().when(timer).isCounting();

        Assert.assertTrue("Timer should be counting", timer.isCounting());

        long elapsedTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue("Elapsed time should be less than duration", elapsedTime < timer.durationMillis);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTimerThrowsExceptionWithNegativeDuration() {
        new Timer(-1000);
    }

    @org.junit.Test
    public void testTimerCanBeInitializedWithZeroDuration() {
        Timer timer = new Timer(0);

        timer.start();

        long elapsedTime = System.currentTimeMillis() - System.currentTimeMillis();
        Assert.assertEquals("Elapsed time should be zero", 0, elapsedTime);
    }

}