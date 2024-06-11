package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntSmall() {
        char[] digitChars = new char[]{'1'};
        int result = NumberInput.parseInt(digitChars, 0, 1);
        assertEquals(1, result);
    }

    @Test
    public void testParseIntLarge() {
        char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int result = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(1000000005, result); // 10^9 + 5
    }

    @Test
    public void testParseIntNegativeSmall() {
        char[] digitChars = new char[]{'-', '1'};
        int result = NumberInput.parseInt(digitChars, 0, 2);
        assertEquals(-1, result);
    }

    @Test
    public void testParseIntNegativeLarge() {
        char[] digitChars = new char[]{'-', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int result = NumberInput.parseInt(digitChars, 0, 11);
        assertEquals(-1000000006, result); // -10^9 + 6
    }

    @Test
    public void testParseIntException() {
        char[] digitChars = new char[]{'1', '0', '1'};
        try {
            NumberInput.parseInt(digitChars, 0, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongSmall() {
        char[] digitChars = new char[]{'1', '2', '3'};
        long result = NumberInput.parseLong(digitChars, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseLongLarge() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 15);
        assertEquals(12345678999999999L, result);
    }

    @Test
    public void testParseLongNegativeSmall() {
        char[] digitChars = new char[]{'-', '1', '2', '3'};
        long result = NumberInput.parseLong(digitChars, 0, 4);
        assertEquals(-123, result);
    }

    @Test
    public void testParseLongNegativeLarge() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 15);
        assertEquals(-123456789999999999L, result);
    }

    @Test
    public void testParseLongException() {
        char[] digitChars = new char[]{'1', '2', '0'};
        try {
            NumberInput.parseLong(digitChars, 0, 3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '0'};
        try {
            NumberInput.parseLong(digitChars, 0, 16);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongInLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 14, false));
    }

    @Test
    public void testParseLongInLongRangeNegative() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 13, true));
    }

    @Test
    public void testParseStringInt() {
        String input = "123";
        int result = NumberInput.parseInt(input);
        assertEquals(123, result);
    }

    @Test
    public void testParseStringNegativeInt() {
        String input = "-123";
        int result = NumberInput.parseInt(input);
        assertEquals(-123, result);
    }

    @Test
    public void testParseStringException() {
        String input = "abcd";
        try {
            NumberInput.parseInt(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseStringOutOfRange() {
        String input = "1234567890123";
        try {
            NumberInput.parseInt(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseStringLong() {
        String input = "1234567890123";
        long result = NumberInput.parseLong(input);
        assertEquals(1234567890123L, result);
    }

    @Test
    public void testParseStringNegativeLong() {
        String input = "-1234567890123";
        long result = NumberInput.parseLong(input);
        assertEquals(-1234567890123L, result);
    }

    @Test
    public void testParseStringExceptionLong() {
        String input = "abcd123";
        try {
            NumberInput.parseLong(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseStringOutOfRangeLong() {
        String input = "12345678901234567890123";
        try {
            NumberInput.parseLong(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }
}