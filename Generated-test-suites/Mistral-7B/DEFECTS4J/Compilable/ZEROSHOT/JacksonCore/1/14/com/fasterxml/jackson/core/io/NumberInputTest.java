package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInputs() {
        char[] input1 = {'1', '2', '3', '4', '5'};
        int expected1 = 12345;
        int result1 = NumberInput.parseInt(input1, 0, 5);
        assertEquals(expected1, result1);

        char[] input2 = {'-', '1', '2', '3', '4', '5'};
        int expected2 = -12345;
        int result2 = NumberInput.parseInt(input2, 0, 6);
        assertEquals(expected2, result2);

        String input3 = "12345";
        int expected3 = 12345;
        int result3 = NumberInput.parseInt(input3);
        assertEquals(expected3, result3);

        String input4 = "-12345";
        int expected4 = -12345;
        int result4 = NumberInput.parseInt(input4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testParseIntWithInvalidInputs() {
        char[] input1 = {'a', 'b', 'c'};
        int expected1 = 0;
        int result1 = NumberInput.parseInt(input1, 0, 3);
        assertEquals(expected1, result1);

        String input2 = "abc";
        int expected2 = 0;
        int result2 = NumberInput.parseInt(input2);
        assertEquals(expected2, result2);

        char[] input3 = {'1', '2', '3', 'a'};
        int expected3 = 0;
        int result3 = NumberInput.parseInt(input3, 0, 4);
        assertEquals(expected3, result3);

        String input4 = "123a";
        int expected4 = 0;
        int result4 = NumberInput.parseInt(input4);
        assertEquals(expected4, result4);

        char[] input5 = {'1', '2', '3', '4', '5', '6'};
        int expected5 = 0;
        int result5 = NumberInput.parseInt(input5, 0, 5);
        assertEquals(expected5, result5);
    }

    @Test
    public void testParseLongWithValidInputs() {
        char[] input1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected1 = 123456789L;
        long result1 = NumberInput.parseLong(input1, 0, 10);
        assertEquals(expected1, result1);

        String input2 = "1234567890";
        long expected2 = 1234567890L;
        long result2 = NumberInput.parseLong(input2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testParseLongWithInvalidInputs() {
        char[] input1 = {'1', '2', '3', '4', '5', '6', 'a'};
        long expected1 = 0;
        long result1 = NumberInput.parseLong(input1, 0, 7);
        assertEquals(expected1, result1);

        String input2 = "1234567890a";
        long expected2 = 0;
        long result2 = NumberInput.parseLong(input2);
        assertEquals(expected2, result2);

        char[] input3 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'};
        long expected3 = 0;
        long result3 = NumberInput.parseLong(input3, 0, 11);
        assertEquals(expected3, result3);

        String input4 = "12345678901";
        long expected4 = 0;
        long result4 = NumberInput.parseLong(input4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testParseLongAsNumberWithValidInputs() {
        String input1 = "12345";
        long expected1 = 12345;
        long result1 = NumberInput.parseLong(input1);
        assertEquals(expected1, result1);

        String input2 = "-12345";
        long expected2 = -12345;
        long result2 = NumberInput.parseLong(input2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testParseLongAsNumberWithInvalidInputs() {
        String input1 = "abcd";
        long expected1 = 0;
        long result1 = NumberInput.parseLong(input1);
        assertEquals(expected1, result1);

        String input2 = "123abcd";
        long expected2 = 0;
        long result2 = NumberInput.parseLong(input2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testParseLongInLongRange() {
        char[] input1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean expected1 = true;
        boolean result1 = NumberInput.inLongRange(input1, 0, 10, false);
        assertEquals(expected1, result1);

        String input2 = "1234567890";
        boolean expected2 = true;
        boolean result2 = NumberInput.inLongRange(input2, false);
        assertEquals(expected2, result2);

        char[] input3 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'};
        boolean expected3 = false;
        boolean result3 = NumberInput.inLongRange(input3, 0, 11, false);
        assertEquals(expected3, result3);

        String input4 = "12345678901";
        boolean expected4 = false;
        boolean result4 = NumberInput.inLongRange(input4, false);
        assertEquals(expected4, result4);
    }

    @Test
    public void testParseLongInLongRangeNegative() {
        char[] input1 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean expected1 = true;
        boolean result1 = NumberInput.inLongRange(input1, 0, 10, true);
        assertEquals(expected1, result1);

        String input2 = "-1234567890";
        boolean expected2 = true;
        boolean result2 = NumberInput.inLongRange(input2, true);
        assertEquals(expected2, result2);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidCharset() {
        char[] input = {'1', '2', '3', '4', '#'};
        NumberInput.parseInt(input, 0, 5);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidCharset() {
        char[] input = {'1', '2', '3', '4', '5', '6', '#'};
        NumberInput.parseLong(input, 0, 7);
    }

    @Test
    public void testParseBigDecimalWithValidInputs() {
        String input1 = "12345.67";
        BigDecimal expected1 = new BigDecimal("12345.67");
        BigDecimal result1 = NumberInput.parseBigDecimal(input1);
        assertEquals(expected1, result1);

        String input2 = "123456789012345.67";
        BigDecimal expected2 = new BigDecimal("123456789012345.67");
        BigDecimal result2 = NumberInput.parseBigDecimal(input2);
        assertEquals(expected2, result2);

        char[] input3 = {'1', '2', '3', '4', '5', '.', '6', '7'};
        BigDecimal expected3 = new BigDecimal("12345.67");
        BigDecimal result3 = NumberInput.parseBigDecimal(input3, 0, 8);
        assertEquals(expected3, result3);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInputs() {
        String input1 = "123a";
        NumberInput.parseBigDecimal(input1);
        fail("Expected NumberFormatException");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidCharset() {
        char[] input1 = {'1', '2', '3', '4', '5', '.', '6', '7', '#'};
        NumberInput.parseBigDecimal(input1, 0, 9);
        fail("Expected NumberFormatException");
    }
}