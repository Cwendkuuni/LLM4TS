package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCountingPositive() {
        Timer timer = new Timer(1000); // Timer with 1 second duration
        timer.start();
        assertTrue("Timer should be counting down", timer.isCounting());
    }

    @Test
    public void testIsCountingNegative() {
        Timer timer = new Timer(1000); // Timer with 1 second duration
        assertFalse("Timer should not be counting down", timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000); // Timer with 1 second duration
        timer.start();
        assertTrue("Start time should be initialized", timer.startTime != -1);
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStarting() {
        Timer timer = new Timer(1000); // Timer with 1 second duration
        timer.isCounting();
    }

    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(Long.MAX_VALUE - 1); // Timer with almost maximum duration
        timer.start();
        assertFalse("Timer should not be counting down due to overflow", timer.isCounting());
    }

    @Test
    public void testIsCountingTimeTravel() {
        Timer timer = new Timer(1000); // Timer with 1 second duration
        timer.start();
        timer.startTime -= 2000; // Simulate time travel
        assertFalse("Timer should not be counting down due to time travel", timer.isCounting());
    }

}