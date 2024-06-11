package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithValidInputFromString() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4'};
        long expected = 1234;
        long result = NumberInput.parseLong(digitChars, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInputFromString() {
        String input = "1234";
        long expected = 1234;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitChars = {'1', '2', '3', 'x'};
        NumberInput.parseInt(digitChars, 0, 3);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNegativeInput() {
        char[] digitChars = {'-', '1', '2', '3', '4'};
        NumberInput.parseInt(digitChars, 0, 4);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInputLongerThanIntMax() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '1'};
        NumberInput.parseInt(digitChars, 0, 11);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithNegativeInput() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5'};
        NumberInput.parseLong(digitChars, 0, 5);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInputLongerThanLongMax() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '0'};
        NumberInput.parseLong(digitChars, 0, 12);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = "123";
        int defaultValue = 456;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsIntWithInputContainingNonNumericCharacters() {
        String input = "123a";
        int defaultValue = 456;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = "1234567890123";
        long defaultValue = 456789012345L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(1234567890123L, result);
    }

    @Test
    public void testParseAsLongWithInputContainingNonNumericCharacters() {
        String input = "1234567890123a";
        long defaultValue = 456789012345L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "123.45";
        double defaultValue = 456.78;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    public void testParseAsDoubleWithInputContainingNonNumericCharacters() {
        String input = "123.45a";
        double defaultValue = 456.78;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(defaultValue, result, 0.001);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String input = "123456789012345678901234567890.123456";
        BigDecimal expected = new BigDecimal("123456789012345678901234567890.123456");
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidInputFromCharArray() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6'};
        BigDecimal expected = new BigDecimal(new String(input));
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String input = "123a";
        NumberInput.parseBigDecimal(input);
    }

    @Test
    public void testInLongRangeWithValidInput() {
        char[] digitChars = {'1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 2, false));
    }

    @Test
    public void testInLongRangeWithNegativeInput() {
        char[] digitChars = {'-', '1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 3, false));
    }

    @Test
    public void testInLongRangeWithValidInputGreaterThanMaxLong() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '0', '1'};
        assertFalse(NumberInput.inLongRange(digitChars, 0, 13, false));
    }

    @Test
    public void testInLongRangeWithValidInputLessThanMinLong() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '0', '1', '0'};
        assertFalse(NumberInput.inLongRange(digitChars, 0, 15, false));
    }

    @Test
    public void testInLongRangeWithValidInputNegative() {
        String numberStr = "-123456789012345678901234567890";
        assertTrue(NumberInput.inLongRange(numberStr, true));
    }

    @Test
    public void testParseDoubleWithValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble(input);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInput() {
        String input = "123a";
        NumberInput.parseAsDouble(input);
    }
}