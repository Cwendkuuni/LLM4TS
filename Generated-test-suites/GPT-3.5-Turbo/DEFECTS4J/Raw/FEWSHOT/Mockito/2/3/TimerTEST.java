package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_whenTimerIsNotStarted_shouldReturnFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsStartedAndNotExpired_shouldReturnTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsStartedAndExpired_shouldReturnFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(2000); // Wait for timer to expire
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_shouldSetStartTime() {
        timer.start();
        assertTrue(timer.isCounting()); // Timer should be counting after start
    }
}