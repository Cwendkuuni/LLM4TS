package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunningAndDurationHasNotExpired_thenTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = 500;
        long expectedRemainingTime = 500;

        // Simulate the passage of time
        currentTime += elapsedTime;

        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting());
        assertEquals(expectedRemainingTime, timer.durationMillis - (currentTime - timer.startTime));
    }

    @Test
    public void testIsCounting_whenTimerIsRunningAndDurationHasExpired_thenFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = 1500;

        // Simulate the passage of time
        currentTime += elapsedTime;

        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenAssertionError() {
        Timer timer = null;
        timer.isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotStartTimerAgain() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsRunningAndReset_thenResetsTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = 500;

        // Simulate the passage of time
        currentTime += elapsedTime;

        timer.start();

        assertTrue(timer.isCounting());
        assertEquals(500, timer.durationMillis - (currentTime - timer.startTime));
    }

    @Test
    public void testStart_whenTimerIsRunningAndResetWithNegativeValue_thenThrowsAssertionError() {
        Timer timer = new Timer(1000);
        timer.start();

        try {
            timer.start();
        } catch (AssertionError e) {
            // expected
            return;
        }

        throw new TestException("Test case expected AssertionError but got none");
    }
}