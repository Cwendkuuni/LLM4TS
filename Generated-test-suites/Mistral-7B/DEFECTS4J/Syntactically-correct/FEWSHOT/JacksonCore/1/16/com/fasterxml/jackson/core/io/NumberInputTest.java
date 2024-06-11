package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArrayWithinIntRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int num = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntFromCharArrayOutsideIntRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int num = NumberInput.parseInt(digitChars, 0, 9);
        assertEquals(123456789, num);

        try {
            num = NumberInput.parseInt(digitChars, 0, 10);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseLongFromCharArrayWithinLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long num = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(123456789101L, num);
    }

    @Test
    public void testParseLongFromCharArrayOutsideLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        try {
            num = NumberInput.parseLong(digitChars, 0, 12);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseLongFromStringWithinLongRange() {
        String numStr = "12345678901";
        long num = NumberInput.parseLong(numStr);
        assertEquals(12345678901L, num);
    }

    @Test
    public void testParseLongFromStringOutsideLongRange() {
        String numStr = "123456789012";
        try {
            num = NumberInput.parseLong(numStr);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseIntFromStringWithinIntRange() {
        String numStr = "123";
        int num = NumberInput.parseAsInt(numStr, 0);
        assertEquals(123, num);
    }

    @Test
    public void testParseIntFromStringOutsideIntRange() {
        String numStr = "12345";
        int num = NumberInput.parseAsInt("12345", 0);
        assertEquals(12345, num);

        numStr = "123456";
        num = NumberInput.parseAsInt(numStr, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseLongFromStringWithDefaultValue() {
        String numStr = "123";
        long num = NumberInput.parseAsLong("123", 456L);
        assertEquals(123L, num);

        numStr = "123456";
        num = NumberInput.parseAsLong("123456", 456L);
        assertEquals(123456L, num);

        numStr = "notANumber";
        num = NumberInput.parseAsLong("notANumber", 456L);
        assertEquals(456L, num);
    }

    @Test
    public void testParseDoubleFromStringWithDefaultValue() {
        String numStr = "123.45";
        double num = NumberInput.parseAsDouble("123.45", 456.0);
        assertEquals(123.45, num, 0.001);

        numStr = "123.456";
        num = NumberInput.parseAsDouble("123.456", 456.0);
        assertEquals(123.456, num, 0.001);

        numStr = "notANumber";
        num = NumberInput.parseAsDouble("notANumber", 456.0);
        assertEquals(456.0, num);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String numStr = "123.45";
        BigDecimal num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123.45"), num);

        numStr = "123.456";
        num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123.456"), num);

        numStr = "notANumber";
        try {
            num = NumberInput.parseBigDecimal(numStr);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal num = NumberInput.parseBigDecimal(digitChars, 0, 6);
        assertEquals(new BigDecimal("1.2345"), num);
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithInvalidFormat() {
        char[] digitChars = new char[]{'1', '2', '3', '.', '4', '5', 'a'};
        try {
            num = NumberInput.parseBigDecimal(digitChars, 0, 7);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }
}