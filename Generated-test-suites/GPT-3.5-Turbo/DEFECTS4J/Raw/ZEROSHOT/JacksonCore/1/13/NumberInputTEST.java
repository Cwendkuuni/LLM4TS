package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        assertEquals(123, NumberInput.parseInt("123"));
        assertEquals(-456, NumberInput.parseInt("-456"));
        assertEquals(0, NumberInput.parseInt("0"));
    }
    
    @Test
    public void testParseInt_withOffsetAndLength() {
        assertEquals(789, NumberInput.parseInt("1234567890".toCharArray(), 3, 3));
        assertEquals(-987, NumberInput.parseInt("-1234567890".toCharArray(), 4, 3));
    }
    
    @Test
    public void testParseLong() {
        assertEquals(1234567890L, NumberInput.parseLong("1234567890"));
        assertEquals(-987654321L, NumberInput.parseLong("-987654321"));
        assertEquals(0L, NumberInput.parseLong("0"));
    }
    
    @Test
    public void testParseLong_withOffsetAndLength() {
        assertEquals(1234567890L, NumberInput.parseLong("012345678901234567890".toCharArray(), 1, 10));
        assertEquals(-987654321L, NumberInput.parseLong("-012345678901234567890".toCharArray(), 1, 10));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(42, NumberInput.parseAsInt("42", -1));
        assertEquals(-1, NumberInput.parseAsInt("not a number", -1));
        assertEquals(-1, NumberInput.parseAsInt("", -1));
        assertEquals(-1, NumberInput.parseAsInt(null, -1));
        assertEquals(0, NumberInput.parseAsInt("0", -1));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(1234567890L, NumberInput.parseAsLong("1234567890", -1L));
        assertEquals(-1L, NumberInput.parseAsLong("not a number", -1L));
        assertEquals(-1L, NumberInput.parseAsLong("", -1L));
        assertEquals(-1L, NumberInput.parseAsLong(null, -1L));
        assertEquals(0L, NumberInput.parseAsLong("0", -1L));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(3.14, NumberInput.parseAsDouble("3.14", -1.0), 0.01);
        assertEquals(-1.0, NumberInput.parseAsDouble("not a number", -1.0), 0.01);
        assertEquals(-1.0, NumberInput.parseAsDouble("", -1.0), 0.01);
        assertEquals(-1.0, NumberInput.parseAsDouble(null, -1.0), 0.01);
        assertEquals(0.0, NumberInput.parseAsDouble("0", -1.0), 0.01);
    }
    
    @Test
    public void testParseDouble() {
        assertEquals(3.14, NumberInput.parseDouble("3.14"), 0.01);
        assertEquals(-1.0, NumberInput.parseDouble("not a number"), 0.01);
        assertEquals(0.0, NumberInput.parseDouble("0"), 0.01);
    }
    
    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("3.14"), NumberInput.parseBigDecimal("3.14"));
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0"));
    }
    
    @Test
    public void testParseBigDecimal_withCharArray() {
        assertEquals(new BigDecimal("3.14"), NumberInput.parseBigDecimal("3.14".toCharArray()));
    }
    
    @Test
    public void testParseBigDecimal_withOffsetAndLength() {
        assertEquals(new BigDecimal("3.14"), NumberInput.parseBigDecimal("01234567893.14".toCharArray(), 10, 4));
    }
    
    @Test
    public void testInLongRange_withCharArray() {
        assertTrue(NumberInput.inLongRange("1234567890".toCharArray(), 0, 10, false));
        assertFalse(NumberInput.inLongRange("01234567890".toCharArray(), 1, 11, false));
    }
    
    @Test
    public void testInLongRange_withString() {
        assertTrue(NumberInput.inLongRange("1234567890", false));
        assertFalse(NumberInput.inLongRange("01234567890", false));
    }
    
    @Test
    public void testInLongRange_withNegativeString() {
        assertTrue(NumberInput.inLongRange("-1234567890", true));
        assertFalse(NumberInput.inLongRange("-01234567890", true));
    }
}