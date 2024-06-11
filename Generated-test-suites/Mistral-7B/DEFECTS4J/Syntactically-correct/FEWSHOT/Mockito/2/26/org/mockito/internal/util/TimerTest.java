package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerCreation() {
        Timer timer = new Timer(100);
        assertNotNull(timer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerNegativeDuration() {
        new Timer(-1);
    }

    @Test
    public void testIsCountingStarted() {
        Timer timer = new Timer(100);
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingElapsedTime() {
        Timer timer = new Timer(100);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        assertTrue(timer.isCounting());
        assertNotEquals(elapsedTime, timer.durationMillis);
        assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test
    public void testIsCountingEnded() {
        Timer timer = new Timer(100);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        timer.start(); // This should not change the result
        assertTrue(timer.isCounting());
        elapsedTime += 101; // Elapsed time is more than the duration
        assertFalse(timer.isCounting());
    }
}