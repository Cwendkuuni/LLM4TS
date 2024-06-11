package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int num = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntFromCharArrayWithLengthOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
        int num = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, num);

        try {
            num = NumberInput.parseInt(digitChars, 0, 6);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            num = NumberInput.parseInt(digitChars, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseIntFromString() {
        String strNum = "12345";
        int num = NumberInput.parseInt(strNum);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntFromStringWithNegativeSign() {
        String strNum = "-12345";
        int num = NumberInput.parseInt(strNum);
        assertEquals(-12345, num);
    }

    @Test
    public void testParseIntFromStringWithLengthOutOfRange() {
        String strNum = "123456";
        int num = NumberInput.parseInt(strNum);
        assertEquals(12345, num);

        try {
            num = NumberInput.parseInt("1234567");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            num = NumberInput.parseInt("12345");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long num = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals(123456789L, num);
    }

    @Test
    public void testParseLongFromCharArrayWithLengthOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long num = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(12345678901L, num);

        try {
            num = NumberInput.parseLong(digitChars, 0, 8);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseLongFromString() {
        String strNum = "12345678901";
        long num = NumberInput.parseLong(strNum);
        assertEquals(12345678901L, num);
    }

    @Test
    public void testParseLongFromStringWithLengthOutOfRange() {
        String strNum = "123456789012345";
        long num = NumberInput.parseLong(strNum);
        assertEquals(123456789012345L, num);

        try {
            num = NumberInput.parseLong("123456789012346");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            num = NumberInput.parseLong("12345678901");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testInLongRangeFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean inRange = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertTrue(inRange);
    }

    @Test
    public void testInLongRangeFromCharArrayWithLengthOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean inRange = NumberInput.inLongRange(digitChars, 0, digitChars.length + 1, false);
        assertFalse(inRange);

        boolean inRange = NumberInput.inLongRange(digitChars, 0, digitChars.length - 1, false);
        assertFalse(inRange);
    }

    @Test
    public void testInLongRangeFromString() {
        String numStr = "12345678901";
        boolean inRange = NumberInput.inLongRange(numStr, false);
        assertTrue(inRange);
    }

    @Test
    public void testInLongRangeFromStringWithNegativeSign() {
        String numStr = "-12345678901";
        boolean inRange = NumberInput.inLongRange(numStr, false);
        assertTrue(inRange);
    }

    @Test
    public void testInLongRangeFromStringWithLengthOutOfRange() {
        String numStr = "123456789012345";
        boolean inRange = NumberInput.inLongRange(numStr, false);
        assertFalse(inRange);

        String numStrShort = "12345678901";
        boolean inRange = NumberInput.inLongRange(numStrShort, false);
        assertTrue(inRange);
    }

    @Test
    public void testParseAsIntFromString() {
        String input = "12345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, num);
    }

    @Test
    public void testParseAsIntFromStringWithNegativeSign() {
        String input = "-12345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(-12345, num);
    }

    @Test
    public void testParseAsIntFromStringWithNonDigitSymbols() {
        String input = "1a2b3c";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsIntFromStringWithDecimalPoint() {
        String input = "123.45";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsLongFromString() {
        String input = "123456789012345";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(123456789012345L, num);
    }

    @Test
    public void testParseAsLongFromStringWithNegativeSign() {
        String input = "-123456789012345";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(-123456789012345L, num);
    }

    @Test
    public void testParseAsLongFromStringWithNonDigitSymbols() {
        String input = "1a2b3c";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, num);
    }

    @Test
    public void testParseAsLongFromStringWithDecimalPoint() {
        String input = "123.45";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, num);
    }

    @Test
    public void testParseAsDoubleFromString() {
        String input = "123.45";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123.45, num, 0.001);
    }

    @Test
    public void testParseAsDoubleFromStringWithNegativeSign() {
        String input = "-123.45";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(-123.45, num, 0.001);
    }

    @Test
    public void testParseAsDoubleFromStringWithExponentialNotation() {
        String input = "1.2345e6";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(1234500.0, num, 0.001);
    }

    @Test
    public void testParseAsDoubleFromStringWithExponentialNotationNegative() {
        String input = "-1.2345e6";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(-1234500.0, num, 0.001);
    }
}