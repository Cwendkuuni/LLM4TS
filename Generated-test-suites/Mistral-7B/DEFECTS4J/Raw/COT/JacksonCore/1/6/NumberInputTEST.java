package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    

    @Test
    public void testParseInt_SmallPositiveNumber() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInt_SmallNegativeNumber() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int result = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInt_LargePositiveNumber() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        int result = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_InvalidInput() {
        char[] digitChars = {'1', '2', 'A', '3', 'B'};
        NumberInput.parseInt(digitChars, 0, 5);
        fail("Expected NumberFormatException");
    }

    @Test
    public void testParseLong_SmallPositiveNumber() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        long expected = 12345;
        long result = NumberInput.parseLong(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_SmallNegativeNumber() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5'};
        long expected = -12345;
        long result = NumberInput.parseLong(digitChars, 0, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_LargePositiveNumber() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9'};
        long expected = 123456789999999999L;
        long result = NumberInput.parseLong(digitChars, 0, 20);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidInput() {
        char[] digitChars = {'1', '2', 'A', '3', 'B'};
        NumberInput.parseLong(digitChars, 0, 5);
        fail("Expected NumberFormatException");
    }

    @Test
    public void testParseLong_StringInput_ValidLong() {
        String str = "123456789999999999";
        long expected = 123456789999999999L;
        long result = NumberInput.parseLong(str);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_StringInput_InvalidLong() {
        String str = "123456789999999999A";
        NumberInput.parseLong(str);
        fail("Expected NumberFormatException");
    }

    @Test
    public void testInLongRange_ValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6'};
        boolean expected = true;
        boolean result = NumberInput.inLongRange(digitChars, 0, 6, false);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_InvalidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7'};
        boolean expected = false;
        boolean result = NumberInput.inLongRange(digitChars, 0, 7, false);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsInt_ValidInput() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsInt_InvalidInput() {
        String input = "123.45";
        int expected = 0;
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLong_ValidInput() {
        String input = "123456789999999999";
        long expected = 123456789999999999L;
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLong_InvalidInput() {
        String input = "123.45";
        long expected = 0;
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsDouble_ValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDouble_InvalidInput() {
        String input = "123A.45";
        NumberInput.parseAsDouble(input, 0);
        fail("Expected NumberFormatException");
    }

    @Test
    public void testParseBigDecimal_ValidInput() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_InvalidInput() {
        String input = "123A.45";
        NumberInput.parseBigDecimal(input);
        fail("Expected NumberFormatException");
    }

    @Test
    public void testParseBigDecimal_BinaryInput() {
        char[] input = {'0', '1', '0', '1', '0', '1', '1', '1', '0', '1', '1', '0'};
        BigDecimal expected = new BigDecimal("1011111011011", 2);
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, 13);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_InvalidBinaryInput() {
        char[] input = {'0', '1', '0', '1', '0', '1', '1', '1', '0', '1', '1', 'A'};
        NumberInput.parseBigDecimal(input, 0, 13);
        fail("Expected NumberFormatException");
    }

    
}