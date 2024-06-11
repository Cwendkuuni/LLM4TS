package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(5000); // Set an arbitrary duration for testing
    }

    @Test
    public void testIsCountingWhenTimerNotStarted() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerStarted() throws InterruptedException {
        timer.start();
        Thread.sleep(1000); // Wait for 1 second
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerElapsed() throws InterruptedException {
        timer.start();
        Thread.sleep(5000); // Wait for 5 seconds (equal to the duration)
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testMultipleStarts() {
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

}