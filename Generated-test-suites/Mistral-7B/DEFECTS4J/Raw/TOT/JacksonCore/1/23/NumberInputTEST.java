package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseIntWithLongInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', 'a'};
        int expected = -1;
        int actual = -1;
        try {
            actual = NumberInput.parseInt(digitChars, 0, 3);
        } catch (NumberFormatException e) {
            actual = -1;
        }
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseIntWithNegativeInput() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        long actual = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseLongWithNegativeInput() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = -123456789;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', 'a'};
        long expected = -1;
        long actual = -1;
        try {
            actual = NumberInput.parseLong(digitChars, 0, 3);
        } catch (NumberFormatException e) {
            actual = -1;
        }
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseLongWithLongInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0'};
        long expected = 12345678900L;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String numStr = "1234567890.12345";
        BigDecimal expected = new BigDecimal("1234567890.12345");
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseBigDecimalWithInvalidInput() {
        String numStr = "123a45";
        BigDecimal expected = null;
        BigDecimal actual = null;
        try {
            actual = NumberInput.parseBigDecimal(numStr);
        } catch (NumberFormatException e) {
            // expected
        }
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }

    @Test
    public void testParseBigDecimalWithEmptyString() {
        String numStr = "";
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
    }
}