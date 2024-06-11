package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = "123456789".toCharArray();
        int offset = 0;
        int len = 9;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(123456789, num);
    }

    @Test
    public void testParseInt_String() {
        String str = "123456789";
        int num = NumberInput.parseInt(str);
        assertEquals(123456789, num);
    }    

    @Test
    public void testParseLong() {
        char[] digitChars = "123456789123456789".toCharArray();
        int offset = 0;
        int len = 18;
        long val = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(123456789123456789L, val);
    }

    @Test
    public void testParseLong_String() {
        String str = "123456789123456789";
        long val = NumberInput.parseLong(str);
        assertEquals(123456789123456789L, val);
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digitChars = "123456789123456789".toCharArray();
        int offset = 0;
        int len = 18;
        boolean inRange = NumberInput.inLongRange(digitChars, offset, len, false);
        assertTrue(inRange);
    }

    @Test
    public void testInLongRange_String() {
        String numberStr = "123456789123456789";
        boolean inRange = NumberInput.inLongRange(numberStr, false);
        assertTrue(inRange);
    }

    @Test
    public void testParseAsInt() {
        String input = "12345";
        int defaultValue = -1;
        int parsedInt = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(12345, parsedInt);
    }

    @Test
    public void testParseAsLong() {
        String input = "123456789123456789";
        long defaultValue = -1L;
        long parsedLong = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(123456789123456789L, parsedLong);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = -1.0;
        double parsedDouble = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45, parsedDouble, 0.001);
    }
    
    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double parsedDouble = NumberInput.parseDouble(numStr);
        assertEquals(123.45, parsedDouble, 0.001);
    }

    @Test
    public void testParseBigDecimal_String() {
        String numStr = "123.45";
        BigDecimal parsedDecimal = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123.45"), parsedDecimal);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] buffer = "123.45".toCharArray();
        BigDecimal parsedDecimal = NumberInput.parseBigDecimal(buffer);
        assertEquals(new BigDecimal("123.45"), parsedDecimal);
    }

    @Test
    public void testParseBigDecimal_charArray_offset_len() {
        char[] buffer = "123.45".toCharArray();
        int offset = 0;
        int len = 7;
        BigDecimal parsedDecimal = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(new BigDecimal("123.45"), parsedDecimal);
    }

}