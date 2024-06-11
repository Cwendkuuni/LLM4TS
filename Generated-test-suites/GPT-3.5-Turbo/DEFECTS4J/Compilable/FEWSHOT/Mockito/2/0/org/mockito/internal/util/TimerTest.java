package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {
    @Test
    public void testIsCounting() {
        // Test when the timer is counting
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test when the timer is not counting
        timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
}