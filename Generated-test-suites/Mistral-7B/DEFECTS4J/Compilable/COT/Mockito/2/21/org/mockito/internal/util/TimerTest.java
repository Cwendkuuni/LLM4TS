package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue("Timer should be counting", timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_whenTimerHasElapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1500); // let the timer elapse by 500ms
        } catch (InterruptedException e) {
            // ignore
        }
        assertFalse("Timer should not be counting after elapsed", timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        assertFalse("Timer should not be counting before start", timer.isCounting());
    }

    @Test
    public void testTimerStart_whenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue("Timer should be counting after start", timer.isCounting());
    }

    @Test
    public void testTimerStart_whenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertTrue("Timer should still be counting after starting while already running", timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructor_whenDurationMillisIsLessThanZero() {
        new Timer(-1);
    }
}