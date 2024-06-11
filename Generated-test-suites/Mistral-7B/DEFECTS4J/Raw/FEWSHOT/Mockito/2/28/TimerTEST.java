package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_notStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_counting() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long delay = 500;
        Thread.sleep(delay);
        assertTrue(timer.isCounting());
        long elapsedTime = System.currentTimeMillis() - currentTime;
        assertTrue(elapsedTime > delay && elapsedTime <= 1000);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_negativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testIsCounting_elapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        long delay = 1500;
        Thread.sleep(delay);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_zeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
        // assertIsInstanceOf(AssertionError.class, expected exception);
    }

    @Test
    public void testStart_alreadyStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testStart_zeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
    }

    @Test
    public void testStart_negativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testStart_alreadyElapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        long delay = 1500;
        Thread.sleep(delay);
        timer.start();
    }

    @Test
    public void testStart_null() {
        Timer timer = new Timer(1000);
        assertNull(timer.startTime);
        timer = new Timer(1000);
        timer.start();
        assertNotNull(timer.startTime);
    }
}