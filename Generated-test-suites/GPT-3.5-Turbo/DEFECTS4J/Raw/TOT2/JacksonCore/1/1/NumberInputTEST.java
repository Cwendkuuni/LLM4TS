package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digits = {'1', '2', '3'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntWithEmptyString() {
        int result = NumberInput.parseInt("");
        assertEquals(0, result);
    }

    @Test
    public void testParseLong() {
        char[] digits = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        long result = NumberInput.parseLong(digits, 0, 10);
        assertEquals(10000000000L, result);
    }

    @Test
    public void testParseLongWithEmptyString() {
        long result = NumberInput.parseLong("");
        assertEquals(0L, result);
    }

    @Test
    public void testInLongRange() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6'};
        boolean result = NumberInput.inLongRange(digits, 0, 10, true);
        assertTrue(result);
    }

    @Test
    public void testParseAsInt() {
        int result = NumberInput.parseAsInt("123", 0);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsLong() {
        long result = NumberInput.parseAsLong("10000000000", 0L);
        assertEquals(10000000000L, result);
    }

    @Test
    public void testParseAsDouble() {
        double result = NumberInput.parseAsDouble("3.14", 0.0);
        assertEquals(3.14, result, 0.0);
    }

    @Test
    public void testParseDouble() {
        double result = NumberInput.parseDouble("3.14");
        assertEquals(3.14, result, 0.0);
    }

    @Test
    public void testParseBigDecimal() {
        BigDecimal result = NumberInput.parseBigDecimal("123.456");
        assertEquals(new BigDecimal("123.456"), result);
    }

    @Test
    public void testParseBigDecimalWithCharBuffer() {
        char[] buffer = {'1', '2', '3', '4', '5', '6'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(new BigDecimal("123456"), result);
    }
}