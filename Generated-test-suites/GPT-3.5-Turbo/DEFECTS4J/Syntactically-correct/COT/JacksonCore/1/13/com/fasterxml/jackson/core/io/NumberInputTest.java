
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.math.BigDecimal;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int result = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(123, result);
    }
    
    @Test
    public void testParseIntWithNegative() {
        char[] digitChars = {'-', '1', '2', '3'};
        int result = NumberInput.parseInt(digitChars, 1, 3);
        assertEquals(-123, result);
    }

    @Test
    public void testParseIntWithString() {
        String str = "123";
        int result = NumberInput.parseInt(str);
        assertEquals(123, result);
    }
    
    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(1234567890, result);
    }
    
    @Test
    public void testParseLongWithString() {
        String str = "1234567890";
        long result = NumberInput.parseLong(str);
        assertEquals(1234567890, result);
    }
    
    @Test
    public void testInLongRangeWithPositiveNumber() {
        char[] digitChars = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        boolean result = NumberInput.inLongRange(digitChars, 0, 10, false);
        assertTrue(result);
    }
    
    @Test
    public void testInLongRangeWithNegativeNumber() {
        char[] digitChars = {'-', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean result = NumberInput.inLongRange(digitChars, 1, 10, true);
        assertTrue(result);
    }
    
    @Test
    public void testInLongRangeWithString() {
        String numberStr = "999999999";
        boolean negative = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertTrue(result);
    }
    
    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = 0;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(123, result);
    }
    
    @Test
    public void testParseAsLong() {
        String input = "1234567890";
        long defaultValue = 0L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(1234567890, result);
    }
    
    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = 0.0;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45, result, 0.001);
    }
    
    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double result = NumberInput.parseDouble(numStr);
        assertEquals(123.45, result, 0.001);
    }
    
    @Test
    public void testParseBigDecimalWithString() {
        String numStr = "123.45";
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123.45"), result);
    }
    
    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(new BigDecimal("123.45"), result);
    }
    
    @Test
    public void testParseBigDecimalWithPartialArray() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(new BigDecimal("123.45"), result);
    }
}

