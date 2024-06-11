package com.example.junit; // replace with your test package if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] input = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithValidInputLongerThanExpected() {
        char[] input = {'1', '2', '3', '4', '5', '6'};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(12345, result); // expected value is the same as the parsed value
    }

    @Test
    public void testParseIntWithValidInputNegative() {
        char[] input = {'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithInvalidInput() {
        char[] input = {'a', 'b', 'c', 'd', 'e'};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(0, result); // expected value is the default value when parsing fails
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInputThrowsException() {
        char[] input = {'a', 'b', 'c', 'd', 'e'};
        NumberInput.parseInt(input, 0, input.length);
    }

    // Repeat the above test cases for parseLong(char[]) and parseInt(String) methods

    @Test
    public void testParseLongWithValidInput() {
        String input = "1234567890123";
        long expected = 1234567890123L;
        long result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInputLongerThanExpected() {
        String input = "123456789012345";
        long result = NumberInput.parseInt(input);
        assertEquals(123456789012345L, result); // expected value is the same as the parsed value
    }

    @Test
    public void testParseLongWithValidInputNegative() {
        String input = "-1234567890123";
        long expected = -1234567890123L;
        long result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithInvalidInput() {
        String input = "abcd";
        long result = NumberInput.parseInt(input);
        assertEquals(0L, result); // expected value is the default value when parsing fails
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInputThrowsException() {
        String input = "abcd";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseLongWithLargeInput() {
        String input = Long.MAX_VALUE + "";
        long expected = Long.MAX_VALUE;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithLargeInputNegative() {
        String input = Long.MIN_VALUE + "";
        long expected = Long.MIN_VALUE;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithLargeInputLongerThanExpected() {
        String input = Long.MAX_VALUE + "123";
        long result = NumberInput.parseLong(input);
        assertEquals(Long.parseLong(Long.MAX_VALUE + ""), result); // expected value is the same as the max long value
    }

    // Repeat the above test cases for parseLong(char[]) and parseLong(String) methods

    @Test
    public void testInLongRangeWithValidInput() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRangeWithInvalidInput() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, false);
        assertFalse(result);
    }

    @Test
    public void testInLongRangeWithInvalidInputNegative() {
        char[] input = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, true);
        assertTrue(result);
    }

    @Test
    public void testInLongRangeWithInvalidInputLongerThanExpected() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        boolean result = NumberInput.inLongRange(input, 0, input.length, false);
        assertFalse(result);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "abcd";
        int result = NumberInput.parseAsInt("abcd", 0);
        assertEquals(0, result); // expected value is the default value when parsing fails
    }

    @Test
    public void testParseAsIntWithInvalidInputThrowsException() {
        String input = "abcd";
        NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseAsInt(input, 0));
        assertNotNull(exception);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "1234567890123";
        long expected = 1234567890123L;
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "abcd";
        long result = NumberInput.parseAsLong("abcd", 0L);
        assertEquals(0L, result); // expected value is the default value when parsing fails
    }

    @Test
    public void testParseAsLongWithInvalidInputThrowsException() {
        String input = "abcd";
        NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseAsLong(input, 0L));
        assertNotNull(exception);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, result, 0.001); // allow small error due to floating-point representation
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "abcd";
        double result = NumberInput.parseAsDouble("abcd", 0);
        assertEquals(0.0, result, 0.001); // expected value is the default value when parsing fails
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidInputThrowsException() {
        String input = "abcd";
        NumberInput.parseAsDouble(input, 0);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithInvalidInput() {
        String input = "abcd";
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertNotNull(result); // expected value is null when parsing fails
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] input = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, input.length);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidCharArray() {
        char[] input = {'a', 'b', 'c', 'd', 'e'};
        NumberInput.parseBigDecimal(input, 0, input.length);
    }

}