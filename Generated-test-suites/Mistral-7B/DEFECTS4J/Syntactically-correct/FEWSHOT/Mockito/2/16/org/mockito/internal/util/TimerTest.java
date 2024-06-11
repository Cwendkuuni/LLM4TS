package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerHasElapsed_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long waitTime = 1500;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsAssertionError() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotThrowException() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}