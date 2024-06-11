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
    public void testParseIntFromCharArrayWithLengthLessThanExpected() {
        char[] digitChars = new char[]{'1', '2'};
        int num = NumberInput.parseInt(digitChars, 0, 2);
        assertEquals(12, num);
    }

    @Test
    public void testParseIntFromCharArrayWithLengthGreaterThanExpected() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
        int num = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntFromString() {
        String numStr = "12345";
        int num = NumberInput.parseInt(numStr);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntFromStringWithNegativeSign() {
        String numStr = "-12345";
        int num = NumberInput.parseInt(numStr);
        assertEquals(-12345, num);
    }

    @Test
    public void testParseIntFromStringWithLengthGreaterThanExpected() {
        String numStr = "123456";
        int num = NumberInput.parseInt(numStr);
        assertEquals(12345, num);
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long num = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseLongFromCharArrayWithLengthLessThanExpected() {
        char[] digitChars = new char[]{'1', '2', '3'};
        long num = NumberInput.parseLong(digitChars, 0, 3);
        assertEquals(123, num);
    }

    @Test
    public void testParseLongFromCharArrayWithLengthGreaterThanExpected() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long num = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(12345678901L, num);
    }

    @Test
    public void testParseLongFromString() {
        String numStr = "1234567890";
        long num = NumberInput.parseLong(numStr);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseLongFromStringWithNegativeSign() {
        String numStr = "-1234567890";
        long num = NumberInput.parseLong(numStr);
        assertEquals(-1234567890L, num);
    }

    @Test
    public void testParseLongFromStringWithLengthGreaterThanExpected() {
        String numStr = "12345678901";
        long num = NumberInput.parseLong(numStr);
        assertEquals(12345678901L, num);
    }

    @Test
    public void testIsInLongRangeFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3'};
        boolean inLongRange = NumberInput.inLongRange(digitChars, 0, 3, false);
        assertTrue(inLongRange);
    }

    @Test
    public void testIsInLongRangeFromCharArrayWithNegativeFlag() {
        char[] digitChars = new char[]{'1', '2', '3'};
        boolean inLongRange = NumberInput.inLongRange(digitChars, 0, 3, true);
        assertFalse(inLongRange);
    }

    @Test
    public void testIsInLongRangeFromString() {
        String numStr = "123";
        boolean inLongRange = NumberInput.inLongRange(numStr, false);
        assertTrue(inLongRange);
    }

    @Test
    public void testIsInLongRangeFromStringWithNegativeFlag() {
        String numStr = "123";
        boolean inLongRange = NumberInput.inLongRange(numStr, true);
        assertFalse(inLongRange);
    }

    @Test
    public void testParseAsIntFromString() {
        String input = "123";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(123, num);
    }

    @Test
    public void testParseAsIntFromStringWithNegativeSign() {
        String input = "-123";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(-123, num);
    }

    @Test
    public void testParseAsIntFromStringWithNonIntegerValue() {
        String input = "123.45";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsLongFromString() {
        String input = "1234567890";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseAsLongFromStringWithNegativeSign() {
        String input = "-1234567890";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(-1234567890L, num);
    }

    @Test
    public void testParseAsLongFromStringWithNonLongValue() {
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
    public void testParseAsDoubleFromStringWithNonDoubleValue() {
        String input = "123";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123.0, num, 0.001);
    }

    @Test
    public void testParseDoubleFromString() {
        String input = "123.45";
        try {
            double num = NumberInput.parseDouble(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseBigDecimalFromString() {
        String input = "123.45";
        try {
            BigDecimal num = NumberInput.parseBigDecimal(input);
            assertNotNull(num);
        } catch (NumberFormatException e) {
            fail("Expected BigDecimal to be parsed");
        }
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] input = new char[]{'1', '2', '3', '.' , '4', '5'};
        try {
            BigDecimal num = NumberInput.parseBigDecimal(input);
            assertNotNull(num);
        } catch (NumberFormatException e) {
            fail("Expected BigDecimal to be parsed");
        }
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithLengthLessThanExpected() {
        char[] input = new char[]{'1', '2', '3'};
        try {
            BigDecimal num = NumberInput.parseBigDecimal(input);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

}