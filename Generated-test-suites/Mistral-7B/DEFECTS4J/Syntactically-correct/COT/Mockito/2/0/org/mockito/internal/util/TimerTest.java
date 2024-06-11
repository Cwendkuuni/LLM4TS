package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCounting_whenTimerHasFinished() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.wait(timer.durationMillis + 10); // let the timer finish
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerStart_whenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerStart_whenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}