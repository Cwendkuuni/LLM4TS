package org.mockito.internal.util;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000); // Typical use case with a duration of 1 second
    }

    @Test
    public void testIsCounting() {
        assertFalse(timer.isCounting()); // Timer has not started yet, should return false

        timer.start();
        assertTrue(timer.isCounting()); // Timer has started, should return true

        try {
            Thread.sleep(2000); // Sleep for 2 seconds to exceed the duration of the timer
            assertFalse(timer.isCounting()); // Timer has exceeded the duration, should return false
        } catch (InterruptedException e) {
            fail("Thread sleep interrupted");
        }
    }

    @Test
    public void testStart() {
        timer.start();
        assertTrue(timer.isCounting()); // Timer has started, should return true
    }

}