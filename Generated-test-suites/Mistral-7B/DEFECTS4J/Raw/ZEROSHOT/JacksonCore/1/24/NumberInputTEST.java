package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray_withinBounds() {
        char[] input = new char[]{'1', '2', '3'};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntFromCharArray_outOfBounds() {
        char[] input = new char[]{'1', '2', '3', '4', '5'};
        int offset = 3;
        int expectedLen = 3;
        int result = NumberInput.parseInt(input, offset, expectedLen);
        assertEquals(216, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntFromCharArray_withNegativeLength() {
        char[] input = new char[]{'1', '2', '3'};
        int offset = 0;
        int len = -1;
        NumberInput.parseInt(input, offset, len);
    }

    @Test
    public void testParseIntFromString_withinBounds() {
        String input = "123";
        int result = NumberInput.parseInt(input);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntFromString_withinBounds_negativeNumber() {
        String input = "-123";
        int result = NumberInput.parseInt(input);
        assertEquals(-123, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromString_outOfBounds() {
        String input = "1234567890123456789";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseLongFromCharArray_withinBounds() {
        char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long result = NumberInput.parseLong(input, 0, input.length - 9);
        long expected = 123456789000L;
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongFromCharArray_outOfBounds() {
        char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result = NumberInput.parseLong(input, 0, input.length);
        long expected = 1234567890123456789L;
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLongFromCharArray_withNegativeLength() {
        char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        NumberInput.parseLong(input, input.length, -1);
    }

    @Test
    public void testParseLongFromString_withinBounds() {
        String input = "123456789000";
        long result = NumberInput.parseLong(input);
        long expected = 123456789000L;
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromString_outOfBounds() {
        String input = "1234567890123456789";
        NumberInput.parseLong(input);
    }

    @Test
    public void testInLongRange_charArray_positiveNumber() {
        char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_charArray_negativeNumber() {
        char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, true);
        assertFalse(result);
    }

    @Test
    public void testInLongRange_String_positiveNumber() {
        String input = "123456789000";
        boolean result = NumberInput.inLongRange(input, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_String_negativeNumber() {
        String input = "123456789000";
        boolean result = NumberInput.inLongRange(input, true);
        assertFalse(result);
    }

    @Test
    public void testParseAsInt_String_withinBounds() {
        String input = "123";
        int result = NumberInput.parseAsInt(input, 45);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsInt_String_outOfBounds() {
        String input = "1234567890123";
        int result = NumberInput.parseAsInt(input, 123);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsLong_String_withinBounds() {
        String input = "1234567890123";
        long result = NumberInput.parseAsLong(input, 1234567890123L);
        assertEquals(1234567890123L, result);
    }

    @Test
    public void testParseAsLong_String_outOfBounds() {
        String input = "12345678901234567890123";
        long result = NumberInput.parseAsLong(input, 1234567890123456789L);
        assertEquals(1234567890123456789L, result);
    }

    @Test
    public void testParseAsDouble_String_withinBounds() {
        String input = "123.45";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    public void testParseAsDouble_String_outOfBounds() {
        String input = "123.456.789";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testParseBigDecimal_String() {
        String input = "123.45";
        BigDecimal result = NumberInput.parseBigDecimal(input);
        BigDecimal expected = new BigDecimal("123.45");
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_CharArray() {
        char[] input = new char[]{'1', '2', '.', '3', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, input.length);
        BigDecimal expected = new BigDecimal("1.2345");
        assertEquals(expected, result);
    }

}