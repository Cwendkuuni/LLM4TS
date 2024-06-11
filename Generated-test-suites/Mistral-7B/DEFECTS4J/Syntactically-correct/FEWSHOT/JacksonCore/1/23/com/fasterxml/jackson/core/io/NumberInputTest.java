package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithOffsetLength() {
        char[] digitChars = "12345".toCharArray();
        int offset = 0, len = 5;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(12345, num);
        offset += len;
        assertArrayEquals(new char[]{}, digitChars, 5, offset, len);
    }

    @Test
    public void testParseIntWithNegativeSign() {
        char[] digitChars = "-12345".toCharArray();
        int offset = 0, len = 6;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(-12345, num);
        offset += len;
        assertArrayEquals(new char[]{}, digitChars, 6, offset, len);
    }

    @Test
    public void testParseIntWithInvalidNumber() {
        char[] digitChars = "1a2b3c".toCharArray();
        int offset = 0, len = 5;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(Integer.MIN_VALUE, num);
        offset += len;
        assertArrayEquals(new char[]{'a', '2', 'b', '3', 'c'}, digitChars, 5, offset, len);
    }

    @Test
    public void testParseLongWithOffsetLength() {
        char[] digitChars = "1234567890123456789".toCharArray();
        int offset = 0, len = 20;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(1234567890123456789L, num);
        offset += len;
        assertArrayEquals(new char[]{}, digitChars, 20, offset, len);
    }

    @Test
    public void testParseLongWithNegativeSign() {
        char[] digitChars = "--1234567890123456789".toCharArray();
        int offset = 0, len = 21;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(-1234567890123456789L, num);
        offset += len;
        assertArrayEquals(new char[]{}, digitChars, 21, offset, len);
    }

    @Test
    public void testParseLongWithInvalidNumber() {
        char[] digitChars = "1a2b3c4d5e6f7g".toCharArray();
        int offset = 0, len = 12;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(Long.MIN_VALUE, num);
        offset += len;
        assertArrayEquals(new char[]{'a', '2', 'b', '3', 'c', '4', 'd', '5', 'e', '6', 'f', 'g'}, digitChars, 12, offset, len);
    }

    @Test
    public void testParseLongAsStringWithOffsetLength() {
        String numberStr = "1234567890123456789";
        int offset = 0, len = 20;
        boolean inLongRange = NumberInput.inLongRange(numberStr.toCharArray(), offset, len, false);
        assertTrue(inLongRange);
        offset += len;

        numberStr += "1";
        inLongRange = NumberInput.inLongRange(numberStr.toCharArray(), offset, 1, false);
        assertFalse(inLongRange);
    }

    @Test
    public void testParseLongAsStringWithNegativeSign() {
        String numberStr = "--1234567890123456789";
        int offset = 0, len = 21;
        boolean inLongRange = NumberInput.inLongRange(numberStr.toCharArray(), offset, len, false);
        assertTrue(inLongRange);
        offset += len;

        numberStr += "1";
        inLongRange = NumberInput.inLongRange(numberStr.toCharArray(), offset, 1, false);
        assertFalse(inLongRange);
    }

    @Test
    public void testParseIntFromString() {
        String input = "42";
        int num = NumberInput.parseAsInt(input, Integer.MIN_VALUE);
        assertEquals(42, num);

        input = "-42";
        num = NumberInput.parseAsInt(input, Integer.MIN_VALUE);
        assertEquals(-42, num);

        input = "42a";
        num = NumberInput.parseAsInt(input, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, num);

        input = "1234567890123456789";
        num = NumberInput.parseAsInt(input, Integer.MIN_VALUE);
        assertEquals(1234567890123456789, num);

        input = "4.5e6";
        num = NumberInput.parseAsInt(input, Integer.MIN_VALUE);
        assertEquals(4500000, num);
    }

    @Test
    public void testParseLongFromString() {
        String input = "1234567890123456789";
        long num = NumberInput.parseAsLong(input, Long.MIN_VALUE);
        assertEquals(1234567890123456789L, num);

        input = "-1234567890123456789";
        num = NumberInput.parseAsLong(input, Long.MIN_VALUE);
        assertEquals(-1234567890123456789L, num);

        input = "1234567890123456789abc";
        num = NumberInput.parseAsLong(input, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, num);

        input = "1234567890123456789e6";
        num = NumberInput.parseAsLong(input, Long.MIN_VALUE);
        assertEquals(1234567890123456789000000L, num);
    }

    @Test
    public void testParseDoubleFromString() {
        String input = "42.0";
        double num = NumberInput.parseAsDouble(input, Double.MIN_VALUE);
        assertEquals(42.0, num, 0.0);

        input = "-42.0";
        num = NumberInput.parseAsDouble(input, Double.MIN_VALUE);
        assertEquals(-42.0, num, 0.0);

        input = "42a.0";
        num = NumberInput.parseAsDouble(input, Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, num);

        input = "1234567890123456789.0";
        num = NumberInput.parseAsDouble(input, Double.MIN_VALUE);
        assertEquals(1234567890123456789.0, num, 0.0);

        input = "1234567890123456789e6";
        num = NumberInput.parseAsDouble(input, Double.MIN_VALUE);
        assertEquals(1.234567890123456789e11, num, 0.0);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String input = "1234567890123456789";
        BigDecimal num = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("1234567890123456789"), num);

        input = "-1234567890123456789";
        num = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("-1234567890123456789"), num);

        input = "1234567890123456789.0123456789";
        num = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("1234567890123456789.0123456789"), num);

        input = "1234567890123456789e6";
        num = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("1234567890123456789000000"), num);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] input = "1234567890123456789".toCharArray();
        BigDecimal num = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("1234567890123456789"), num);

        input = "--1234567890123456789".toCharArray();
        num = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("-1234567890123456789"), num);

        char[] inputWithDecimal = "1234567890123456789.".toCharArray();
        int decimalIndex = 11;
        num = NumberInput.parseBigDecimal(inputWithDecimal, 0, decimalIndex);
        assertEquals(new BigDecimal("1234567890123456789.0"), num);

        char[] inputWithExponent = "1234567890123456789e6".toCharArray();
        int exponentIndex = 16;
        num = NumberInput.parseBigDecimal(inputWithExponent, 0, exponentIndex);
        assertEquals(new BigDecimal("1234567890123456789000000"), num);
    }
}