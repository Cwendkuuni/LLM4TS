package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        for (int i = 0; i <= 10; i++) {
            int num = NumberInput.parseInt(digitChars, 0, i);
            assertEquals(i, num);
        }
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseInt(digitChars, 0, 11));
    }

    @Test
    public void testParseIntFromString() {
        String testCases = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 21 22 23 24 25 26 27 28 29 30";
        String[] testStrings = testCases.split(" ");
        for (String testString : testStrings) {
            int num = NumberInput.parseInt(testString);
            assertEquals(Integer.parseInt(testString), num);
        }
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt("abc"));
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long num = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(123456789L, num);
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseLong(digitChars, 0, 11));
    }

    @Test
    public void testParseLongFromString() {
        String testCases = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 123 1234 12345 123456 1234567 12345678 123456789 1234567890";
        String[] testStrings = testCases.split(" ");
        for (String testString : testStrings) {
            long num = NumberInput.parseLong(testString);
            assertEquals(Long.parseLong(testString), num);
        }
        assertThrows(NumberFormatException.class, () -> NumberInput.parseLong("abc"));
    }

    @Test
    public void testIsInLongRange() {
        assertTrue(NumberInput.inLongRange(new char[]{'1', '2', '3'}, false));
        assertTrue(NumberInput.inLongRange("123", false));
        assertFalse(NumberInput.inLongRange(new char[]{'1', '2', '3'}, true));
        assertFalse(NumberInput.inLongRange("12345678901234567890", true));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(42, NumberInput.parseAsInt("42"));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(42, NumberInput.parseAsInt("42 "));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(10, NumberInput.parseAsInt("10"));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(10, NumberInput.parseAsInt("10 "));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(42, NumberInput.parseAsInt("+42"));
        assertEquals(42, NumberInput.parseAsInt("42+"));
        assertEquals(42, NumberInput.parseAsInt("-42"));
        assertEquals(-42, NumberInput.parseAsInt("42-"));
        assertEquals(0, NumberInput.parseAsInt("-42"));
        assertEquals(1, NumberInput.parseAsInt("1.1"));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(1, NumberInput.parseAsInt("1.1 "));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(1234567890123456789L, NumberInput.parseAsLong("1234567890123456789"));
        assertEquals(0L, NumberInput.parseAsLong(""));
        assertEquals(1234567890123456789L, NumberInput.parseAsLong("1234567890123456789 "));
        assertEquals(0L, NumberInput.parseAsLong(""));
        assertEquals(123L, NumberInput.parseAsLong("123"));
        assertEquals(0L, NumberInput.parseAsLong(""));
        assertEquals(123L, NumberInput.parseAsLong("123 "));
        assertEquals(0L, NumberInput.parseAsLong(""));
        assertEquals(1234567890123456789L, NumberInput.parseAsLong("+1234567890123456789"));
        assertEquals(1234567890123456789L, NumberInput.parseAsLong("1234567890123456789+"));
        assertEquals(-1234567890123456789L, NumberInput.parseAsLong("1234567890123456789-"));
        assertEquals(0L, NumberInput.parseAsLong("1234567890123456789-"));
        assertEquals(0L, NumberInput.parseAsLong("abc"));
    }

    @Test
    public void testParseAsDouble() {
        assertNotNull(NumberInput.parseAsDouble("123.45"));
        assertNotNull(NumberInput.parseAsDouble("123.45e6"));
        assertNotNull(NumberInput.parseAsDouble("123.45e-6"));
        assertEquals(123.45, NumberInput.parseAsDouble("123.45"), 0.0001);
        assertEquals(123450000.0, NumberInput.parseAsDouble("123.45e6"), 0.0001);
        assertEquals(0.012345, NumberInput.parseAsDouble("1.23e-5"), 1e-9);
        assertEquals(Double.NaN, NumberInput.parseAsDouble("NaN"));
        assertEquals(Double.POSITIVE_INFINITY, NumberInput.parseAsDouble("Inf"));
        assertEquals(Double.NEGATIVE_INFINITY, NumberInput.parseAsDouble("-Inf"));
        assertNull(NumberInput.parseAsDouble(""));
        assertNull(NumberInput.parseAsDouble(" "));
        assertNull(NumberInput.parseAsDouble("abc"));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseAsDouble("123.a5"));
    }

    @Test
    public void testParseBigDecimal() {
        BigDecimal expected = new BigDecimal("1234567890123456789");
        BigDecimal result = NumberInput.parseBigDecimal("1234567890123456789");
        assertEquals(expected, result);
        assertNull(NumberInput.parseBigDecimal(""));
        assertNull(NumberInput.parseBigDecimal("abc"));
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal result = NumberInput.parseBigDecimal(digits, 0, 10);
        BigDecimal expected = new BigDecimal(new String(digits, 0, 10));
        assertEquals(expected, result);
        assertNull(NumberInput.parseBigDecimal(new char[0], 0, 0));
        assertNull(NumberInput.parseBigDecimal(new char[]{'a'}, 0, 1));
    }

}