package org.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000L);
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenShouldReturnTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerHasElapsed_thenShouldReturnFalse() {
        timer.start();
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_whenTimerIsNotRunning_thenShouldThrowException() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenShouldReturnFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerDurationReached_thenShouldReturnFalse() {
        timer.start();
        try {
            Thread.sleep(1500L);
            timer.start(); // second start should not change the result
            assertFalse(timer.isCounting());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}