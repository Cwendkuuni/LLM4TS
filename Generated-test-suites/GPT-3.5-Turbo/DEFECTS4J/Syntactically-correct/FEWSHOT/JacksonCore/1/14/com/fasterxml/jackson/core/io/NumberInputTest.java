package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(123456789, NumberInput.parseInt(digitChars, 0, 9));
        
        char[] digitChars2 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(-123456789, NumberInput.parseInt(digitChars2, 1, 9));
    }
    
    @Test
    public void testParseIntString() {
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
    }
    
    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(1234567890123456789L, NumberInput.parseLong(digitChars, 0, 19));
        
        char[] digitChars2 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(-1234567890123456789L, NumberInput.parseLong(digitChars2, 1, 19));
    }
    
    @Test
    public void testParseLongString() {
        assertEquals(1234567890123456789L, NumberInput.parseLong("1234567890123456789"));
        assertEquals(-1234567890123456789L, NumberInput.parseLong("-1234567890123456789"));
    }
    
    @Test
    public void testInLongRange() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 19, false));
        
        char[] digitChars2 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertTrue(NumberInput.inLongRange(digitChars2, 1, 19, true));
    }
    
    @Test
    public void testInLongRangeString() {
        assertTrue(NumberInput.inLongRange("1234567890123456789", false));
        assertTrue(NumberInput.inLongRange("-1234567890123456789", true));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(123456, NumberInput.parseAsInt("123456", 0));
        assertEquals(-123456, NumberInput.parseAsInt("-123456", 0));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(123456789012345L, NumberInput.parseAsLong("123456789012345", 0));
        assertEquals(-123456789012345L, NumberInput.parseAsLong("-123456789012345", 0));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(1234.56789, NumberInput.parseAsDouble("1234.56789", 0.0), 0.001);
    }
    
    @Test
    public void testParseDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0000001);
    }
    
    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("1234.56789"), NumberInput.parseBigDecimal("1234.56789"));
        char[] buffer = {'1', '2', '3', '4', '.', '5', '6', '7', '8', '9'};
        assertEquals(new BigDecimal("1234.56789"), NumberInput.parseBigDecimal(buffer));
        assertEquals(new BigDecimal("1234.56"), NumberInput.parseBigDecimal(buffer, 0, 7));
    }
}