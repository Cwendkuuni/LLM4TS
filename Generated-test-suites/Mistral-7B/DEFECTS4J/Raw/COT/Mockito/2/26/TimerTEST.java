package org.mockito.internal.util;

import org.junit.Test;
import org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testTimerConstruction() {
        Timer timer = new Timer(500);
        assertEquals(500, timer.durationMillis);
    }

    @Test
    public void testIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(500);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerHasFinished() {
        Timer timer = new Timer(500);
        timer.start();
        try {
            Thread.sleep(1001); // Sleep longer than the timer duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(500);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsNotRunning() {
        Timer timer = new Timer(500);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_whenTimerIsAlreadyRunning() {
        Timer timer = new Timer(500);
        timer.start();
        timer.start();
    }

    @Test
    public void testStart_whenTimerHasAlreadyFinished() {
        Timer timer = new Timer(500);
        timer.start();
        try {
            Thread.sleep(1001); // Sleep longer than the timer duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.start();
        assertFalse(timer.isCounting());
    }

}