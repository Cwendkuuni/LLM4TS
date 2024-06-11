package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'5', '2', '3'};
        int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
        int expected = 523;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromCharArrayWithLengthOutOfRange() {
        char[] digitChars = new char[]{'5', '2', '3'};
        int actual = NumberInput.parseInt(digitChars, 0, 10);
        int expected = 523;
        assertEquals(expected, actual);

        try {
            NumberInput.parseInt(digitChars, 0, 4);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            NumberInput.parseInt(digitChars, 0, 11);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseIntFromString() {
        String input = "523";
        int actual = NumberInput.parseInt(input);
        int expected = 523;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromStringWithLengthOutOfRange() {
        String input = "523";
        try {
            NumberInput.parseInt("123456789012345");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        String inputTwo = "523abc";
        try {
            NumberInput.parseInt(inputTwo);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        long expected = 12345678L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromCharArrayWithLengthOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long actual = NumberInput.parseLong(digitChars, 0, 19);
        long expected = 12345678L;
        assertEquals(expected, actual);

        try {
            NumberInput.parseLong(digitChars, 0, 10);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            NumberInput.parseLong(digitChars, 0, 20);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongFromString() {
        String input = "123456789012345";
        long actual = NumberInput.parseLong(input);
        long expected = 123456789012345L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromStringWithLengthOutOfRange() {
        String input = "123456789012345";
        try {
            NumberInput.parseLong("1234567890123456789012345");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        String inputTwo = "123456789012345abc";
        try {
            NumberInput.parseLong(inputTwo);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testIsInLongRangeFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3'};
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertTrue(actual);
    }

    @Test
    public void testIsInLongRangeFromCharArrayWithNegativeFlag() {
        char[] digitChars = new char[]{'1', '2', '3'};
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, true);
        assertFalse(actual);
    }

    @Test
    public void testIsInLongRangeFromString() {
        String input = "123";
        boolean actual = NumberInput.inLongRange(input, false);
        assertTrue(actual);
    }

    @Test
    public void testIsInLongRangeFromStringWithNegativeFlag() {
        String input = "-123";
        boolean actual = NumberInput.inLongRange(input, true);
        assertFalse(actual);
    }

    @Test
    public void testParseAsIntFromString() {
        String input = "123";
        int actual = NumberInput.parseAsInt("123", 0);
        int expected = 123;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntFromStringWithDefaultValue() {
        String input = "abc";
        int actual = NumberInput.parseAsInt("abc", 42);
        int expected = 42;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromString() {
        String input = "123";
        long actual = NumberInput.parseAsLong("123", 0L);
        long expected = 123L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromStringWithDefaultValue() {
        String input = "abc";
        long actual = NumberInput.parseAsLong("abc", 12345L);
        long expected = 12345L;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleFromString() {
        String input = "3.14";
        double actual = NumberInput.parseAsDouble("3.14", 0.0);
        double expected = 3.14;
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testParseAsDoubleFromStringWithDefaultValue() {
        String input = "abc";
        double actual = NumberInput.parseAsDouble("abc", 123.45);
        double expected = 123.45;
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String input = "1.23";
        BigDecimal actual = NumberInput.parseBigDecimal("1.23");
        BigDecimal expected = new BigDecimal("1.23");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] digitChars = new char[]{'1', '.', '2', '3'};
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length);
        BigDecimal expected = new BigDecimal("1.23");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithOffset() {
        char[] digitChars = new char[]{'1', '.', '2', '3'};
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 1, 3);
        BigDecimal expected = new BigDecimal("1.23");
        assertEquals(expected, actual);
    }
}