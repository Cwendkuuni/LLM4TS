package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int expected = 123;
        int actual = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromCharArrayWithOffset() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int expected = 123;
        int actual = NumberInput.parseInt(digitChars, 1, 2);
        assertEquals(expected * 10 + 2, actual);
    }

    @Test
    public void testParseIntFromCharArrayWithLenGreaterThanInputLength() {
        char[] digitChars = new char[]{'1'};
        int offset = 0;
        int len = 5;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(0, actual);
    }

    @Test
    public void testParseIntFromCharArrayWithLenLessThanInputLength() {
        char[] digitChars = new char[]{'1', '2', '3', '4'};
        int offset = 1;
        int len = 2;
        int expected = 12;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testParseIntFromCharArrayWithInvalidCharacter() {
        char[] digitChars = new char[]{'1', '2', '3', 'x'};
        int offset = 3;
        int len = 1;
        NumberInput.parseInt(digitChars, offset, len);
    }

    @Test
    public void testParseIntFromString() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromStringWithNegativeSign() {
        String input = "-123";
        int expected = -123;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromStringWithInvalidCharacter() {
        String input = "123x";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        long expected = 12345;
        long actual = NumberInput.parseLong(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromString() {
        String input = "12345";
        long expected = 12345;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromStringWithNegativeSign() {
        String input = "-12345";
        long expected = -12345;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromStringWithInvalidCharacter() {
        String input = "123x";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseLongInRangeFromCharArray() {
        char[] digitChars = new char[]{'1', '2'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 2, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongNotInRangeFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3'};
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 3, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongInRangeFromString() {
        String input = "12";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(input, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongNotInRangeFromString() {
        String input = "123";
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(input, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntFromString() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntFromStringWithNegativeSign() {
        String input = "-123";
        int expected =
                -123;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntFromStringWithDefaultValue() {
        String input = "abcd";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromString() {
        String input = "12345678901234567890";
        long expected = 12345678901234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromStringWithNegativeSign() {
        String input = "-12345678901234567890";
        long expected = -12345678901234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongFromStringWithDefaultValue() {
        String input = "abcd";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleFromString() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.00001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleFromStringWithInvalidCharacter() {
        String input = "123x";
        NumberInput.parseAsDouble(input, 0);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] input = new char[]{'4', '5', '.'};
        for (int i = 0; i < input.length; i++) {
            if (i == 1 && input[i] != '.') {
                throw new AssertionError();
            }
        }
        BigDecimal expected = new BigDecimal("4.5");
        BigDecimal actual = NumberInput.parseBigDecimal(input, 0, input.length);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalFromCharArrayWithInvalidCharacter() {
        char[] input = new char[]{'4', '5', 'x'};
        NumberInput.parseBigDecimal(input, 0, input.length);
    }

}