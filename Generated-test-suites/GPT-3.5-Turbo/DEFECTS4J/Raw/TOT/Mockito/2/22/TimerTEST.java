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
    public void testIsCounting_WhenTimerNotStarted_ShouldReturnFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndElapsedDuration_ShouldReturnFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndNotElapsedDuration_ShouldReturnTrue() throws InterruptedException {
        timer.start();
        Thread.sleep(500);
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_ShouldUpdateStartTime() {
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }
}