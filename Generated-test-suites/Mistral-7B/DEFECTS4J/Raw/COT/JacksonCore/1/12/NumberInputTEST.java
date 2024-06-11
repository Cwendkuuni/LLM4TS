package com.example.numberinput;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] validIntDigitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(validIntDigitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithValidInputString() {
        String validIntString = "12345";
        int expected = 12345;
        int result = NumberInput.parseInt(validIntString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] validLongDigitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 12345678L;
        long result = NumberInput.parseLong(validLongDigitChars, 0, 8);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInputString() {
        String validLongString = "1234567890";
        long expected = 1234567890L;
        long result = NumberInput.parseLong(validLongString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongOutOfRangeNegative() {
        char[] longNegativeDigitChars = new char[]{MIN_LONG_STR_NO_SIGN.charAt(0),
                MIN_LONG_STR_NO_SIGN.charAt(1), MIN_LONG_STR_NO_SIGN.charAt(2),
                MIN_LONG_STR_NO_SIGN.charAt(3), MIN_LONG_STR_NO_SIGN.charAt(4),
                MIN_LONG_STR_NO_SIGN.charAt(5), MIN_LONG_STR_NO_SIGN.charAt(6),
                MIN_LONG_STR_NO_SIGN.charAt(7), MIN_LONG_STR_NO_SIGN.charAt(8),
                MIN_LONG_STR_NO_SIGN.charAt(9), MIN_LONG_STR_NO_SIGN.charAt(10)};
        boolean expected = true;
        boolean result = NumberInput.inLongRange(longNegativeDigitChars, false);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongOutOfRangePositive() {
        char[] longPositiveDigitChars = new char[]{MAX_LONG_STR.charAt(0),
                MAX_LONG_STR.charAt(1), MAX_LONG_STR.charAt(2),
                MAX_LONG_STR.charAt(3), MAX_LONG_STR.charAt(4),
                MAX_LONG_STR.charAt(5), MAX_LONG_STR.charAt(6),
                MAX_LONG_STR.charAt(7), MAX_LONG_STR.charAt(8),
                MAX_LONG_STR.charAt(9), MAX_LONG_STR.charAt(10),
                MAX_LONG_STR.charAt(11)};
        boolean expected = false;
        boolean result = NumberInput.inLongRange(longPositiveDigitChars, false);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String validIntString = "12345";
        int expected = 12345;
        int result = NumberInput.parseAsInt(validIntString, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String invalidIntString = "12345a";
        int expected = 0;
        int result = NumberInput.parseAsInt(invalidIntString, 0);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithValidInput() {
        String validLongString = "12345678901234567890";
        long expected = 12345678901234567890L;
        long result = NumberInput.parseAsLong(validLongString, 0L);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithInvalidInput() {
        String invalidLongString = "12345678901234567890a";
        long expected = 12345678901234567890L;
        long result = NumberInput.parseAsLong(invalidLongString, 0L);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String validDoubleString = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble(validDoubleString, 0.0);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidInput() {
        String invalidDoubleString = "123.45a";
        double expected = 0.0;
        double result = NumberInput.parseAsDouble(invalidDoubleString, 0.0);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        char[] invalidBigDecimalBuffer = new char[]{'2', '.', '3'};
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal result = NumberInput.parseBigDecimal(invalidBigDecimalBuffer);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String validBigDecimalString = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(validBigDecimalString);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInputBigInteger() {
        char[] invalidBigDecimalBuffer = new char[]{'1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal result = NumberInput.parseBigDecimal(invalidBigDecimalBuffer);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidInputBigInteger() {
        char[] validBigDecimalBuffer = new char[]{'1', '2', '3', '4', '5', '6', '7',
                '8', '9'};
        BigDecimal expected = new BigInteger("123456789").toBigDecimal();
        BigDecimal result = NumberInput.parseBigDecimal(validBigDecimalBuffer, 0, 10);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBigDecimalWithNegativeOffset() {
        char[] validBigDecimalBuffer = new char[]{'1', '2', '3', '4', '5', '6', '7',
                '8', '9'};
        NumberInput.parseBigDecimal(validBigDecimalBuffer, -1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBigDecimalWithNegativeLength() {
        char[] validBigDecimalBuffer = new char[]{'1', '2', '3', '4', '5', '6', '7',
                '8', '9'};
        NumberInput.parseBigDecimal(validBigDecimalBuffer, 0, -1);
    }
}