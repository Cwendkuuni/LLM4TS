package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithEdgeCases() {
        char[] digitCharsMin = new char[]{'-', '1', '2', '3'};
        int expectedMin = -123;
        int actualMin = NumberInput.parseInt(digitCharsMin, 0, 4);
        assertEquals(expectedMin, actualMin);

        char[] digitCharsMax = new char[]{'1', '2', '3', '9', '9'};
        int expectedMax = Integer.MAX_VALUE;
        int actualMax = NumberInput.parseInt(digitCharsMax, 0, 5);
        assertEquals(expectedMax, actualMax);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitCharsWithSymbol = new char[]{'1', '2', '3', '+', '4'};
        int actual = NumberInput.parseInt(digitCharsWithSymbol, 0, 5);
        // JUnit will throw an exception if actual is not of the expected type
    }

    // Test other methods in NumberInput class similarly
}