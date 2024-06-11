package com.fasterxml.jackson.core.io; // make sure the test class is in the same package as the testee class

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NumberInputTest {

    @Test
    public void testParseIntFromSmallArray() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromString() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromStringWithNegativeSign() {
        String input = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromStringWithInvalidCharacter() {
        String input = "123a5";
        NumberInput.parseInt(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntFromArrayWithInvalidLength() {
        char[] digitChars = {'1', '2', '3'};
        NumberInput.parseInt(digitChars, 0, 4);
    }

    @Test
    public void testParseLongFromSmallArray() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 12345678L;
        long actual = NumberInput.parseLong(digitChars, 0, 8);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromString() {
        String input = "12345678901234567890";
        long expected = 12345678901234567890L;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromStringWithInvalidLength() {
        String input = "1234567890123456789";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseLongAsIntFromSmallArray() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, NumberInput.parseInt(digitChars, 0, 5));
    }

    @Test
    public void testParseLongAsIntFromString() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongAsIntFromStringWithInvalidCharacter() {
        String input = "123a5";
        NumberInput.parseInt(input);
    }

    @Test
    public void testIsInLongRangeForIntArray() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 9, false);
        assertTrue(expected == actual);
    }

    @Test
    public void testIsInLongRangeForString() {
        String numberStr = "12345678901234567890";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(numberStr, false);
        assertTrue(expected == actual);
    }

    @Test
    public void testIsInLongRangeForNegativeNumberString() {
        String numberStr = "-12345678901234567890";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(numberStr, false);
        assertTrue(expected == actual);
    }

    @Test
    public void testIsNotInLongRangeForStringWithInvalidLength() {
        String numberStr = "1234567890123456789";
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(numberStr, false);
        assertFalse(expected == actual);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = "12345";
        int defaultValue = 0;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(12345, actual);
    }

    @Test
    public void testParseAsIntWithDefaultValueWhenInputIsEmpty() {
        String input = "";
        int defaultValue = 0;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(defaultValue, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntWithInvalidCharacter() {
        String input = "123a5";
        int defaultValue = 0;
        NumberInput.parseAsInt(input, defaultValue);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithInvalidCharacter() {
        String input = "123a5";
        long defaultValue = 0L;
        NumberInput.parseAsLong(input, defaultValue);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "123.45";
        double defaultValue = 0.0;
        double actual = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidCharacter() {
        String input = "123.a5";
        NumberInput.parseAsDouble(input, 0.0);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithOffsetAndLength() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalFromStringWithInvalidCharacter() {
        String numStr = "123.a5";
        NumberInput.parseBigDecimal(numStr.toCharArray());
    }
}