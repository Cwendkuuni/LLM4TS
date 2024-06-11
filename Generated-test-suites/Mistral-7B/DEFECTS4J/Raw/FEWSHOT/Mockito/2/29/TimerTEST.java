package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerStarted_lessThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = 500; // Half the duration
        long currentTime = System.currentTimeMillis() - elapsedTime;
        assertTrue(timer.isCounting());
        long remainingTime = timer.durationMillis - (System.currentTimeMillis() - timer.startTime);
        assertTrue(remainingTime >= 0);
        assertTrue(remainingTime <= timer.durationMillis);
    }

    @Test
    public void testIsCounting_timerStarted_equalToDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = timer.durationMillis;
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long remainingTime = timer.durationMillis - (System.currentTimeMillis() - timer.startTime);
        assertEquals(0, remainingTime);
    }

    @Test
    public void testIsCounting_timerStarted_moreThanDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = timer.durationMillis * 2;
        long currentTime = System.currentTimeMillis();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        assertNotNull(timer.startTime);
        assertFalse(timer.isCounting());
        timer.start();
        long elapsedTime = 500;
        long currentTime = System.currentTimeMillis() - elapsedTime;
        assertTrue(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testStartTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testGetDurationMillis() {
        Timer timer = new Timer(1000);
        assertEquals(1000, timer.durationMillis);
    }
}