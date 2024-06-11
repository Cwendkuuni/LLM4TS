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
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_whenTimerStarted_shouldReturnTrue() {
        timer.start();
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - 1001; // start timer with a delay of 1ms to test edge case
        System.setCurrentTimeMillis(startTime);
        assertTrue(timer.isCounting());
        System.setCurrentTimeMillis(currentTime);
    }

    @Test
    public void testIsCounting_whenTimerNotStarted_shouldReturnFalse() {
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_whenTimerHasNotBeenInitialized_shouldThrowException() {
        assertFalse(new Timer(-1).isCounting());
    }

    @Test
    public void testIsCounting_whenTimerHasElapsed_shouldReturnFalse() {
        timer.start();
        long elapsedTime = System.currentTimeMillis() - (timer.getDurationMillis() + 100);
        System.setCurrentTimeMillis(elapsedTime);
        assertFalse(timer.isCounting());
        System.setCurrentTimeMillis(System.currentTimeMillis());
    }

    @Test
    public void testStart_whenTimerHasNotBeenStarted_shouldSetStartTime() {
        long startTimeBefore = System.currentTimeMillis();
        timer.start();
        long startTimeAfter = System.currentTimeMillis();
        assertTrue(startTimeAfter > startTimeBefore);
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_whenTimerHasAlreadyBeenStarted_shouldThrowException() {
        timer.start();
        timer.start();
    }

}