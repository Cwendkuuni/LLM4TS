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
        long timeDifference = currentTime - timer.startTime;
        assertTrue(timer.isCounting());
        assertTrue(timeDifference <= 1000);
    }

    @Test
    public void testIsCounting_whenTimerIsStartedAndTimeIsUp_thenShouldReturnFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timer.startTime;
        timer.start(); // Start it again to make sure it's past the duration
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenShouldThrowAssertionError() {
        Timer timer = null; // not initialized
        timer.isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenShouldStartTheTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long timeDifference = currentTime - timer.startTime;
        assertTrue(timeDifference >= 0);
        assertTrue(timeDifference <= 1000);
    }

    @Test
    public void testStart_whenTimerIsAlreadyStarted_thenShouldDoNothing() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }
}