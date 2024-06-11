package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt_CharArray() {
        char[] digitChars = "1234567890".toCharArray();
        assertEquals(1234567890, NumberInput.parseInt(digitChars, 0, digitChars.length));
    }
    
    @Test
    public void testParseInt_String() {
        assertEquals(1234567890, NumberInput.parseInt("1234567890"));
    }
    
    @Test
    public void testParseLong_CharArray() {
        char[] digitChars = "1234567890".toCharArray();
        assertEquals(1234567890L, NumberInput.parseLong(digitChars, 0, digitChars.length));
    }
    
    @Test
    public void testParseLong_String() {
        assertEquals(1234567890L, NumberInput.parseLong("1234567890"));
    }
    
    @Test
    public void testInLongRange_CharArray() {
        char[] digitChars = "1234567890".toCharArray();
        assertEquals(true, NumberInput.inLongRange(digitChars, 0, digitChars.length, false));
    }
    
    @Test
    public void testInLongRange_String() {
        assertEquals(true, NumberInput.inLongRange("1234567890", false));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(1234567890, NumberInput.parseAsInt("   1234567890  ", 0));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(1234567890L, NumberInput.parseAsLong("   1234567890  ", 0L));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(12.345, NumberInput.parseAsDouble("12.345", 0.0), 0.001);
    }
    
    @Test
    public void testParseBigDecimal_String() {
        assertEquals(new BigDecimal("12.345"), NumberInput.parseBigDecimal("12.345"));
    }
    
    @Test
    public void testParseBigDecimal_CharArray() {
        char[] buffer = "12.345".toCharArray();
        assertEquals(new BigDecimal("12.345"), NumberInput.parseBigDecimal(buffer));
    }
    
    @Test
    public void testParseBigDecimal_CharArrayWithOffsetAndLen() {
        char[] buffer = "123.4567890".toCharArray();
        assertEquals(new BigDecimal("3.4567890"), NumberInput.parseBigDecimal(buffer, 2, 9));
    }
}