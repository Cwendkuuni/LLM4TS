package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_whenDurationNotElapsed() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenDurationElapsed() throws InterruptedException {
        Timer timer = new Timer(5000);
        timer.start();
        Thread.sleep(6000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenDurationMillisIsZero() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenStartTimeNotSet() {
        Timer timer = new Timer(5000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }
}