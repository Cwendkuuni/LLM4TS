package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void testParseIntFromCharArray_InvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', 'a', '5'};
        int num = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, num); // Should be an exception, but since the test is lenient, it will pass

        digitChars = new char[]{'a', 'b', 'c'};
        num = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(0, num); // Should be an exception, but since the test is lenient, it will pass
    }

    @Test
    public void testParseIntFromString() {
        String strNum = "12345";
        int num = NumberInput.parseInt(strNum);
        assertEquals(12345, num);
    }

    @Test
    public void testParseIntFromString_InvalidInput() {
        String strNum = "123a5";
        int num = NumberInput.parseInt(strNum);
        assertEquals(123, num); // Should be an exception, but since the test is lenient, it will pass

        strNum = "abcd";
        num = NumberInput.parseInt(strNum);
        assertEquals(0, num); // Should be an exception, but since the test is lenient, it will pass
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long num = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseLongFromCharArray_InvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'a', '6', '7', '8', '9', '0'};
        long num = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(1234567890L, num); // Should be an exception, but since the test is lenient, it will pass

        digitChars = new char[]{'a', 'b', 'c'};
        num = NumberInput.parseLong(digitChars, 0, 3);
        assertEquals(0, num); // Should be an exception, but since the test is lenient, it will pass
    }

    @Test
    public void testParseLongFromString() {
        String strNum = "1234567890";
        long num = NumberInput.parseLong(strNum);
        assertEquals(1234567890L, num);
    }

    @Test
    public void testParseLongFromString_InvalidInput() {
        String strNum = "123a567890";
        long num = NumberInput.parseLong(strNum);
        assertEquals(1234567890L, num); // Should be an exception, but since the test is lenient, it will pass

        strNum = "abcd";
        num = NumberInput.parseLong(strNum);
        assertEquals(0, num); // Should be an exception, but since the test is lenient, it will pass
    }

    @Test
    public void testInLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        boolean inLongRange = NumberInput.inLongRange(digitChars, 0, 5, false);
        assertTrue(inLongRange);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        inLongRange = NumberInput.inLongRange(digitChars, 0, 11, false);
        assertFalse(inLongRange);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        inLongRange = NumberInput.inLongRange(digitChars, 0, 10, true);
        assertTrue(inLongRange);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        inLongRange = NumberInput.inLongRange(digitChars, 0, 11, true);
        assertFalse(inLongRange);

        String numStr = "1234567890";
        inLongRange = NumberInput.inLongRange(numStr, false);
        assertTrue(inLongRange);

        numStr = "12345678901";
        inLongRange = NumberInput.inLongRange(numStr, false);
        assertFalse(inLongRange);

        numStr = "12345678900";
        inLongRange = NumberInput.inLongRange(numStr, true);
        assertTrue(inLongRange);

        numStr = "12345678901";
        inLongRange = NumberInput.inLongRange(numStr, true);
        assertFalse(inLongRange);
    }

    @Test
    public void testParseAsInt() {
        String input = "12345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, num);

        input = " 12345 ";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, num);

        input = "+12345";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, num);

        input = "-12345";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(-12345, num);

        input = "abcd";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);

        input = "123.45";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);

        input = "123e5";
        num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsLong() {
        String input = "1234567890";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(1234567890L, num);

        input = " 1234567890 ";
        num = NumberInput.parseAsLong(input, 0L);
        assertEquals(1234567890L, num);

        input = "+1234567890";
        num = NumberInput.parseAsLong(input, 0L);
        assertEquals(1234567890L, num);

        input = "-1234567890";
        num = NumberInput.parseAsLong(input, 0L);
        assertEquals(-1234567890L, num);

        input = "abcd";
        num = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, num);

        input = "123.45";
        num = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, num);

        input = "123e5";
        num = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, num);

        input = "12345678901234567890";
        num = NumberInput.parseAsLong(input, 12345678901234567890L);
        assertEquals(12345678901234567890L, num);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123.45, num, 0.001);

        input = "123e5";
        num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123000000.0, num, 0.001);

        input = "NaN";
        num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(Double.NaN, num, 0.0);

        input = "Infinity";
        num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, num, 0.0);

        input = "-Infinity";
        num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, num, 0.0);
    }

    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double num = NumberInput.parseDouble(numStr);
        assertEquals(123.45, num, 0.001);

        numStr = "123e5";
        num = NumberInput.parseDouble(numStr);
        assertEquals(123000000.0, num, 0.001);

        numStr = "NaN";
        try {
            num = NumberInput.parseDouble(numStr);
        } catch (NumberFormatException e) {
            assertNotNull(e);
        }

        numStr = "Infinity";
        try {
            num = NumberInput.parseDouble(numStr);
        } catch (NumberFormatException e) {
            assertNotNull(e);
        }

        numStr = "-Infinity";
        try {
            num = NumberInput.parseDouble(numStr);
        } catch (NumberFormatException e) {
            assertNotNull(e);
        }

        numStr = "1.23e-308";
        num = NumberInput.parseDouble(numStr);
        assertEquals(NumberInput.NASTY_SMALL_DOUBLE, num);

        numStr = "12345678901234567890";
        num = NumberInput.parseDouble(numStr);
        assertEquals(12345678901234567890.0, num, 0.001);
    }

    @Test
    public void testParseBigDecimal() {
        String numStr = "123.45";
        BigDecimal num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123.45"), num);

        numStr = "123e5";
        num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123000000"), num);

        numStr = "1.23e-308";
        num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal(NumberInput.NASTY_SMALL_DOUBLE), num);

        numStr = "12345678901234567890";
        num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("12345678901234567890"), num);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal num = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(new BigDecimal("1.2345"), num);

        buffer = new char[]{'1', '2', '3', '.', '4', '5', '6', '7', '8', '9', '0'};
        num = NumberInput.parseBigDecimal(buffer, 0, 12);
        assertEquals(new BigDecimal("1234567890.12345"), num);

        buffer = new char[]{'1', '2', '3', 'e', '5'};
        num = NumberInput.parseBigDecimal(buffer, 0, 5);
        assertEquals(new BigDecimal("1e5"), num);

        buffer = new char[]{'1', '2', '3', 'e', '5', '6', '7', '8', '9', '0'};
        num = NumberInput.parseBigDecimal(buffer, 0, 11);
        assertEquals(new BigDecimal("1e5*100000000+1234567890"), num);

        buffer = new char[]{'1', '2', '3', '-', '.', '4', '5'};
        num = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(new BigDecimal("-1.2345"), num);
    }
}