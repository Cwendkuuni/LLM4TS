package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenShouldReturnFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenShouldReturnTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 500;
        Thread.yield(); // To make sure the system time advances
        long currentTime = System.currentTimeMillis();

        assertTrue(timer.isCounting());
        long expectedElapsedTime = currentTime - startTime;
        assertTrue(expectedElapsedTime >= 0 && expectedElapsedTime <= 1000);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenShouldThrowAssertionError() {
        new Timer(1000).isCounting(); // The assert statement in the isCounting method is not satisfied
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenShouldStartCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 500;
        Thread.yield(); // To make sure the system time advances
        long currentTime = System.currentTimeMillis();

        assertTrue(timer.isCounting());
        long expectedElapsedTime = currentTime - startTime;
        assertTrue(expectedElapsedTime >= 0 && expectedElapsedTime <= 1000);
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenShouldNotAffectTheTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        timer.start();
        long elapsedTime = 1500;
        Thread.yield(); // To make sure the system time advances
        long currentTime = System.currentTimeMillis();

        assertTrue(timer.isCounting());
        long expectedElapsedTime = currentTime - startTime;
        assertTrue(expectedElapsedTime >= 1000 && expectedElapsedTime <= 2000);
    }
}