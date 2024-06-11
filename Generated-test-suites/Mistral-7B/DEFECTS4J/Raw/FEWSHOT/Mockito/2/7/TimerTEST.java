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
    public void testIsCounting_whenTimerIsStartedAndTimeRemains_thenShouldReturnTrue() {
        Timer timer = new Timer(1000);
        timer.start();

        long currentTime = System.currentTimeMillis();

        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() - currentTime < 1001);
    }

    @Test
    public void testIsCounting_whenTimerIsStartedAndTimeIsUp_thenShouldReturnFalse() {
        Timer timer = new Timer(1000);
        timer.start();

        long delay = 1500; // Delay more than the timer duration
        long currentTime = System.currentTimeMillis();

        // Simulate time passing
        while (System.currentTimeMillis() - currentTime < delay) {
            Thread.yield();
        }

        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenShouldThrowAssertionError() {
        new Timer(null);
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenShouldStartCounting() {
        Timer timer = new Timer(1000);

        timer.start();

        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsAlreadyStarted_thenShouldNotChangeCountingStatus() {
        Timer timer = new Timer(1000);
        timer.start();

        timer.start();

        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsStartedAndReset_thenShouldStartCountingAgain() {
        Timer timer = new Timer(1000);
        timer.start();

        timer.start();

        // Wait for the timer to expire
        long currentTime = System.currentTimeMillis();
        while (timer.isCounting()) {
            Thread.yield();
        }

        timer.start();

        assertTrue(timer.isCounting());
    }
}