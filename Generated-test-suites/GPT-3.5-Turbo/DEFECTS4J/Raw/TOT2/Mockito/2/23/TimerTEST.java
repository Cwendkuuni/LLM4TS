package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1100); // Wait for timer to reach duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStartTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = timer.startTime;
        timer.start();
        assertEquals(startTime, timer.startTime);
    }

    @Test
    public void testNegativeDuration() {
        Timer timer = new Timer(-1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}