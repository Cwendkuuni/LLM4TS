package org.junit.test; // replace with your testing package if necessary

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    @Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingWhenElapsedTimeExceeded() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerIsNotCountingWhenNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }
}