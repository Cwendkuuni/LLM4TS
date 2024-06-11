package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(5000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStartEdgeCase() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingError() {
        Timer timer = new Timer(5000);
        timer.isCounting(); // This should throw an AssertionError
    }

    @Test(expected = NullPointerException.class)
    public void testStartError() {
        Timer timer = new Timer(5000);
        timer.start(); // This should throw a NullPointerException
    }

}