package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithinRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 9;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(123456789, num);
    }

    @Test
    public void testParseIntWithNegativeSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 11;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(-123456789, num);
    }

    @Test
    public void testParseIntWithLengthLessThanMinimum() {
        char[] digitChars = new char[]{'1'};
        int offset = 0, len = 1;
        int num = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(1, num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidCharacter() {
        char[] digitChars = new char[]{'1', 'a', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 10;
        NumberInput.parseInt(digitChars, offset, len);
    }

    @Test
    public void testParseLongWithinRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        int offset = 0, len = 13;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(123456789123456L, num);
    }

    @Test
    public void testParseLongWithNegativeSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        int offset = 0, len = 15;
        long num = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(-123456789123456123L, num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithLengthLessThanMinimum() {
        char[] digitChars = new char[]{'1'};
        int offset = 0, len = 1;
        NumberInput.parseLong(digitChars, offset, len);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidCharacter() {
        char[] digitChars = new char[]{'1', 'a', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 10;
        NumberInput.parseLong(digitChars, offset, len);
    }

    @Test
    public void testParseLongAsBigDecimal() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        int offset = 0, len = 13;
        BigDecimal num = NumberInput.parseBigDecimal(digitChars, offset, len);
        assertEquals(new BigDecimal("123456789123456"), num);
    }

    @Test
    public void testParseLongAsBigDecimalWithNegativeSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        int offset = 0, len = 15;
        BigDecimal num = NumberInput.parseBigDecimal(digitChars, offset, len);
        assertEquals(new BigDecimal("-123456789123456123"), num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongAsBigDecimalWithInvalidCharacter() {
        char[] digitChars = new char[]{'1', 'a', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0, len = 10;
        NumberInput.parseBigDecimal(digitChars, offset, len);
    }

    @Test
    public void testParseStringAsIntWithinRange() {
        String input = "123456789";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(123456789, num);
    }

    @Test
    public void testParseStringAsIntWithNegativeSign() {
        String input = "-123456789";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(-123456789, num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseStringAsIntWithInvalidCharacter() {
        String input = "1a3456789";
        NumberInput.parseAsInt(input, 0);
    }

    @Test
    public void testParseStringAsLongWithinRange() {
        String input = "123456789123456";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(123456789123456L, num);
    }

    @Test
    public void testParseStringAsLongWithNegativeSign() {
        String input = "-123456789123456123";
        long num = NumberInput.parseAsLong(input, 0L);
        assertEquals(-123456789123456123L, num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseStringAsLongWithInvalidCharacter() {
        String input = "1a3456789123456";
        NumberInput.parseAsLong(input, 0L);
    }

    @Test
    public void testParseStringAsDoubleWithinRange() {
        String input = "123456789.123456";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123456789.123456, num, 0.00001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseStringAsDoubleWithInvalidCharacter() {
        String input = "1a3456789.123456";
        NumberInput.parseAsDouble(input, 0.0);
    }

}