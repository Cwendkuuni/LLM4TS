package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(5000);
    }

    @Test
    public void testIsCounting_TrueWhenTimerIsStillCounting() {
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_FalseWhenTimerHasNotStarted() {
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_FalseWhenTimerHasElapsed() throws InterruptedException {
        timer.start();
        Thread.sleep(6000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_StartsTheTimer() {
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

}