package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testIsCounting_whenTimerHasExpired_thenReturnsFalse() {
        Timer timer = new Timer(1);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime <= 1);
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_whenTimerHasNotBeenInitialized_thenThrowsException() {
        Timer timer = null;
        timer.isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotAffectTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        timer.start();
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= 1000);
    }
}