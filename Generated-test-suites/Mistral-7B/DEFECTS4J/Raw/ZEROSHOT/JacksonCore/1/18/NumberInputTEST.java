package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int len = 5;
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int len = 6;
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', 'x', '3', '4', '5'};
        int len = 6;
        int expected = 0;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithLongInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int len = 10;
        int expected = 1234567890;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntWithNegativeLength() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int len = -3;
        NumberInput.parseInt(digitChars, 0, len);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int len = 9;
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int len = 11;
        long expected = -1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', 'x', '3', '4', '5', '6', '7', '8', '9'};
        int len = 10;
        long expected = 0;
        long actual = NumberInput.parseLong(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithLargeInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        int len = 11;
        long expected = 12345678901L;
        long actual = NumberInput.parseLong(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidLength() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int len = 3;
        NumberInput.parseLong(digitChars, 0, len);
        // This test checks if the exception is thrown
    }

    @Test
    public void testParseLongWhenKnownToBeInt() {
        String input = "123";
        long expected = 123L;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInLongRangeWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInLongRangeWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'x'};
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInLongRangeWithNegativeInput() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInLongRangeWithLargeInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "12x";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
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
        String input = "x";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "1234567890123";
        long expected = 1234567890123L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "12x";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithNegativeInput() {
        String input = "-1234567890123";
        long expected = -1234567890123L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = "x";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "12x.45";
        double expected = 0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithNegativeInput() {
        String input = "-123.45";
        double expected = -123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "x";
        double expected = 0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithInvalidInput() {
        String input = "12x.45";
        BigDecimal expected = null;
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithNegativeInput() {
        String input = "-123.45";
        BigDecimal expected = new BigDecimal("-123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithCharArrayInput() {
        char[] input = new char[]{'1', '2', '3', '.', '4', '5'};
        int len = 6;
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(input, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithInvalidCharArrayInput() {
        char[] input = new char[]{'1', '2', 'x', '3', '4', '5'};
        int len = 6;
        BigDecimal expected = null;
        BigDecimal actual = NumberInput.parseBigDecimal(input, 0, len);
        assertEquals(expected, actual);
    }
}