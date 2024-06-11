package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_WhenTimerIsNotStarted_ReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndNotExpired_ReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndExpired_ReturnsFalse() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(2000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_WhenTimerIsNotStarted_UpdatesStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        long expectedStartTime = System.currentTimeMillis();
        assertEquals(expectedStartTime, timer.getStartTime(), 1000);
    }

    @Test
    public void testStart_WhenTimerIsAlreadyStarted_DoesNotUpdateStartTime() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        long initialStartTime = timer.getStartTime();
        Thread.sleep(200);
        timer.start();
        assertEquals(initialStartTime, timer.getStartTime());
    }

}