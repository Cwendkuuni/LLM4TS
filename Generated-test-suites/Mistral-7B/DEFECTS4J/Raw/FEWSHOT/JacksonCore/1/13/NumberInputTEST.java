package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNegative;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithinIntRange() {
        char[] intDigits = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(intDigits, 0, intDigits.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithinIntRangeWithNegativeSign() {
        char[] intDigits = new char[]{'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int actual = NumberInput.parseInt(intDigits, 0, intDigits.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntOutOfIntRange() {
        char[] longDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int actual = NumberInput.parseInt(longDigits, 0, longDigits.length);
        assertEquals(Integer.MAX_VALUE, actual);

        char[] longDigitsWithMinus = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int actualNegative = NumberInput.parseInt(longDigitsWithMinus, 0, longDigitsWithMinus.length);
        assertEquals(Integer.MIN_VALUE, actualNegative);

        char[] longDigitsTooLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        int actualTooLong = NumberInput.parseInt(longDigitsTooLong, 0, longDigitsTooLong.length);
        assertEquals(Integer.MAX_VALUE, actualTooLong);
    }

    @Test
    public void testParseLongWithinLongRange() {
        char[] longDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 12345678L;
        long actual = NumberInput.parseLong(longDigits, 0, longDigits.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongOutOfLongRange() {
        char[] longDigitsTooLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        long actual = NumberInput.parseLong(longDigitsTooLong, 0, longDigitsTooLong.length);
        assertEquals(Long.MAX_VALUE, actual);

        char[] longDigitsNegative = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
        long actualNegative = NumberInput.parseLong(longDigitsNegative, 0, longDigitsNegative.length);
        assertEquals(Long.MIN_VALUE, actualNegative);
    }

    @Test
    public void testParseLongAsStringWithinLongRange() {
        String longString = "12345678";
        boolean expected = NumberInput.inLongRange(longString.toCharArray(), false);
        assertTrue(expected);

        String longNegativeString = "-12345678";
        boolean expectedNegative = NumberInput.inLongRange(longNegativeString.toCharArray(), true);
        assertTrue(expectedNegative);
    }

    @Test
    public void testParseLongAsStringOutOfLongRange() {
        String longStringTooLong = "12345678901234567890";
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(longStringTooLong.toCharArray(), false);
        assertFalse(actual);

        String longNegativeStringTooLong = "-12345678901234567890";
        boolean expectedNegative = false;
        boolean actualNegative = NumberInput.inLongRange(longNegativeStringTooLong.toCharArray(), true);
        assertFalse(actualNegative);
    }

    @Test
    public void testParseAsInt() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, Integer.MIN_VALUE);
        assertEquals(expected, actual);

        String inputEmpty = "";
        int actualEmpty = NumberInput.parseAsInt(inputEmpty, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, actualEmpty);

        String inputNegative = "-123";
        int expectedNegative = -123;
        int actualNegative = NumberInput.parseAsInt(inputNegative, Integer.MIN_VALUE);
        assertEquals(expectedNegative, actualNegative);

        String inputWithLetters = "123a";
        int expectedLetters = Integer.MIN_VALUE;
        int actualLetters = NumberInput.parseAsInt(inputWithLetters, Integer.MIN_VALUE);
        assertEquals(expectedLetters, actualLetters);
    }

    @Test
    public void testParseAsLong() {
        String input = "123456789012345";
        long expected = 123456789012345L;
        long actual = NumberInput.parseAsLong(input, Long.MIN_VALUE);
        assertEquals(expected, actual);

        String inputEmpty = "";
        long actualEmpty = NumberInput.parseAsLong(inputEmpty, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, actualEmpty);

        String inputNegative = "-123456789012345";
        long expectedNegative = -123456789012345L;
        long actualNegative = NumberInput.parseAsLong(inputNegative, Long.MIN_VALUE);
        assertEquals(expectedNegative, actualNegative);

        String inputWithLetters = "123a";
        long expectedLetters = Long.MIN_VALUE;
        long actualLetters = NumberInput.parseAsLong(inputWithLetters, Long.MIN_VALUE);
        assertEquals(expectedLetters, actualLetters);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, Double.NaN);
        assertEquals(expected, actual, 0.0001);

        String inputEmpty = "";
        double actualEmpty = NumberInput.parseAsDouble(inputEmpty, Double.NaN);
        assertEquals(Double.NaN, actualEmpty, 0.0001);

        String inputNegative = "-123.45";
        double expectedNegative = -123.45;
        double actualNegative = NumberInput.parseAsDouble(inputNegative, Double.NaN);
        assertEquals(expectedNegative, actualNegative, 0.0001);

        String inputWithLetters = "123a.45";
        double expectedLetters = Double.NaN;
        double actualLetters = NumberInput.parseAsDouble(inputWithLetters, Double.NaN);
        assertEquals(expectedLetters, actualLetters, 0.0001);
    }

    @Test
    public void testParseDouble() {
        String input = "123.45";
        double expected = 123.45;
        double actual;
        try {
            actual = NumberInput.parseDouble(input);
        } catch (NumberFormatException e) {
            actual = Double.NaN;
        }
        assertEquals(expected, actual, 0.0001);

        String inputEmpty = "";
        double actualEmpty = NumberInput.parseDouble(inputEmpty);
        assertEquals(Double.NaN, actualEmpty, 0.0001);

        String inputNegative = "-123.45";
        double expectedNegative = -123.45;
        double actualNegative;
        try {
            actualNegative = NumberInput.parseDouble(inputNegative);
        } catch (NumberFormatException e) {
            actualNegative = Double.NaN;
        }
        assertEquals(expectedNegative, actualNegative, 0.0001);

        String inputWithLetters = "123a.45";
        double expectedLetters = Double.NaN;
        double actualLetters;
        try {
            actualLetters = NumberInput.parseDouble(inputWithLetters);
        } catch (NumberFormatException e) {
            actualLetters = Double.NaN;
        }
        assertEquals(expectedLetters, actualLetters, 0.0001);

        String inputNaN = "NaN";
        double expectedNaN = Double.NaN;
        double actualNaN;
        try {
            actualNaN = NumberInput.parseDouble(inputNaN);
        } catch (NumberFormatException e) {
            actualNaN = Double.NaN;
        }
        assertEquals(expectedNaN, actualNaN, 0.0001);

        String inputInfinity = "Infinity";
        double expectedInfinity = Double.POSITIVE_INFINITY;
        double actualInfinity;
        try {
            actualInfinity = NumberInput.parseDouble(inputInfinity);
        } catch (NumberFormatException e) {
            actualInfinity = Double.NaN;
        }
        assertEquals(expectedInfinity, actualInfinity, 0.0001);
    }

    @Test
    public void testParseBigDecimal() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual;
        try {
            actual = NumberInput.parseBigDecimal(input);
        } catch (NumberFormatException e) {
            actual = BigDecimal.ZERO;
        }
        assertEquals(expected, actual);

        String inputEmpty = "";
        BigDecimal actualEmpty = NumberInput.parseBigDecimal(inputEmpty);
        assertNull(actualEmpty);

        String inputNegative = "-123.45";
        BigDecimal expectedNegative = new BigDecimal("-123.45");
        BigDecimal actualNegative;
        try {
            actualNegative = NumberInput.parseBigDecimal(inputNegative);
        } catch (NumberFormatException e) {
            actualNegative = BigDecimal.ZERO;
        }
        assertEquals(expectedNegative, actualNegative);

        String inputWithLetters = "123a.45";
        BigDecimal expectedLetters = BigDecimal.ZERO;
        BigDecimal actualLetters = NumberInput.parseBigDecimal(inputWithLetters);
        assertEquals(expectedLetters, actualLetters);

        char[] inputInt = new char[]{'1', '2', '3'};
        BigDecimal expectedInt = new BigDecimal("123");
        BigDecimal actualInt = NumberInput.parseBigDecimal(inputInt, 0, inputInt.length);
        assertEquals(expectedInt, actualInt);

        char[] inputLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        BigDecimal expectedLong = new BigDecimal("123456789012345");
        BigDecimal actualLong = NumberInput.parseBigDecimal(inputLong, 0, inputLong.length);
        assertEquals(expectedLong, actualLong);
    }
}