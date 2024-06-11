package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInputs() {
        char[] intDigits = new char[]{'1', '2', '3', '4', '5'};
        for (int length = 1; length <= 5; length++) {
            int offset = 0;
            int result = NumberInput.parseInt(intDigits, offset, length);
            assertEquals(Integer.parseInt(new String(intDigits, 0, length)), result);
        }

        String intString = "12345";
        int expected = Integer.parseInt(intString);
        int result = NumberInput.parseInt(intString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithInvalidInputs() {
        char[] intDigits = new char[]{'1', '2', '3', '4', '5', '6'};
        int offset = 0;
        int len = intDigits.length;
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseInt(intDigits, offset, len));

        int[] invalidInputs = new int[]{-1, 10, Integer.MAX_VALUE + 1};
        for (int input : invalidInputs) {
            String inputString = String.valueOf(input);
            assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(inputString));
        }
    }

    @Test
    public void testParseLongWithValidInputs() {
        char[] longDigits = new char[]{'1', '2', '3'};
        for (int length = 3; length <= 17; length += 3) {
            int offset = 0;
            long result = NumberInput.parseLong(longDigits, offset, length);
            assertEquals(Long.parseLong(new String(longDigits, 0, length)), result);
        }

        String longString = "1234567890123456789";
        long expected = Long.parseLong(longString);
        long result = NumberInput.parseLong(longString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithInvalidInputs() {
        char[] longDigits = new char[]{'1', '2', '3', '4', '5', '6', '7'};
        int offset = 0;
        int len = longDigits.length;
        assertThrows(IllegalArgumentException.class, () -> NumberInput.parseLong(longDigits, offset, len));

        long[] invalidInputs = new long[]{Long.MIN_VALUE - 1, Long.MAX_VALUE};
        for (long input : invalidInputs) {
            String inputString = String.valueOf(input);
            assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(inputString));
        }
    }

    @Test
    public void testParseAsIntWithValidInputs() {
        String[] inputStrings = new String[]{"123", "+123", " 123", "  123", "123   ", "123  "};
        int defaultValue = 45;
        for (String input : inputStrings) {
            int result = NumberInput.parseAsInt(input, defaultValue);
            assertEquals(Integer.parseInt(input), result);
        }
    }

    @Test
    public void testParseAsIntWithInvalidInputs() {
        String[] inputStrings = new String[]{"", "  ", "123a", "123.123", "123x", "abc"};
        int defaultValue = 45;
        for (String input : inputStrings) {
            int result = NumberInput.parseAsInt(input, defaultValue);
            assertEquals(defaultValue, result);
        }
    }

    @Test
    public void testParseAsLongWithValidInputs() {
        String[] inputStrings = new String[]{"1234567890123456789", "+1234567890123456789", " 1234567890123456789", "  1234567890123456789", "1234567890123456789   ", "1234567890123456789  "};
        long defaultValue = 1234567890123456789L;
        for (String input : inputStrings) {
            long result = NumberInput.parseAsLong(input, defaultValue);
            assertEquals(Long.parseLong(input), result);
        }
    }

    @Test
    public void testParseAsLongWithInvalidInputs() {
        String[] inputStrings = new String[]{"", "  ", "123a", "123.123", "123x", "abc"};
        long defaultValue = 1234567890123456789L;
        for (String input : inputStrings) {
            long result = NumberInput.parseAsLong(input, defaultValue);
            assertEquals(defaultValue, result);
        }
    }

    @Test
    public void testParseAsDoubleWithValidInputs() {
        String[] inputStrings = new String[]{"123.123", "123.123E-1", "1.23E3", "1.23", "123"};
        double defaultValue = 123.123;
        for (String input : inputStrings) {
            double result = NumberInput.parseAsDouble(input, defaultValue);
            assertEquals(Double.parseDouble(input), result, 0.001);
        }
    }

    @Test
    public void testParseAsDoubleWithInvalidInputs() {
        String[] inputStrings = new String[]{"", "  ", "123a", "123.123x", "abc", "123.123e"};
        double defaultValue = 123.123;
        for (String input : inputStrings) {
            double result = NumberInput.parseAsDouble(input, defaultValue);
            assertEquals(defaultValue, result, 0.0);
        }
    }

    @Test
    public void testParseBigDecimalWithValidInputs() {
        String[] inputStrings = new String[]{"123.123", "123.123E-1", "1.23E3", "1.23", "123"};
        BigDecimal defaultValue = new BigDecimal("123.123");
        for (String input : inputStrings) {
            BigDecimal result = NumberInput.parseBigDecimal(input);
            assertEquals(new BigDecimal(input), result);
        }
    }

    @Test
    public void testParseBigDecimalWithInvalidInputs() {
        String[] inputStrings = new String[]{"", "  ", "123a", "123.123x", "abc", "123.123e"};
        BigDecimal defaultValue = new BigDecimal("123.123");
        for (String input : inputStrings) {
            BigDecimal result = NumberInput.parseBigDecimal(input);
            assertEquals(defaultValue, result);
        }
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] charArray = new char[]{'1', '2', '.', '3', '1', '2', '3'};
        String expectedString = "123.123";
        BigDecimal expected = new BigDecimal(expectedString);
        BigDecimal result = NumberInput.parseBigDecimal(charArray);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithInvalidCharArray() {
        char[] charArray = new char[]{'1', '2', '.', '3', '1', '2', 'x'};
        BigDecimal defaultValue = new BigDecimal("123.123");
        BigDecimal result = NumberInput.parseBigDecimal(charArray);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testParseBigDecimalWithCharArrayLongOffsetAndLength() {
        char[] charArray = new char[]{'1', '2', '.', '3', '1', '2', '3'};
        int offset = 0;
        int length = 7;
        String expectedString = "123.123";
        BigDecimal expected = new BigDecimal(expectedString);
        BigDecimal result = NumberInput.parseBigDecimal(charArray, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithCharArrayInvalidOffsetAndLength() {
        char[] charArray = new char[]{'1', '2', '.', '3', '1', '2', '3'};
        int offset = 1;
        int length = 6;
        BigDecimal defaultValue = new BigDecimal("123.123");
        BigDecimal result = NumberInput.parseBigDecimal(charArray, offset, length);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testInLongRangeWithValidInputs() {
        char[] intDigits1 = new char[]{'0', '1', '2', '3', '4', '5', '6'};
        char[] intDigits2 = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean negative = false;
        assertTrue(NumberInput.inLongRange(intDigits1, 0, intDigits1.length, negative));
        assertTrue(NumberInput.inLongRange(intDigits2, 0, intDigits2.length, negative));

        String intString1 = "0123456789999999999";
        assertTrue(NumberInput.inLongRange(intString1, negative));

        String intString2 = "999999999999999999";
        assertTrue(NumberInput.inLongRange(intString2, negative));
    }

    @Test
    public void testInLongRangeWithNegativeInputs() {
        char[] intDigits1 = new char[]{'0', '1', '2', '3', '4', '5', '6'};
        char[] intDigits2 = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean negative = true;
        assertTrue(NumberInput.inLongRange(intDigits1, 0, intDigits1.length, negative));
        assertTrue(NumberInput.inLongRange(intDigits2, 0, intDigits2.length, negative));

        String intString1 = "0123456789999999999";
        assertTrue(NumberInput.inLongRange(intString1, negative));

        String intString2 = "999999999999999999";
        assertTrue(NumberInput.inLongRange(intString2, negative));
    }

    @Test
    public void testInLongRangeWithInvalidInputs() {
        char[] intDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean negative = false;
        assertFalse(NumberInput.inLongRange(intDigits, 0, intDigits.length, negative));

        String intString = "1234567890123456789";
        assertFalse(NumberInput.inLongRange(intString, negative));

        char[] longDigits = new char[]{'1', '2', '3'};
        assertFalse(NumberInput.inLongRange(longDigits, 0, longDigits.length, negative));

        String longString = "123";
        assertFalse(NumberInput.inLongRange(longString, negative));
    }

    @Test
    public void testParseLongWithVeryLargeNumber() {
        char[] charArray = new char[19];
        for (int i = 0; i < 19; i++) {
            charArray[i] = '9';
        }
        long result = NumberInput.parseLong(charArray);
        BigInteger expected = new BigInteger("9223372036854775807");
        BigInteger actual = new BigInteger(Long.toString(result));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithVerySmallNumber() {
        char[] charArray = new char[18];
        for (int i = 0; i < 18; i++) {
            charArray[i] = '0';
        }
        charArray[18] = '2';
        long result = NumberInput.parseLong(charArray);
        BigDecimal expected = new BigDecimal("2.2250738585072012e-308");
        BigDecimal actual = new BigDecimal(NumberInput.NASTY_SMALL_DOUBLE);
        assertEquals(expected, actual);
        assertEquals(expected.doubleValue(), result, 0.0);
    }
}