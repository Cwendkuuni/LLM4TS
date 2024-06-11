package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromFixedLengthString() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 1; i <= 9; i++) {
            int expected = i;
            int actual = NumberInput.parseInt(digitChars, 0, i);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testParseIntFromVariableLengthString() {
        String str = "123456789";
        int actual = NumberInput.parseInt(str);
        int expected = 123456789;
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromStringWithNonIntegerChars() {
        String str = "1a2b3c4d5";
        NumberInput.parseInt(str);
    }

    @Test
    public void testParseLongFromFixedLengthString() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long expected = 1234567891;
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length - 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromVariableLengthString() {
        String str = "12345678910";
        long actual = NumberInput.parseLong(str);
        long expected = 12345678910;
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromStringWithNonLongChars() {
        String str = "1a2b3c4d5e";
        NumberInput.parseLong(str);
    }

    @Test
    public void testParseLongInRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertTrue(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '2'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertTrue(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '3'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertTrue(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '4'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertTrue(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '5'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertFalse(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '5'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertFalse(actual);
    }

    @Test
    public void testParseLongOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '10'};
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertFalse(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '11'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertFalse(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertFalse(actual);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '10'};
        actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertFalse(actual);
    }

    @Test
    public void testParseLongWithNegativeSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = -123456789;
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String str = "1234567890.12345";
        BigDecimal expected = new BigDecimal("1234567890.12345");
        BigDecimal actual = NumberInput.parseBigDecimal(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromFixedLengthCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
        int offset = 10;
        int length = 5;
        BigDecimal expected = new BigDecimal("12345");
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromVariableLengthCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
        BigDecimal expected = new BigDecimal("1234567890.12345");
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }
}