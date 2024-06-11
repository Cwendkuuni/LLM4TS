package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCountingWhenNotStarted() {
        Timer timer = new Timer(1000L);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenStartedAndWithinDuration() {
        Timer timer = new Timer(1000L);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 500L;

        long expectedEndTime = startTime + timer.durationMillis;
        long currentTime = startTime + elapsedTime;

        assertTrue(timer.isCounting());
        assertTrue(currentTime <= expectedEndTime);
    }

    @Test
    public void testIsCountingWhenStartedAndBeyondDuration() {
        Timer timer = new Timer(1000L);
        timer.start();
        long elapsedTime = timer.durationMillis + 500L;

        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWhenStartTimeIsNotSet() {
        Timer timer = new Timer(1000L);
        assertFalse(timer.isCounting());
        timer.isCounting();
    }

    @Test
    public void testStartMethod() {
        Timer timer = new Timer(1000L);
        long startTime = System.currentTimeMillis();
        timer.start();

        long expectedEndTime = startTime + timer.durationMillis;
        long currentTime = System.currentTimeMillis();

        assertTrue(timer.isCounting());
        assertTrue(currentTime <= expectedEndTime);
    }
}