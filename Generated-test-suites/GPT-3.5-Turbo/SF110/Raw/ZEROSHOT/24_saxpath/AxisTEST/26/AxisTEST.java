package org.saxpath;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AxisTest {

    @Before
    public void setUp() {
        // Set up any necessary test data or objects
    }

    @Test
    public void testLookupWithValidAxisNum() {
        String expected = "child";
        String actual = Axis.lookup(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithInvalidAxisNum() {
        String expected = null;
        String actual = Axis.lookup(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithValidAxisName() {
        int expected = 1;
        int actual = Axis.lookup("child");
        assertEquals(expected, actual);
    }

    @Test
    public void testLookupWithInvalidAxisName() {
        int expected = 0;
        int actual = Axis.lookup("invalid-axis");
        assertEquals(expected, actual);
    }

    // Add more test methods for other methods in the Axis class

}