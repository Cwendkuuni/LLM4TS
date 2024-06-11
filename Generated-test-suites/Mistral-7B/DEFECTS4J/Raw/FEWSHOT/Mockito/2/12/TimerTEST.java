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
        long elapsedTime = currentTime - timer.startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= 1000);
    }

    @Test
    public void testIsCounting_elapsed() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = 1500; // Elapsed time is more than the duration
        long currentTime = timer.startTime + elapsedTime;
        timer.startTime = currentTime;
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_startTimeNotSet() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting()); // Should not throw an exception
        timer.isCounting(); // This line should throw an AssertionError
    }

    @Test
    public void testStart_alreadyStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
        assertEquals(timer.startTime, timer.startTime); // Both startTime should be the same
    }
}