package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000L);
        timer.start();

        // Test when timer is still counting
        boolean isCounting = timer.isCounting();
        Assert.assertTrue(isCounting);

        // Test when timer has finished counting
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isCounting = timer.isCounting();
        Assert.assertFalse(isCounting);
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(10000L);

        // Test if start initializes startTime
        timer.start();
        long startTime = timer.getStartTime();
        Assert.assertNotEquals(-1, startTime);

        // Test if start updates startTime
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.start();
        long newStartTime = timer.getStartTime();
        Assert.assertNotEquals(startTime, newStartTime);
    }
}