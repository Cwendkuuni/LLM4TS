package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstructor() {
        Timer timer = new Timer(1000);
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testIsCountingWhenNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenStartedAndElapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsed = 500;
        long currentTime = System.currentTimeMillis() - elapsed;
        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting() && System.currentTimeMillis() - currentTime <= 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCountingWhenNotStartedAndElapsed() {
        Timer timer = new Timer(1000);
        long elapsed = 1500;
        timer.isCounting();
    }

    @Test
    public void testIsCountingWhenStartedAndExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsed = 2000;
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenStartedAndElapsedLessThanExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsed = 500;
        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting() && System.currentTimeMillis() - elapsed <= 1000);
    }

    @Test
    public void testStartMethod() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsed = 500;
        long currentTime = System.currentTimeMillis() - elapsed;
        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting() && System.currentTimeMillis() - currentTime <= 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartMethodWhenAlreadyStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}