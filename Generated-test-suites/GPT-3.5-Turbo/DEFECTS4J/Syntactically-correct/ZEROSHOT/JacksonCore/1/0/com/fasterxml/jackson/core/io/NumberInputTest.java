package com.fasterxml.jackson.core.io;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    private char[] digitChars;
    private String str;
    private String numberStr;
    private boolean negative;

    @Before
    public void setUp() {
        digitChars = new char[]{'1', '2', '3', '4', '5'};
        str = "12345";
        numberStr = "12345";
        negative = false;
    }

    @Test
    public void testParseInt_charArray() {
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInt_String() {
        int expected = 12345;
        int result = NumberInput.parseInt(str);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_charArray() {
        long expected = 12345L;
        long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_String() {
        long expected = 12345L;
        long result = NumberInput.parseLong(str);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_charArray() {
        boolean expected = true;
        boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length, negative);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_String() {
        boolean expected = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsInt() {
        int expected = 12345;
        int result = NumberInput.parseAsInt(str, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLong() {
        long expected = 12345L;
        long result = NumberInput.parseAsLong(str, 0L);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsDouble() {
        double expected = 12345.0;
        double result = NumberInput.parseAsDouble(str, 0.0);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testParseDouble() {
        double expected = 12345.0;
        double result = NumberInput.parseDouble(str);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testParseBigDecimal_String() {
        BigDecimal expected = new BigDecimal("12345");
        BigDecimal result = NumberInput.parseBigDecimal(numberStr);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        BigDecimal expected = new BigDecimal("12345");
        BigDecimal result = NumberInput.parseBigDecimal(digitChars);
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseBigDecimal_charArray_offset_len() {
        int offset = 0;
        int len = digitChars.length;
        
        BigDecimal expected = new BigDecimal("12345");
        BigDecimal result = NumberInput.parseBigDecimal(digitChars, offset, len);
        assertEquals(expected, result);
    }
}