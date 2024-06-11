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
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - (timer.startTime + 100); // simulate some elapsed time

        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() - timer.startTime <= timer.durationMillis);
    }

    @Test
    public void testIsCounting_whenTimerHasElapsed_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = timer.durationMillis + 100;

        assertFalse(timer.isCounting());
        assertTrue(System.currentTimeMillis() - timer.startTime > timer.durationMillis);
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenThrowsIllegalStateException() {
        Timer timer = new Timer(1000);
        timer.start();

        assertThrows(IllegalStateException.class, timer::start);
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsCounting() {
        Timer timer = new Timer(1000);

        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime;

        assertTrue(timer.isCounting());
        assertTrue(System.currentTimeMillis() - timer.startTime <= timer.durationMillis);
    }
}