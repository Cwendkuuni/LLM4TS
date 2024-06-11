package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_TimerIsRunning() {
        // Typical use case: Timer is running within its duration
        Timer timer = new Timer(1000);
        timer.start();
        long timeElapsed = System.currentTimeMillis() - timer.startTime;
        Assert.assertTrue("Timer should be counting", timer.isCounting());
        Thread.yield(); // yield to allow time for System.currentTimeMillis() to update
        Assert.assertTrue("Timer should still be counting", timer.isCounting());
    }

    

    // Your additional test cases here

    
}