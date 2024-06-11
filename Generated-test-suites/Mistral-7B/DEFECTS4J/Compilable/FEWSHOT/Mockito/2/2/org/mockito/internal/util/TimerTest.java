package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testIsCounting_timerExpired() {
        Timer timer = new Timer(1);
        timer.start();
        long startTime = System.currentTimeMillis();
        Thread.yield(); // yield to other threads to make sure System.currentTimeMillis() changes
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerNotInitialized() {
        new Timer(1000).isCounting(); // no startTime set
    }
}