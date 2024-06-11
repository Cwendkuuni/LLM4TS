package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {
    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsStartedAndHasNotExpired_thenReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 500;
        long expectedRemainingTime = 500;

        // Simulate the passage of time
        long currentTime = startTime + elapsedTime;
        while (timer.isCounting() && currentTime < System.currentTimeMillis()) {
            currentTime = System.currentTimeMillis();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assertTrue(timer.isCounting());
        long remainingTime = durationMillis - (System.currentTimeMillis() - startTime);
        assertEquals(expectedRemainingTime, remainingTime);
    }

    @Test
    public void testIsCounting_whenTimerIsStartedAndHasExpired_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = durationMillis + 1000;

        // Simulate the passage of time
        while (timer.isCounting()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenThrowsException() {
        new Timer(1000).isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenStartsTheTimer() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.start();
        long startTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedTime = 500;

        // Simulate the passage of time
        long currentTime = startTime + elapsedTime;
        while (timer.isCounting() && currentTime < System.currentTimeMillis()) {
            currentTime = System.currentTimeMillis();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long remainingTime = durationMillis - (System.currentTimeMillis() - startTime);
        assertEquals(500, remainingTime);
    }

    @Test
    public void testStart_whenTimerIsAlreadyStarted_thenDoesNotStartItAgain() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        timer.start();

        long elapsedTime = 500;

        // Simulate the passage of time
        long currentTime = startTime + elapsedTime;
        while (timer.isCounting() && currentTime < System.currentTimeMillis()) {
            currentTime = System.currentTimeMillis();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assertTrue(timer.isCounting());
        long remainingTime = durationMillis - (System.currentTimeMillis() - startTime);
        assertEquals(500, remainingTime);
    }

    @Test
    public void testStart_whenTimerIsStartedAndHasExpired_thenDoesNotStartItAgain() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = durationMillis + 1000;

        // Let the timer expire
        while (timer.isCounting()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assertFalse(timer.isCounting());
        timer.start();

        // Simulate the passage of time
        long currentTime = System.currentTimeMillis();
        while (!timer.isCounting() && currentTime < System.currentTimeMillis()) {
            currentTime = System.currentTimeMillis();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assertTrue(timer.isCounting());
        long remainingTime = durationMillis - (System.currentTimeMillis() - startTime);
        assertEquals(0, remainingTime);
    }
}