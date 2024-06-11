package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] numChars = new char[] {'1', '2', '3', '4', '5'};
        assertEquals(12345, NumberInput.parseInt(numChars, 0, 5));
    }

    @Test
    public void testParseIntFromCharArrayWithNegativeSign() {
        char[] numChars = new char[] {'-', '1', '2', '3', '4', '5'};
        assertEquals(-12345, NumberInput.parseInt(numChars, 0, 6));
    }

    @Test
    public void testParseIntFromCharArrayWithLengthLessThan1() {
        char[] numChars = new char[] {'1'};
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseInt(numChars, 0, 0));
    }

    @Test
    public void testParseIntFromCharArrayWithLengthGreaterThanRequired() {
        char[] numChars = new char[] {'1', '2', '3', '4', '5', '6'};
        assertEquals(12345, NumberInput.parseInt(numChars, 0, 5));
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] numChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 1234567890L;
        assertEquals(expected, NumberInput.parseLong(numChars, 0, 10));
    }

    @Test
    public void testParseLongFromCharArrayWithLengthLessThan1() {
        char[] numChars = new char[] {'1'};
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseLong(numChars, 0, 0));
    }

    @Test
    public void testParseLongFromCharArrayWithLengthGreaterThanRequired() {
        char[] numChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long expected = 1234567890L;
        assertEquals(expected, NumberInput.parseLong(numChars, 0, 11));
    }

    @Test
    public void testParseLongFromString() {
        String numStr = "1234567890";
        long expected = 1234567890L;
        assertEquals(expected, NumberInput.parseLong(numStr));
    }

    @Test
    public void testParseLongFromStringWithNegativeSign() {
        String numStr = "-1234567890";
        long expected = -1234567890L;
        assertEquals(expected, NumberInput.parseLong(numStr));
    }

    @Test
    public void testParseLongFromStringWithLengthGreaterThanRequired() {
        String numStr = "1234567890abc";
        long expected = 1234567890L;
        assertEquals(expected, NumberInput.parseLong(numStr));
    }

    @Test
    public void testParseLongFromStringWithLengthLessThanRequired() {
        String numStr = "123";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(numStr));
    }

    @Test
    public void testInLongRangeWithCharArray() {
        char[] numChars = new char[] {'1', '2', '3', '4', '5'};
        assertTrue(NumberInput.inLongRange(numChars, 0, 5, false));
        assertTrue(NumberInput.inLongRange(numChars, 0, 5, true));
        assertFalse(NumberInput.inLongRange(numChars, 0, 6, false));
        assertFalse(NumberInput.inLongRange(numChars, 0, 6, true));
    }

    @Test
    public void testInLongRangeWithString() {
        String numStr = "12345";
        assertTrue(NumberInput.inLongRange(numStr, false));
        assertTrue(NumberInput.inLongRange(numStr, true));
        assertFalse(NumberInput.inLongRange("123456", false));
        assertFalse(NumberInput.inLongRange("123456", true));
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        assertEquals(0, NumberInput.parseAsInt("", 0));
        assertEquals(123, NumberInput.parseAsInt(" 123 ", 0));
        assertEquals(0, NumberInput.parseAsInt(" -123 ", 0));
        assertEquals(123, NumberInput.parseAsInt("1A2B3C", 0));
        assertEquals(0, NumberInput.parseAsInt("123.45", 0));
        assertEquals(0, NumberInput.parseAsInt(null, 0));
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        assertEquals(1234567890L, NumberInput.parseAsLong("1234567890", 0));
        assertEquals(0, NumberInput.parseAsLong("", 0));
        assertEquals(1234567890L, NumberInput.parseAsLong(" 1234567890 ", 0));
        assertEquals(-1234567890L, NumberInput.parseAsLong("-1234567890", 0));
        assertEquals(0, NumberInput.parseAsLong("1A2B3C", 0));
        assertEquals(0, NumberInput.parseAsLong("123.45", 0));
        assertEquals(0, NumberInput.parseAsLong(null, 0));
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", 0));
        assertEquals(0, NumberInput.parseAsDouble("", 0));
        assertEquals(123.45, NumberInput.parseAsDouble(" 123.45 ", 0));
        assertEquals(0, NumberInput.parseAsDouble("123", 0));
        assertEquals(Double.MIN_VALUE, NumberInput.parseAsDouble(NumberInput.NASTY_SMALL_DOUBLE, 0));
        assertEquals(0, NumberInput.parseAsDouble(null, 0));
    }

    @Test
    public void testParseBigDecimalWithDefaultValue() {
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45", null));
        assertEquals(null, NumberInput.parseBigDecimal("", null));
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal(" 123.45 ", null));
        assertEquals(null, NumberInput.parseBigDecimal("123", null));
        assertEquals(NumberInput.parseBigDecimal(NumberInput.NASTY_SMALL_DOUBLE, null), NumberInput.parseBigDecimal(NumberInput.NASTY_SMALL_DOUBLE, null));
        assertEquals(null, NumberInput.parseBigDecimal(null, null));
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithDefaultValue() {
        char[] numChars = new char[] {'1', '2', '3', '.', '4', '5'};
        assertEquals(new BigDecimal("1.2345"), NumberInput.parseBigDecimal(numChars, null));
        assertEquals(null, NumberInput.parseBigDecimal(new char[0], null));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(new char[]{'1', '2', '3'}, null));
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithOffsetAndLengthWithDefaultValue() {
        char[] numChars = new char[] {'1', '2', '3', '.', '4', '5'};
        assertEquals(new BigDecimal("1.2345"), NumberInput.parseBigDecimal(numChars, 3, 5));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(new char[0], 0, 0));
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(new char[]{'1', '2', '3'}, 1, 2));
    }
}