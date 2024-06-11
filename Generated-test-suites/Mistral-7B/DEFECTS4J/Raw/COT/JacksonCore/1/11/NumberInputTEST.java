package com.example; // replace with your package name

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithValidInputFromString() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseInt(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 1234567890L;
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInputFromString() {
        String input = "1234567890";
        long expected = 1234567890L;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithLongerInput() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long expected = 12345678901L;
        long result = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithNegativeNumber() {
        char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5'};
        long expected = -12345;
        long result = NumberInput.parseLong(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithNegativeNumberFromString() {
        String input = "-12345";
        long expected = -12345;
        long result = NumberInput.parseLong(input);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[] {'1', '2', '3', 'x', '5'};
        NumberInput.parseLong(digitChars, 0, 5);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInputFromString() {
        String input = "123x5";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseLongWithMinValue() {
        char[] digitChars = new char[] {NumberInput.NASTY_SMALL_DOUBLE.charAt(0),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(1),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(2),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(3),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(4),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(5),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(6),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(7),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(8),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(9),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(10),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(11),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(12),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(13),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(14),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(15),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(16),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(17),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(18),
                                          NumberInput.NASTY_SMALL_DOUBLE.charAt(19)};
        long expected = Double.longBitsToDouble(Double.longBitsToDouble(0x000000010000000000000000000000002p-1023L) & 0xFFFFFFFFFFFFL);
        long result = NumberInput.parseLong(digitChars, 0, 20);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithMaxValue() {
        char[] digitChars = new char[] {'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9'};
        long expected = Long.MAX_VALUE;
        long result = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithMaxValueFromString() {
        String input = Long.MAX_VALUE.toString();
        long expected = Long.MAX_VALUE;
        long result = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithLargeInput() {
        String input = "9223372036854775807";
        long expected = Long.MAX_VALUE;
        long result = NumberInput.parseLong(input.toCharArray(), 0, input.length());
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String input = "1234567890.123456";
        BigDecimal expected = new BigDecimal("1234567890.123456");
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidInputFromChars() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '1', '2', '3', '4', '5', '6'};
        BigDecimal expected = new BigDecimal("1234567890.123456");
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, input.length);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String input = "123x5";
        NumberInput.parseBigDecimal(input.toCharArray(), 0, input.length());
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInputFromString() {
        String input = "123x5";
        NumberInput.parseBigDecimal(input.toCharArray(), 0, input.length());
    }
}