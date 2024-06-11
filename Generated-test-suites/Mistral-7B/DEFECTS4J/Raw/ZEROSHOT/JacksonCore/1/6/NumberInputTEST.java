package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArrayWithinIntRange() {
        char[] digitChars = new char[] {'1', '2', '3', '4'};
        int expectedResult = 1234;
        int result = NumberInput.parseInt(digitChars, 0, 4);
        assertEquals("Expected 1234, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseIntFromCharArrayOutsideIntRange() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
        int expectedResult = -1;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals("Expected -1, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseIntFromStringWithinIntRange() {
        String input = "1234";
        int expectedResult = 1234;
        int result = NumberInput.parseInt(input);
        assertEquals("Expected 1234, but got " + result, expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromStringOutsideIntRange() {
        String input = "12345";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseLongFromCharArrayWithinLongRange() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8'};
        long expectedResult = 12345678L;
        long result = NumberInput.parseLong(digitChars, 0, 8);
        assertEquals("Expected 12345678, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseLongFromCharArrayOutsideLongRange() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expectedResult = -1;
        long result = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals("Expected -1, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseLongFromStringWithinLongRange() {
        String input = "12345678901234567890";
        long expectedResult = 12345678901234567890L;
        long result = NumberInput.parseLong(input);
        assertEquals("Expected 12345678901234567890, but got " + result, expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromStringOutsideLongRange() {
        String input = "12345678901234567891";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseLongWithinLongRangeUsingParseAsLong() {
        String input = "12345678901234567890";
        long expectedResult = 12345678901234567890L;
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals("Expected 12345678901234567890, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseLongOutsideLongRangeUsingParseAsLong() {
        String input = "12345678901234567891";
        long expectedResult = -1;
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals("Expected -1, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseLongWithinLongRangeUsingParseInt() {
        String input = "12345678901234567890";
        long expectedResult = 12345678901234567890L;
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals("Expected 12345678901234567890, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseLongOutsideLongRangeUsingParseInt() {
        String input = "12345678901234567891";
        long expectedResult = -1;
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals("Expected -1, but got " + result, expectedResult, result);
    }

    @Test
    public void testParseLongWithinLongRangeUsingParseDouble() {
        String input = "12345678901234567890";
        long expectedResult = 12345678901234567890L;
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals("Expected 12345678901234567890, but got " + result, expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongOutsideLongRangeUsingParseDouble() {
        String input = "12345678901234567891";
        NumberInput.parseAsLong(input, 0L);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String input = "12345678901234567890.123456";
        BigDecimal expectedResult = new BigDecimal("12345678901234567890.123456");
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals("Expected " + expectedResult + ", but got " + result, expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalFromStringInvalidFormat() {
        String input = "12345678901234567890a";
        NumberInput.parseBigDecimal(input);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] input = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
        int offset = 0;
        int length = input.length;
        BigDecimal expectedResult = new BigDecimal(new BigInteger("12345678901234567890"), 10);
        BigDecimal result = NumberInput.parseBigDecimal(input, offset, length);
        assertEquals("Expected " + expectedResult + ", but got " + result, expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalFromCharArrayInvalidFormat() {
        char[] input = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a'};
        int offset = 0;
        int length = input.length;
        NumberInput.parseBigDecimal(input, offset, length);
    }
}