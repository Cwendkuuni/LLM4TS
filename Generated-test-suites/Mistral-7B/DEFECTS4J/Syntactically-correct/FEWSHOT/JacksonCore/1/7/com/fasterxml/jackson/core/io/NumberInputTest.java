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
    public void testNASTY_SMALL_DOUBLE() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE));
    }

    @Test
    public void testParseIntFromDigits_smallNumber() {
        char[] digitChars = new char[]{'1'};
        assertEquals(1, NumberInput.parseInt(digitChars, 0, 1));
    }

    @Test
    public void testParseIntFromDigits_largeNumber() {
        char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertEquals(10000000009L, (long) NumberInput.parseInt(digitChars, 0, 10));
    }

    @Test
    public void testParseIntFromDigits_longerNumber() {
        char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseInt(digitChars, 0, 11));
    }

    @Test
    public void testParseIntFromString_smallNumber() {
        assertEquals(1, NumberInput.parseInt("1"));
    }

    @Test
    public void testParseIntFromString_negativeNumber() {
        assertEquals(-1, NumberInput.parseInt("-1"));
    }

    @Test
    public void testParseIntFromString_numberWithLeadingZeroes() {
        assertEquals(0, NumberInput.parseInt("000001"));
    }

    @Test
    public void testParseIntFromString_numberWithLeadingMinusSign() {
        assertEquals(-1, NumberInput.parseInt("-1"));
        assertEquals(1, NumberInput.parseInt("1"));
    }

    @Test
    public void testParseIntFromString_largeNumber() {
        assertEquals(123456789, NumberInput.parseInt("123456789"));
    }

    @Test
    public void testParseIntFromString_largeNumberWithLeadingMinusSign() {
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
    }

    @Test
    public void testParseLongFromDigits_smallNumber() {
        char[] digitChars = new char[]{'1'};
        assertEquals(1, NumberInput.parseLong(digitChars, 0, 1));
    }

    @Test
    public void testParseLongFromDigits_largeNumber() {
        char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertEquals(1125899906842597L, NumberInput.parseLong(digitChars, 0, 18));
    }

    @Test
    public void testParseLongFromDigits_longerNumber() {
        char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseLong(digitChars, 0, 19));
    }

    @Test
    public void testParseLongFromString_smallNumber() {
        assertEquals(1, NumberInput.parseLong("1"));
    }

    @Test
    public void testParseLongFromString_negativeNumber() {
        assertEquals(-1, NumberInput.parseLong("-1"));
    }

    @Test
    public void testParseLongFromString_numberWithLeadingZeroes() {
        assertEquals(0, NumberInput.parseLong("00000000001"));
    }

    @Test
    public void testParseLongFromString_numberWithLeadingMinusSign() {
        assertEquals(-1, NumberInput.parseLong("-1"));
        assertEquals(1, NumberInput.parseLong("1"));
    }

    @Test
    public void testParseLongFromString_largeNumber() {
        assertEquals(Long.parseLong("1234567890123456789"), NumberInput.parseLong("1234567890123456789"));
    }

    @Test
    public void testParseLongFromString_largeNumberWithLeadingMinusSign() {
        assertEquals(-Long.parseLong("1234567890123456789"), NumberInput.parseLong("-1234567890123456789"));
    }

    @Test
    public void testInLongRange_intArray() {
        char[] digitChars = new char[]{'1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 3, false));
    }

    @Test
    public void testInLongRange_intArray_negative() {
        char[] digitChars = new char[]{'1', '2', '3'};
        assertFalse(NumberInput.inLongRange(digitChars, 0, 3, true));
    }

    @Test
    public void testInLongRange_String() {
        String numberStr = "123";
        assertTrue(NumberInput.inLongRange(numberStr, false));
    }

    @Test
    public void testInLongRange_String_negative() {
        String numberStr = "123";
        assertFalse(NumberInput.inLongRange(numberStr, true));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(1, NumberInput.parseAsInt("1"));
        assertEquals(0, NumberInput.parseAsInt(""));
        assertEquals(123, NumberInput.parseAsInt("123"));
        assertEquals(0, NumberInput.parseAsInt("abc"));
        assertEquals(123, NumberInput.parseAsInt(" 123 "));
        assertEquals(123, NumberInput.parseAsInt("123 "));
        assertEquals(123, NumberInput.parseAsInt("123\t"));
        assertEquals(123, NumberInput.parseAsInt("123\n"));
        assertEquals(123, NumberInput.parseAsInt("123\r"));
        assertEquals(123, NumberInput.parseAsInt("123\r\n"));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(1, NumberInput.parseAsLong("1"));
        assertEquals(0, NumberInput.parseAsLong(""));
        assertEquals(123, NumberInput.parseAsLong("123"));
        assertEquals(Long.MIN_VALUE, NumberInput.parseAsLong(NumberInput.NASTY_SMALL_DOUBLE));
        assertEquals(0, NumberInput.parseAsLong("abc"));
        assertEquals(123, NumberInput.parseAsLong(" 123 "));
        assertEquals(123, NumberInput.parseAsLong("123 "));
        assertEquals(123, NumberInput.parseAsLong("123\t"));
        assertEquals(123, NumberInput.parseAsLong("123\n"));
        assertEquals(123, NumberInput.parseAsLong("123\r"));
        assertEquals(123, NumberInput.parseAsLong("123\r\n"));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(1.0, NumberInput.parseAsDouble("1"), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble(""));
        assertEquals(123.5, NumberInput.parseAsDouble("123.5"), 0.0);
        assertEquals(Double.MIN_VALUE, NumberInput.parseAsDouble(NumberInput.NASTY_SMALL_DOUBLE));
        assertEquals(0.0, NumberInput.parseAsDouble("abc"));
    }

    @Test
    public void testParseBigDecimal() {
        String testString = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
        BigDecimal expected = new BigDecimal(testString);
        BigDecimal result = NumberInput.parseBigDecimal(testString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] testArray = new char[testString.length()];
        System.arraycopy(testString.toCharArray(), 0, testArray, 0, testString.length());
        BigDecimal expected = new BigDecimal(testString);
        BigDecimal result = NumberInput.parseBigDecimal(testArray);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray_offsetLength() {
        char[] testArray = new char[testString.length()];
        System.arraycopy(testString.toCharArray(), 0, testArray, 0, testString.length());
        BigDecimal expected = new BigDecimal(testString.substring(5));
        BigDecimal result = NumberInput.parseBigDecimal(testArray, 5, 13);
        assertEquals(expected, result);
    }
}