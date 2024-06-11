package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCountingReturnsTrueWhenTimerIsStillCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingReturnsFalseWhenTimerIsCompleted() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStartSetsStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    @Test
    public void testStartResetsStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingThrowsAssertionErrorIfStartNotCalled() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }
}