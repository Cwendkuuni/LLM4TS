package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCounting_whenTimerHasExpired() {
        Timer timer = new Timer(1);
        timer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerIsCounting_whenTimerHasNotBeenStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.isCounting();
    }

    @Test
    public void testTimerIsCounting_whenTimerStartedAndPaused() {
        Timer timer = new Timer(1000);
        timer.start();
        long pauseTime = System.currentTimeMillis();
        timer.pause();
        long resumeTime = System.currentTimeMillis();
        timer.resume();
        assertTrue(timer.isCounting());
        long elapsedTime = System.currentTimeMillis() - resumeTime;
        assertTrue(elapsedTime <= durationMillis);
    }

    @Test
    public void testTimerIsNotCounting_whenTimerHasBeenPaused() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.pause();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_whenTimerHasBeenResumed() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.pause();
        timer.resume();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCounting_whenTimerHasReachedZero() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerConstructor_whenDurationIsNegative() {
        new Timer(-1);
    }

    @Test
    public void testTimerIsCounting_whenTimerIsReset() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.reset();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerIsCounting_whenTimerIsResetAndRestarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.reset();
        timer.start();
        assertTrue(timer.isCounting());
    }

}