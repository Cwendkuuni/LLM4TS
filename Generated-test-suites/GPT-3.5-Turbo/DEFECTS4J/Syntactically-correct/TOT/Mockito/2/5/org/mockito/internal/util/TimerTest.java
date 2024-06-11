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
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndDurationNotReached_ReturnsTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndDurationReached_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(1100); // Sleep for longer than the timer duration (1000ms)
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_WhenTimerIsNotStarted_ShouldSetStartTime() {
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }

    @Test
    public void testStart_WhenTimerIsAlreadyStarted_ShouldResetStartTime() {
        timer.start();
        long initialStartTime = timer.getStartTime();

        try {
            Thread.sleep(500); // Sleep for 500ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.start();
        assertNotEquals(initialStartTime, timer.getStartTime());
    }
}