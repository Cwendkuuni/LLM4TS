package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int offset = 0, len = 5;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        int expected = 12345;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithLongerInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 10;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        int expected = 123456789;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'x'};
        int offset = 0, len = 6;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        int expected = Integer.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int offset = 0, len = 11;
        long actual = NumberInput.parseLong(digitChars, offset, len);
        long expected = 12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'x'};
        int offset = 0, len = 11;
        long actual = NumberInput.parseLong(digitChars, offset, len);
        long expected = Long.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testParseLongWithStringInput() {
        String input = "12345678901";
        long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        long expected = 12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithStringInputLongerThanNeeded() {
        String input = "123456789011234567890";
        long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        long expected = 12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidStringInput() {
        String input = "12345678901x";
        long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        long expected = Long.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testParseLongWithNegativeInput() {
        String input = "-12345678901";
        long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        long expected = -12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithNegativeInputLongerThanNeeded() {
        String input = "--12345678901";
        long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        long expected = -12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidNegativeInput() {
        String input = "--12345678901x";
        long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        long expected = Long.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testInLongRangeWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean negative = false;
        assertTrue(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
    }

    @Test
    public void testInLongRangeWithLongerInputThanNeeded() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean negative = false;
        assertTrue(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
    }

    @Test
    public void testInLongRangeWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'x'};
        boolean negative = false;
        assertFalse(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
    }

    @Test
    public void testInLongRangeWithNegativeInput() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean negative = true;
        assertTrue(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "12345";
        int actual = NumberInput.parseAsInt(input, 0);
        int expected = 12345;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithLongerInputThanNeeded() {
        String input = "123456";
        int actual = NumberInput.parseAsInt(input, 0);
        int expected = 12345;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "1234x";
        int actual = NumberInput.parseAsInt(input, 0);
        int expected = Integer.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testParseAsIntWithNegativeInput() {
        String input = "-1234";
        int actual = NumberInput.parseAsInt(input, 0);
        int expected = -1234;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "12345678901";
        long actual = NumberInput.parseAsLong(input, 0);
        long expected = 12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithLongerInputThanNeeded() {
        String input = "12345678901234567890";
        long actual = NumberInput.parseAsLong(input, 0);
        long expected = 12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "12345678901x";
        long actual = NumberInput.parseAsLong(input, 0);
        long expected = Long.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testParseAsLongWithNegativeInput() {
        String input = "-12345678901";
        long actual = NumberInput.parseAsLong(input, 0);
        long expected = -12345678901L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double actual = NumberInput.parseAsDouble(input, 0);
        double expected = 123.45;
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithLongerInputThanNeeded() {
        String input = "123.456";
        double actual = NumberInput.parseAsDouble(input, 0);
        double expected = 123.45;
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "123.4x";
        double actual = NumberInput.parseAsDouble(input, 0);
        double expected = Double.MIN_VALUE;
        assert(actual < expected);
    }

    @Test
    public void testParseAsDoubleWithExponentInput() {
        String input = "1.23e5";
        double actual = NumberInput.parseAsDouble(input, 0);
        double expected = 123000.0;
        assertEquals(expected, actual, 0.001);
    }
}