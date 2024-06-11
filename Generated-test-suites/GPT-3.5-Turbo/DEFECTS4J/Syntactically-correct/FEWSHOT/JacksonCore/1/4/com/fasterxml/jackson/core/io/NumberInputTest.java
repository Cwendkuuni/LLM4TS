package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberInputTest {
    @Test
    public void testParseIntCharArrayInt() {
        char[] digitChars = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digitChars, 0, 3));
        
        char[] digitChars2 = {'-', '1', '2', '3'};
        assertEquals(-123, NumberInput.parseInt(digitChars2, 0, 4));
    }

    @Test
    public void testParseIntString() {
        String str = "123";
        assertEquals(123, NumberInput.parseInt(str));
        
        String str2 = "-123";
        assertEquals(-123, NumberInput.parseInt(str2));
    }

    @Test
    public void testParseLongCharArrayInt() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(1234567890L, NumberInput.parseLong(digitChars, 0, 10));
        
        char[] digitChars2 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(-1234567890L, NumberInput.parseLong(digitChars2, 0, 11));
    }

    @Test
    public void testParseLongString() {
        String str = "1234567890";
        assertEquals(1234567890L, NumberInput.parseLong(str));
        
        String str2 = "-1234567890";
        assertEquals(-1234567890L, NumberInput.parseLong(str2));
    }

    @Test
    public void testInLongRangeCharArrayIntIntBoolean() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 10, false));
        
        char[] digitChars2 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertTrue(NumberInput.inLongRange(digitChars2, 0, 11, true));
    }

    @Test
    public void testInLongRangeStringBoolean() {
        String str = "1234567890";
        assertTrue(NumberInput.inLongRange(str, false));
        
        String str2 = "-1234567890";
        assertTrue(NumberInput.inLongRange(str2, true));
    }

    @Test
    public void testParseAsIntStringInt() {
        String input = "123";
        int defaultValue = -1;
        assertEquals(123, NumberInput.parseAsInt(input, defaultValue));
        
        String input2 = "-123";
        int defaultValue2 = -1;
        assertEquals(-123, NumberInput.parseAsInt(input2, defaultValue2));
    }

    @Test
    public void testParseAsLongStringLong() {
        String input = "1234567890";
        long defaultValue = -1L;
        assertEquals(1234567890L, NumberInput.parseAsLong(input, defaultValue));
        
        String input2 = "-1234567890";
        long defaultValue2 = -1L;
        assertEquals(-1234567890L, NumberInput.parseAsLong(input2, defaultValue2));
    }

    @Test
    public void testParseAsDoubleStringDouble() {
        String input = "12.34";
        double defaultValue = -1.0;
        assertEquals(12.34, NumberInput.parseAsDouble(input, defaultValue), 0.0001);
        
        String input2 = "-12.34";
        double defaultValue2 = -1.0;
        assertEquals(-12.34, NumberInput.parseAsDouble(input2, defaultValue2), 0.0001);
    }

    @Test
    public void testParseDoubleString() {
        String numStr = "12.34";
        assertEquals(12.34, NumberInput.parseDouble(numStr), 0.0001);
        
        String numStr2 = "2.2250738585072012e-308";
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(numStr2), 0.0001);
    }

    @Test
    public void testParseBigDecimalString() {
        String numStr = "12.34";
        assertEquals(new BigDecimal("12.34"), NumberInput.parseBigDecimal(numStr));
    }

    @Test
    public void testParseBigDecimalCharArray() {
        char[] buffer = {'1', '2', '3', '4'};
        assertEquals(new BigDecimal("1234"), NumberInput.parseBigDecimal(buffer));
    }

    @Test
    public void testParseBigDecimalCharArrayIntInt() {
        char[] buffer = {'1', '2', '3', '4'};
        assertEquals(new BigDecimal("12"), NumberInput.parseBigDecimal(buffer, 0, 2));
    }
}