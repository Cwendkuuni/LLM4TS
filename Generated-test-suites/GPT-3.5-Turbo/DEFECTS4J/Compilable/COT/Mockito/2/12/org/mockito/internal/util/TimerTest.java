package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(500);
        assertFalse(timer.isCounting());

        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(500);
        timer.start();
        assertTrue(timer.isCounting());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testIsCountingWithZeroDuration() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStarting() {
        Timer timer = new Timer(500);
        timer.isCounting();
    }

}