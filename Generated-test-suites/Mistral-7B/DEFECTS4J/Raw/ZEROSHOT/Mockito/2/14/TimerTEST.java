package org.junit.test; // replace with your package name if needed

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testTimerCreation() {
        Timer timer = new Timer(1000);
        Assert.assertNotNull(timer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testIsCountingInitialState() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterStart() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long delay = 10;
        long expectedEndTime = currentTime + delay;

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(timer.isCounting());
        Assert.assertTrue(System.currentTimeMillis() < expectedEndTime);
    }

    @Test
    public void testIsCountingAfterEnd() {
        Timer timer = new Timer(1000);
        timer.start();
        long delay = 2000;
        long currentTime = System.currentTimeMillis();

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.stop(); // simulate stopping the timer
        Assert.assertFalse(timer.isCounting());
    }
}