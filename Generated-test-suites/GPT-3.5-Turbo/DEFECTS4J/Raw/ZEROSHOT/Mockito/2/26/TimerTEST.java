package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000); // Set duration to 1 second
    }

    @Test
    public void testIsCounting() {
        // Timer should not be counting initially
        assertFalse(timer.isCounting());

        // Start the timer
        timer.start();

        // Timer should be counting after starting
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        // Timer should not be counting initially
        assertFalse(timer.isCounting());

        // Start the timer
        timer.start();

        // Timer should be counting after starting
        assertTrue(timer.isCounting());
    }
}