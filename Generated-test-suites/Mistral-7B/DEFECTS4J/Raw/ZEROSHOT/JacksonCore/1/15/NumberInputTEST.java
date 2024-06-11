package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int expected = 123;
        int actual = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidLength() {
        char[] digitChars = new char[]{'1', '2'};
        NumberInput.parseInt(digitChars, 0, 2);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNegativeDigit() {
        char[] digitChars = new char[]{'-', '1'};
        NumberInput.parseInt(digitChars, 0, 1);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7'};
        long expected = 1234567;
        long actual = NumberInput.parseLong(digitChars, 0, 7);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidLength() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        NumberInput.parseLong(digitChars, 0, 5);
    }

    @Test
    public void testParseLongWithBigNumber() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long expected = Long.MAX_VALUE;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithNegativeNumber() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = Long.MIN_VALUE;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', 'a'};
        try {
            NumberInput.parseLong(digitChars, 0, 2);
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongWithInLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean result = NumberInput.inLongRange(digitChars, 0, 11, false);
        assertTrue(result);

        digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        result = NumberInput.inLongRange(digitChars, 0, 11, true);
        assertTrue(result);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '10'};
        result = NumberInput.inLongRange(digitChars, 0, 11, false);
        assertFalse(result);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '11'};
        result = NumberInput.inLongRange(digitChars, 0, 11, false);
        assertFalse(result);
    }

    @Test
    public void testParseStringWithValidIntInput() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseStringWithInvalidIntInput() {
        String input = "1a3";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseStringWithNegativeIntInput() {
        String input = "-123";
        int expected = -123;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseStringWithBigIntInput() {
        String input = "9223372036854775807";
        long expected = Long.MAX_VALUE;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseStringWithNegativeBigIntInput() {
        String input = "-9223372036854775808";
        long expected = Long.MIN_VALUE;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseStringWithInvalidBigIntInput() {
        String input = "1a3";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseStringWithInLongRange() {
        String input = "12345678901234567890";
        boolean result = NumberInput.inLongRange(input, false);
        assertTrue(result);

        input = "-12345678901234567890";
        result = NumberInput.inLongRange(input, true);
        assertTrue(result);

        input = "12345678901234567891";
        result = NumberInput.inLongRange(input, false);
        assertFalse(result);

        input = "12345678901234567892";
        result = NumberInput.inLongRange(input, false);
        assertFalse(result);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntWithInvalidInput() {
        String input = "1a3";
        NumberInput.parseAsInt(input, 0);
    }

    @Test
    public void testParseAsIntWithNegativeInput() {
        String input = "-123";
        int expected = -123;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = "";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);

        input = null;
        expected = 0;
        actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "12345678901234567890";
        long expected = 12345678901234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithInvalidInput() {
        String input = "1a3";
        NumberInput.parseAsLong(input, 0);
    }

    @Test
    public void testParseAsLongWithNegativeInput() {
        String input = "-12345678901234567890";
        long expected = -12345678901234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = "";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);

        input = null;
        expected = 0;
        actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "1.23";
        double expected = 1.23;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.00001); // allow small error due to floating point imprecision
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidInput() {
        String input = "1a3";
        NumberInput.parseAsDouble(input, 0);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "";
        double expected = 0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0); // allow small error due to floating point imprecision

        input = null;
        expected = 0;
        actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0); // allow small error due to floating point imprecision
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithValidLongInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal expected = new BigDecimal(new Long(NumberInput.parseLong(digitChars, 0, 9)));
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 0, 9);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String input = "1a3";
        NumberInput.parseBigDecimal(input);
    }

    @Test
    public void testParseBigDecimalWithDefaultValue() {
        String input = "";
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);

        input = null;
        expected = BigDecimal.ZERO;
        actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }
}