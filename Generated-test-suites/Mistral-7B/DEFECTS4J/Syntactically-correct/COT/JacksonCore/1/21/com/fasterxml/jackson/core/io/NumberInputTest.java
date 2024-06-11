package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);

        String str = "12345";
        expected = 12345;
        result = NumberInput.parseInt(str);
        assertEquals(expected, result);

        str = "-12345";
        expected = -12345;
        result = NumberInput.parseInt(str);
        assertEquals(expected, result);

        str = "123A45";
        int exceptionCaught = 0;
        try {
            result = NumberInput.parseInt(str);
        } catch (NumberFormatException e) {
            exceptionCaught = 1;
        }
        assertTrue(exceptionCaught > 0);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 1234567890L;
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, result);

        String str = "1234567890";
        expected = 1234567890L;
        result = NumberInput.parseLong(str);
        assertEquals(expected, result);

        str = "123A4567890";
        int exceptionCaught = 0;
        try {
            result = NumberInput.parseLong(str);
        } catch (NumberFormatException e) {
            exceptionCaught = 1;
        }
        assertTrue(exceptionCaught > 0);

        str = "-1234567890";
        expected = -1234567890L;
        result = NumberInput.parseLong(str);
        assertEquals(expected, result);

        str = "12345678901";
        exceptionCaught = 0;
        try {
            result = NumberInput.parseLong(str);
        } catch (NumberFormatException e) {
            exceptionCaught = 1;
        }
        assertTrue(exceptionCaught > 0);
    }

    @Test
    public void testParseLongWithInLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean negative = false;
        boolean expectedResult = true;
        boolean result = NumberInput.inLongRange(digitChars, 0, 10, negative);
        assertEquals(expectedResult, result);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        expectedResult = false;
        result = NumberInput.inLongRange(digitChars, 0, 11, negative);
        assertEquals(expectedResult, result);

        digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        negative = true;
        expectedResult = true;
        result = NumberInput.inLongRange(digitChars, 0, 10, negative);
        assertEquals(expectedResult, result);

        digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        negative = true;
        expectedResult = false;
        result = NumberInput.inLongRange(digitChars, 0, 11, negative);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseLongWithStringArgument() {
        String numberStr = "1234567890";
        boolean negative = false;
        boolean expectedResult = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, result);

        numberStr = "12345678901";
        negative = false;
        expectedResult = false;
        result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, result);

        numberStr = "-1234567890";
        negative = true;
        expectedResult = true;
        result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, result);

        numberStr = "-12345678901";
        negative = true;
        expectedResult = false;
        result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseAsInt() {
        String input = "12345";
        int defaultValue = 0;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(12345, result);

        input = "1A2B3C";
        defaultValue = 0;
        result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(0, result);

        input = "-12345";
        defaultValue = 0;
        result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(-12345, result);

        input = "";
        defaultValue = 0;
        result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(0, result);

        input = null;
        defaultValue = 0;
        result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "1234567890";
        long defaultValue = 0L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(1234567890L, result);

        input = "1A2B3C4D5E";
        defaultValue = 0L;
        result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(0L, result);

        input = "-1234567890";
        defaultValue = 0L;
        result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(-1234567890L, result);

        input = "";
        defaultValue = 0L;
        result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(0L, result);

        input = null;
        defaultValue = 0L;
        result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(0L, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = 0.0;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45, result, 0.001);

        input = "123A45.67";
        defaultValue = 0.0;
        result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(0.0, result, 0.001);

        input = "-123.45";
        defaultValue = 0.0;
        result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(-123.45, result, 0.001);

        input = "";
        defaultValue = 0.0;
        result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(0.0, result, 0.001);

        input = null;
        defaultValue = 0.0;
        result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(0.0, result, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithException() {
        String input = "123A45.67";
        NumberInput.parseDouble(input);
    }

    @Test
    public void testParseBigDecimal() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);

        numStr = "12345678901234567890";
        expected = new BigInteger("12345678901234567890").toBigDecimal();
        result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);

        numStr = "-123.45";
        expected = new BigDecimal("-123.45");
        result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);

        numStr = "";
        expected = BigDecimal.ZERO;
        result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);

        numStr = null;
        expected = BigDecimal.ZERO;
        result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(expected, result);

        buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        expected = new BigInteger("12345678901234567890").toBigDecimal();
        result = NumberInput.parseBigDecimal(buffer, 0, 21);
        assertEquals(expected, result);

        buffer = new char[]{'-', '1', '2', '3', '4', '5'};
        expected = new BigDecimal("-123.45");
        result = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(expected, result);

        buffer = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        expected = new BigInteger("-1234567890").toBigDecimal();
        result = NumberInput.parseBigDecimal(buffer, 0, 11);
        assertEquals(expected, result);

        buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        expected = new BigInteger("1234567890").toBigDecimal();
        result = NumberInput.parseBigDecimal(buffer, 0, 10);
        assertEquals(expected, result);

        buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        expected = new BigDecimal("12345678901");
        result = NumberInput.parseBigDecimal(buffer, 0, 11);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithCharArrayLeadingZero() {
        char[] buffer = new char[]{'0', '1', '2', '3', '4', '5'};
        BigDecimal expected = new BigDecimal("0.012345");
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 1, 6);
        assertEquals(expected, result);
    }
}