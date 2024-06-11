package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenTimerIsNotRunning_thenReturnsFalse() {
        Timer timer = new Timer(500);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        Timer timer = new Timer(500);
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        Thread.yield(); // yield the current thread to let other thread run
        assertTrue(timer.isCounting());
        long elapsedTime = currentTime - System.currentTimeMillis();
        assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test
    public void testIsCounting_whenTimerHasExpired_thenReturnsFalse() {
        Timer timer = new Timer(100);
        timer.start();
        Thread.sleep(150); // sleep for longer than the timer duration
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsAssertionError() {
        Timer timer = new Timer(500);
        assert timer.isCounting(); // this line should throw AssertionError
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
        Timer timer = new Timer(500);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNothing() {
        Timer timer = new Timer(500);
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

}