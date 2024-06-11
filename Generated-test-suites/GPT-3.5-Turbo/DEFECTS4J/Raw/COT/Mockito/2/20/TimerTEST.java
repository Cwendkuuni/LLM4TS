package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test case 1: Timer is still counting
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());

        // Test case 2: Timer has finished counting
        Timer timer2 = new Timer(0);
        timer2.start();
        Assert.assertFalse(timer2.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertNotEquals(timer.startTime, -1);
    }

}