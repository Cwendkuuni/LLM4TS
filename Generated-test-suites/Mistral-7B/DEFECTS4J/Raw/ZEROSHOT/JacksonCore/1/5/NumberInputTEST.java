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
    public void testParseIntWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int offset = 0, len = 5;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(12345, num);

        String str = "12345";
        num = NumberInput.parseInt(str);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntWithNegativeNumbers() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int offset = 0, len = 6;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(-12345, num);

        String str = "-12345";
        num = NumberInput.parseInt(str);
        assertEquals(-12345, num);
    }

    @Test
    public void testParseIntWithInvalidInputs() {
        char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
        int offset = 0, len = 5;
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitChars, offset, len));

        String str = "1a345";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(str));
    }

    @Test
    public void testParseLongWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int offset = 0, len = 10;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(1234567890L, num);

        String str = "1234567890";
        num = NumberInput.parseLong(str);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseLongWithNegativeNumbers() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int offset = 0, len = 12;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(-1234567890123L, num);

        String str = "-1234567890123";
        num = NumberInput.parseLong(str);
        assertEquals(-1234567890123L, num);
    }

    @Test
    public void testInLongRangeWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean negative = false;
        assertTrue(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));

        String numberStr = "1234567890";
        assertTrue(NumberInput.inLongRange(numberStr, negative));
    }

    @Test
    public void testInLongRangeWithInvalidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        boolean negative = false;
        assertFalse(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));

        String numberStr = "12345678901";
        assertFalse(NumberInput.inLongRange(numberStr, negative));
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = null;
        int defaultValue = 42;
        int num = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(defaultValue, num);

        input = "";
        num = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(defaultValue, num);
    }

    @Test
    public void testParseAsIntWithValidInputs() {
        String input = "12345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, num);

        input = "  12345  ";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, num);
    }

    @Test
    public void testParseAsIntWithNegativeNumbers() {
        String input = "-12345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(-12345, num);
    }

    @Test
    public void testParseAsIntWithInvalidInputs() {
        String input = "1a345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = null;
        long defaultValue = 42L;
        long num = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(defaultValue, num);

        input = "";
        num = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(defaultValue, num);
    }

    @Test
    public void testParseAsLongWithValidInputs() {
        String input = "1234567890";
        long num = NumberInput.parseAsLong(input, 0);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseAsLongWithNegativeNumbers() {
        String input = "-1234567890123";
        long num = NumberInput.parseAsLong(input, 0);
        assertEquals(-1234567890123L, num);
    }

    @Test
    public void testParseAsLongWithInvalidInputs() {
        String input = "1a34567890123";
        long num = NumberInput.parseAsLong(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = null;
        double defaultValue = 42.0;
        double num = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(defaultValue, num, 0.001);

        input = "";
        num = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(defaultValue, num, 0.001);
    }

    @Test
    public void testParseAsDoubleWithValidInputs() {
        String input = "123.45";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123.45, num, 0.001);
    }

    @Test
    public void testParseAsDoubleWithInvalidInputs() {
        String input = "1a3.45";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(0.0, num, 0.001);
    }

    @Test
    public void testParseDoubleWithValidInputs() {
        String numStr = "123.45";
        double num = NumberInput.parseDouble(numStr);
        assertEquals(123.45, num, 0.001);
    }

    @Test
    public void testParseDoubleWithInvalidInputs() {
        String numStr = "1a3.45";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseDouble(numStr));
    }

    @Test
    public void testParseBigDecimalWithValidInputs() {
        String numStr = "123.45";
        BigDecimal num = NumberInput.parseBigDecimal(numStr);
        BigDecimal expected = new BigDecimal("123.45");
        assertEquals(expected, num);
    }

    @Test
    public void testParseBigDecimalWithInvalidInputs() {
        String numStr = "1a3.45";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(numStr));
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
        int offset = 0, len = 6;
        BigDecimal num = NumberInput.parseBigDecimal(buffer, offset, len);
        BigDecimal expected = new BigDecimal("123.45");
        assertEquals(expected, num);
    }

    @Test
    public void testParseBigDecimalWithInvalidCharArray() {
        char[] buffer = new char[]{'1', '2', '3', '.', '4', 'a'};
        int offset = 0, len = 6;
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(buffer, offset, len));
    }
}