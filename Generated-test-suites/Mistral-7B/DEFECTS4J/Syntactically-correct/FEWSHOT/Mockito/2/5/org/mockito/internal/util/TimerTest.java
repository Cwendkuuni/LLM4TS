package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsNotRunning_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - (timer.getStartTime() + 500); // Let the timer run for a short while
        assertTrue(timer.isCounting());
        assertFalse(timer.isCounting()); // The timer should have stopped by now
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenThrowsAssertionError() {
        Timer timer = new Timer(-1);
        timer.isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.getStartTime();
        assertTrue(elapsedTime >= 0);
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotChangeTimerState() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.getStartTime();
        assertTrue(elapsedTime >= 0);
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testGetStartTime_whenTimerIsRunning_thenReturnsStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = timer.getStartTime();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        assertTrue(elapsedTime >= 0);
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testGetStartTime_whenTimerIsNotRunning_thenReturnsNegativeValue() {
        Timer timer = new Timer(1000);
        assertNegative(timer.getStartTime());
    }
}