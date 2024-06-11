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
    public void testIsCountingWithDurationZero() {
        Timer timer = new Timer(0);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWithDurationNegative() {
        Timer timer = new Timer(-1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingBeforeStart() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

}