package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();

        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_NotStarted() {
        Timer timer = new Timer(1000);

        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_Expired() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(2000);

        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();

        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_Twice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();

        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_Stop_Start() throws InterruptedException {
        Timer timer = new Timer(1000);

        timer.start();
        Thread.sleep(500);
        timer.start();

        Assert.assertTrue(timer.isCounting());
    }

}