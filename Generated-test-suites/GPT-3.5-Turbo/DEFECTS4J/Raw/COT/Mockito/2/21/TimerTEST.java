
package org.mockito.internal.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        assertEquals(durationMillis, timer.getDurationMillis());
    }

    // Test case for isCounting method when timer is counting
    @Test
    public void testIsCounting_WhenCounting() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        timer.start();
        assertTrue(timer.isCounting());
    }

    // Test case for isCounting method when timer is not counting
    @Test
    public void testIsCounting_WhenNotCounting() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        assertFalse(timer.isCounting());
    }

    // Test case for start method
    @Test
    public void testStart() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        timer.start();
        assertTrue(timer.isCounting());
    }
  
    // Additional test case for start method to check if the start time is set correctly
    @Test
    public void testStart_CheckStartTime() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        timer.start();
        assertTrue(timer.getStartTime() != -1);
    }

}
