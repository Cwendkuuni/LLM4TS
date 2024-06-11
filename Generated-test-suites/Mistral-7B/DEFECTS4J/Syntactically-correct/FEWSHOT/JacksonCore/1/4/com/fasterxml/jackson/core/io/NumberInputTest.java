package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntFromCharArray_Negative() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int result = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntFromCharArray_LengthMismatch() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int expected = -1;
        int result = NumberInput.parseInt(digitChars, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromCharArray_Negative() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = -123456789;
        long result = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromCharArray_LengthMismatch() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        long expected = -1;
        long result = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntFromString() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntFromString_Negative() {
        String input = "-12345";
        int expected = -12345;
        int result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntFromString_LengthMismatch() {
        String input = "123";
        int expected = -1;
        int result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromString() {
        String input = "123456789";
        long expected = 123456789;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromString_Negative() {
        String input = "-123456789";
        long expected = -123456789;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromString_LengthMismatch() {
        String input = "1234567";
        long expected = -1;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInLongRangeFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        boolean expected = true;
        boolean result = NumberInput.inLongRange(digitChars, 0, 5, false);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInLongRangeFromCharArray_Negative() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        boolean expected = false;
        boolean result = NumberInput.inLongRange(digitChars, 0, 6, false);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInLongRangeFromCharArray_LengthMismatch() {
        char[] digitChars = new char[]{'1', '2', '3'};
        boolean expected = false;
        boolean result = NumberInput.inLongRange(digitChars, 0, 4, false);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInLongRangeFromString() {
        String input = "12345";
        boolean expected = true;
        boolean result = NumberInput.inLongRange(input, false);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInLongRangeFromString_Negative() {
        String input = "-12345";
        boolean expected = false;
        boolean result = NumberInput.inLongRange(input, false);
        assertEquals(expected, result);
    }

    @Test
    public void testIsInLongRangeFromString_LengthMismatch() {
        String input = "123";
        boolean expected = false;
        boolean result = NumberInput.inLongRange(input, false);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntFromString() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseAsInt("12345", -1);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntFromString_DefaultValue() {
        String input = "123";
        int expected = 123;
        int result = NumberInput.parseAsInt("123", 45);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntFromString_InvalidInput() {
        String input = "abcd";
        int expected = 45;
        int result = NumberInput.parseAsInt("abcd", 45);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLongFromString() {
        String input = "12345678901234567890";
        long expected = 12345678901234567890L;
        long result = NumberInput.parseAsLong("12345678901234567890", -1);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLongFromString_DefaultValue() {
        String input = "12345678901234567890";
        long expected = 12345678901234567890L;
        long result = NumberInput.parseAsLong("12345678901234567890", 9876543210L);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLongFromString_InvalidInput() {
        String input = "abcd";
        long expected = 9876543210L;
        long result = NumberInput.parseAsLong("abcd", 9876543210L);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsDoubleFromString() {
        String input = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble("123.45", -1);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testParseAsDoubleFromString_DefaultValue() {
        String input = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble("123.45", 9876543210);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testParseAsDoubleFromString_InvalidInput() {
        String input = "abcd";
        double expected = 9876543210;
        double result = NumberInput.parseAsDouble("abcd", 9876543210);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] input = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalFromCharArray_InvalidInput() {
        char[] input = new char[]{'a', 'b', 'c'};
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, 3);
        assertEquals(expected, result);
    }
}