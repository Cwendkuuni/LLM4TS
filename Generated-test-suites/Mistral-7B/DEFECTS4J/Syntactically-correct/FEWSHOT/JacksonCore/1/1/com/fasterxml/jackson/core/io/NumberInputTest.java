package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digitChars, 0, 3));

        char[] digitCharsNegative = new char[]{'-', '1', '2', '3'};
        assertEquals(-123, NumberInput.parseInt(digitCharsNegative, 0, 4));

        char[] digitCharsLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(123456789, NumberInput.parseInt(digitCharsLong, 0, 10));

        char[] digitCharsOutOfRange = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitCharsOutOfRange, 0, 11));
    }

    @Test
    public void testParseIntFromString() {
        assertEquals(123, NumberInput.parseInt("123"));
        assertEquals(-123, NumberInput.parseInt("-123"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt("1234567890"));
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2'};
        assertEquals(12, NumberInput.parseLong(digitChars, 0, 2));

        char[] digitCharsNegative = new char[]{'-', '1', '2'};
        assertEquals(-12, NumberInput.parseLong(digitCharsNegative, 0, 3));

        char[] digitCharsLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        assertEquals(12345678, NumberInput.parseLong(digitCharsLong, 0, 8));

        char[] digitCharsOutOfRange = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digitCharsOutOfRange, 0, 11));
    }

    @Test
    public void testParseLongFromString() {
        assertEquals(1234567890123L, NumberInput.parseLong("1234567890123"));
        assertEquals(-1234567890123L, NumberInput.parseLong("-1234567890123"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseLong("12345678901230"));
    }

    @Test
    public void testIsInLongRange() {
        char[] digitChars = new char[]{'1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 3, false));
        assertTrue(NumberInput.inLongRange(digitChars, 0, 3, true));

        char[] digitCharsNegative = new char[]{'-', '1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitCharsNegative, 0, 4, false));
        assertTrue(NumberInput.inLongRange(digitCharsNegative, 0, 4, true));

        char[] digitCharsLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertTrue(NumberInput.inLongRange(digitCharsLong, 0, 10, false));
        assertTrue(NumberInput.inLongRange(digitCharsLong, 0, 10, true));

        char[] digitCharsOutOfRange = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '1'};
        assertFalse(NumberInput.inLongRange(digitCharsOutOfRange, 0, 11, false));
        assertFalse(NumberInput.inLongRange(digitCharsOutOfRange, 0, 11, true));
    }

    @Test
    public void testIsInLongRangeWithString() {
        String numberStr = "123";
        assertTrue(NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), false));
        assertTrue(NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), true));

        String numberStrNegative = "-123";
        assertTrue(NumberInput.inLongRange(numberStrNegative.toCharArray(), 0, numberStrNegative.length(), false));
        assertTrue(NumberInput.inLongRange(numberStrNegative.toCharArray(), 0, numberStrNegative.length(), true));

        String numberStrLong = "1234567890123";
        assertTrue(NumberInput.inLongRange(numberStrLong.toCharArray(), 0, numberStrLong.length(), false));
        assertTrue(NumberInput.inLongRange(numberStrLong.toCharArray(), 0, numberStrLong.length(), true));

        String numberStrOutOfRange = "12345678901230";
        assertFalse(NumberInput.inLongRange(numberStrOutOfRange.toCharArray(), 0, numberStrOutOfRange.length(), false));
        assertFalse(NumberInput.inLongRange(numberStrOutOfRange.toCharArray(), 0, numberStrOutOfRange.length(), true));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(42, NumberInput.parseAsInt("42"));
        assertEquals(42, NumberInput.parseAsInt(" 42"));
        assertEquals(42, NumberInput.parseAsInt("42 "));
        assertEquals(42, NumberInput.parseAsInt("+42"));
        assertEquals(42, NumberInput.parseAsInt("+ 42"));
        assertEquals(42, NumberInput.parseAsInt("+ 42 "));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(0, NumberInput.parseAsInt(" "));
        assertEquals(0, NumberInput.parseAsInt("  "));
        assertEquals(0, NumberInput.parseAsInt("+"));
        assertEquals(0, NumberInput.parseAsInt("+-"));
        assertEquals(defaultValue, NumberInput.parseAsInt(null));
        assertEquals(defaultValue, NumberInput.parseAsInt(""));
        assertEquals(defaultValue, NumberInput.parseAsInt(" "));
        assertEquals(defaultValue, NumberInput.parseAsInt("+"));
        assertEquals(defaultValue, NumberInput.parseAsInt("+-"));
        assertEquals(defaultValue, NumberInput.parseAsInt("abc"));
        assertEquals(defaultValue, NumberInput.parseAsInt("123.45"));
        assertEquals(defaultValue, NumberInput.parseAsInt("123e4"));
        assertEquals(defaultValue, NumberInput.parseAsInt("0x123"));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(1234567890123L, NumberInput.parseAsLong("1234567890123"));
        assertEquals(1234567890123L, NumberInput.parseAsLong(" 1234567890123"));
        assertEquals(1234567890123L, NumberInput.parseAsLong(" 1234567890123 "));
        assertEquals(-1234567890123L, NumberInput.parseAsLong("-1234567890123"));
        assertEquals(-1234567890123L, NumberInput.parseAsLong(" -1234567890123"));
        assertEquals(-1234567890123L, NumberInput.parseAsLong(" -1234567890123 "));
        assertEquals(0L, NumberInput.parseAsLong(""));
        assertEquals(0L, NumberInput.parseAsLong(" "));
        assertEquals(0L, NumberInput.parseAsLong("  "));
        assertEquals(0L, NumberInput.parseAsLong("+"));
        assertEquals(0L, NumberInput.parseAsLong("+-"));
        assertEquals(defaultValue, NumberInput.parseAsLong(null));
        assertEquals(defaultValue, NumberInput.parseAsLong(""));
        assertEquals(defaultValue, NumberInput.parseAsLong(" "));
        assertEquals(defaultValue, NumberInput.parseAsLong("+"));
        assertEquals(defaultValue, NumberInput.parseAsLong("+-"));
        assertEquals(defaultValue, NumberInput.parseAsLong("abc"));
        assertEquals(defaultValue, NumberInput.parseAsLong("123.45"));
        assertEquals(defaultValue, NumberInput.parseAsLong("123e4"));
        assertEquals(defaultValue, NumberInput.parseAsLong("0x123"));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(3.141592653589793, NumberInput.parseAsDouble("3.141592653589793"), 0.0000001);
        assertEquals(3.141592653589793, NumberInput.parseAsDouble(" 3.141592653589793"), 0.0000001);
        assertEquals(3.141592653589793, NumberInput.parseAsDouble(" 3.141592653589793 "), 0.0000001);
        assertEquals(-3.141592653589793, NumberInput.parseAsDouble("-3.141592653589793"), 0.0000001);
        assertEquals(-3.141592653589793, NumberInput.parseAsDouble(" -3.141592653589793"), 0.0000001);
        assertEquals(-3.141492653589793, NumberInput.parseAsDouble(" -3.141492653589793"), 0.0000001);
        assertEquals(0.0, NumberInput.parseAsDouble(""));
        assertEquals(0.0, NumberInput.parseAsDouble(" "));
        assertEquals(0.0, NumberInput.parseAsDouble("  "));
        assertEquals(0.0, NumberInput.parseAsDouble("+"));
        assertEquals(0.0, NumberInput.parseAsDouble("+-"));
        assertEquals(defaultValue, NumberInput.parseAsDouble(null));
        assertEquals(defaultValue, NumberInput.parseAsDouble(""));
        assertEquals(defaultValue, NumberInput.parseAsDouble(" "));
        assertEquals(defaultValue, NumberInput.parseAsDouble("+"));
        assertEquals(defaultValue, NumberInput.parseAsDouble("+-"));
        assertEquals(defaultValue, NumberInput.parseAsDouble("abc"));
        assertEquals(defaultValue, NumberInput.parseAsDouble("123.45"));
        assertEquals(defaultValue, NumberInput.parseAsDouble("123e4"));
        assertEquals(defaultValue, NumberInput.parseAsDouble("0x123"));
    }

    @Test
    public void testParseBigDecimal() {
        BigDecimal expected = new BigDecimal("1234567890123.45");
        BigDecimal actual = NumberInput.parseBigDecimal("1234567890123.45");
        assertEquals(expected, actual);

        expected = new BigDecimal("1234567890123.45");
        actual = NumberInput.parseBigDecimal(" 1234567890123.45");
        assertEquals(expected, actual);

        expected = new BigDecimal("1234567890123.45");
        actual = NumberInput.parseBigDecimal(" 1234567890123.45 ");
        assertEquals(expected, actual);

        expected = new BigDecimal("-1234567890123.45");
        actual = NumberInput.parseBigDecimal("-1234567890123.45");
        assertEquals(expected, actual);

        expected = new BigDecimal("-1234567890123.45");
        actual = NumberInput.parseBigDecimal(" -1234567890123.45");
        assertEquals(expected, actual);

        expected = new BigDecimal("-1234567890123.45");
        actual = NumberInput.parseBigDecimal(" -1234567890123.45 ");
        assertEquals(expected, actual);

        expected = new BigDecimal("0");
        actual = NumberInput.parseBigDecimal("");
        assertEquals(expected, actual);

        expected = new BigDecimal("0");
        actual = NumberInput.parseBigDecimal(" ");
        assertEquals(expected, actual);

        expected = new BigDecimal("0");
        actual = NumberInput.parseBigDecimal("  ");
        assertEquals(expected, actual);

        expected = new BigDecimal("0");
        actual = NumberInput.parseBigDecimal("+");
        assertEquals(expected, actual);

        expected = new BigDecimal("0");
        actual = NumberInput.parseBigDecimal("+-");
        assertEquals(expected, actual);

        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(null));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(""));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(" "));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("+"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("+-"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("abc"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("123.45"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("123e4"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("0x123"));
    }
}