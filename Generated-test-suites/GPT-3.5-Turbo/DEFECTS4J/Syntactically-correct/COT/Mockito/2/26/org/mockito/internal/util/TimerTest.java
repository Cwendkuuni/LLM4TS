package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCountingWhenTimerIsInProgress() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerIsNotStarted() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCountingWhenTimerIsCompleted() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(2000); // Wait for timer to complete
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertNotEquals(-1, timer.startTime);
    }

}