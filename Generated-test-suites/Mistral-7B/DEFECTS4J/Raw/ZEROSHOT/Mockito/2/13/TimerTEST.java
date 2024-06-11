package org.junit.test; // replace with your package name if needed

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
    public void testTimerIsNotCountingAfterExpiration() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(2000); // sleep for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerIsNotCountingBeforeStart() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.stop(); // add this line to test the IllegalStateException when calling isCounting before start
    }

    @Test
    public void testTimerIsCountingWhenStartedAgain() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.stop();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerStartTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerDurationLessThanZero() {
        try {
            new Timer(-1000);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testTimerDurationZero() {
        Timer timer = new Timer(0);
        timer.start();
        assertTrue(timer.isCounting());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(timer.isCounting());
    }

}