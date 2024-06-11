package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, result);

        digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        result = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(11111111111L, result);

        digitChars = new char[]{'1', '2', '3', 'a', '5'};
        int exceptionThrown = 0;
        try {
            NumberInput.parseInt(digitChars, 0, 5);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        digitChars = new char[]{'0'};
        result = NumberInput.parseInt(digitChars, 0, 1);
        assertEquals(0, result);

        digitChars = new char[]{'-'};
        exceptionThrown = 0;
        try {
            NumberInput.parseInt(digitChars, 0, 1);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        digitChars = new char[]{'-', '1'};
        result = NumberInput.parseInt(digitChars, 0, 2);
        assertEquals(-1, result);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals(123456789L, result);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        result = NumberInput.parseLong(digitChars, 0, 18);
        assertEquals(111111111111111111111111111111L, result);

        digitChars = new char[]{'1', '2', '3', 'a', '5'};
        int exceptionThrown = 0;
        try {
            NumberInput.parseLong(digitChars, 0, 5);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        digitChars = new char[]{'-'};
        exceptionThrown = 0;
        try {
            NumberInput.parseLong(digitChars, 0, 1);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        digitChars = new char[]{'-', '1'};
        result = NumberInput.parseLong(digitChars, 0, 2);
        assertEquals(-1L, result);
    }

    @Test
    public void testParseLongWithBigDecimal() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal result = NumberInput.parseBigDecimal(digitChars, 0, 9);
        assertEquals(new BigDecimal("123456789"), result);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        result = NumberInput.parseBigDecimal(digitChars, 0, 18);
        assertEquals(new BigDecimal("11111111111111111111111111111111111111"), result);

        digitChars = new char[]{'1', '2', '3', 'a', '5'};
        int exceptionThrown = 0;
        try {
            NumberInput.parseBigDecimal(digitChars, 0, 5);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        digitChars = new char[]{'-'};
        exceptionThrown = 0;
        try {
            NumberInput.parseBigDecimal(digitChars, 0, 1);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        digitChars = new char[]{'-', '1'};
        result = NumberInput.parseBigDecimal(digitChars, 0, 2);
        assertEquals(new BigDecimal("-1"), result);
    }

    @Test
    public void testParseLongWithString() {
        String numStr = "123456789";
        long result = NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
        assertEquals(123456789L, result);

        numStr = "123456789123456789123456789123456789123456789123456789123456789";
        result = NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
        assertEquals(11111111111111111111111111111111111111111111111111111111111111L, result);

        numStr = "123a56789";
        int exceptionThrown = 0;
        try {
            NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        numStr = "--1";
        exceptionThrown = 0;
        try {
            NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        numStr = "--12";
        result = NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
        assertEquals(-12L, result);

        numStr = "123--456";
        result = NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
        assertEquals(123L, result);
    }

    @Test
    public void testParseIntWithString() {
        String numStr = "12345";
        int result = NumberInput.parseAsInt(numStr, 0);
        assertEquals(12345, result);

        numStr = "--12345";
        result = NumberInput.parseAsInt(numStr, 0);
        assertEquals(-12345, result);

        numStr = "123a45";
        int exceptionThrown = 0;
        try {
            NumberInput.parseAsInt(numStr, 0);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        numStr = "123";
        result = NumberInput.parseAsInt(numStr, 1000);
        assertEquals(123, result);
    }

    @Test
    public void testParseLongWithString() {
        String numStr = "123456789";
        long result = NumberInput.parseAsLong(numStr, 0L);
        assertEquals(123456789L, result);

        numStr = "--123456789";
        result = NumberInput.parseAsLong(numStr, 0L);
        assertEquals(-123456789L, result);

        numStr = "123a456789";
        int exceptionThrown = 0;
        try {
            NumberInput.parseAsLong(numStr, 0L);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        numStr = "123";
        result = NumberInput.parseAsLong(numStr, 1000000L);
        assertEquals(123L, result);
    }

    @Test
    public void testParseDoubleWithString() {
        String numStr = "123.45";
        double result = NumberInput.parseAsDouble(numStr, 0.0);
        assertEquals(123.45, result, 0.001);

        numStr = "--123.45";
        result = NumberInput.parseAsDouble(numStr, 0.0);
        assertEquals(-123.45, result, 0.001);

        numStr = "123.a5";
        int exceptionThrown = 0;
        try {
            NumberInput.parseAsDouble(numStr, 0.0);
        } catch (NumberFormatException e) {
            exceptionThrown = 1;
        }
        assertTrue(exceptionThrown > 0);

        numStr = "123";
        result = NumberInput.parseAsDouble(numStr, 0.0);
        assertEquals(123.0, result, 0.001);
    }
}