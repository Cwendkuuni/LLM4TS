package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 500;
        long expectedEndTime = startTime + elapsedTime;

        while (System.currentTimeMillis() < expectedEndTime) {
            // Simulate time passing
            timer.sleep(10);
        }

        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsException() {
        timer.isCounting();
    }

    @Test
    public void testIsCounting_whenTimerHasExpired_thenReturnsFalse() {
        timer.start();
        long startTime = System.currentTimeMillis();
        timer.sleep(durationPlusOneMillisecond(timer.durationMillis));

        assertFalse(timer.isCounting());
    }

    private long durationPlusOneMillisecond(long durationMillis) {
        return durationMillis + 1;
    }

    @Test
    public void testStart_whenTimerIsNotStarted_thenStartsCounting() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_whenTimerIsAlreadyRunning_thenThrowsException() {
        timer.start();
        timer.start();
    }

    @Test
    public void testSleep_whenMillisIsZero_thenDoesNotBlock() {
        timer.sleep(0);
    }

    @Test
    public void testSleep_whenMillisIsGreaterThanZero_thenBlocksForSpecifiedTime() {
        long startTime = System.currentTimeMillis();
        long millisToSleep = 500;

        timer.sleep(millisToSleep);

        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime >= millisToSleep);
    }

    @Test
    public void testSleep_whenTimerIsExpired_thenThrowsInterruptedException() {
        timer.start();
        timer.sleep(durationMillis);

        try {
            timer.sleep(1);
            fail("Expected InterruptedException");
        } catch (InterruptedException e) {
            // Expected
        }
    }

}