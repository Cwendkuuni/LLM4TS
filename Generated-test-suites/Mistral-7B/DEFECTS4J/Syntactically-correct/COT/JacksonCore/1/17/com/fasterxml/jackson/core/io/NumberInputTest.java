package com.example.junit;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidInt() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_ValidIntWithOffset() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 2, 3);
        assertEquals(123, actual);
    }

    @Test
    public void testParseInt_ValidIntFromString() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_InvalidInput() {
        String input = "abc";
        int expected = 0;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_NegativeNumberWithOffset() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4'};
        NumberInput.parseInt(digitChars, 0, digitChars.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_NegativeNumberFromString() {
        String input = "-12345";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseLong_ValidLong() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_ValidLongFromString() {
        String input = "123456789";
        long expected = 123456789;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_ValidLongWithOffsetAndLength() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789;
        long actual = NumberInput.parseLong(digitChars, 6, 3);
        assertEquals(678, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_NegativeNumber() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
        NumberInput.parseLong(digitChars, 0, digitChars.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_NegativeNumberFromString() {
        String input = "-123456789";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseLong_LongOutOfRange() {
        char[] digitChars = new char[]{'9', '2', '2', '5', '0', '7', '3', '8', '5', '0', '7', '2', '0', '1', '2'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_LongOutOfRangeNegative() {
        char[] digitChars = new char[]{'-', '9', '2', '2', '5', '0', '7', '3', '8', '5', '0', '7', '2', '0', '1', '2'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_LongOutOfRangeString() {
        String input = "922507385072012";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(input, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_LongOutOfRangeStringNegative() {
        String input = "-922507385072012";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(input, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt_ValidInt() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt_ValidIntWithDefaultValue() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(input, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt_InvalidInput() {
        String input = "abc";
        int expected = -1;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt_NegativeNumber() {
        String input = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLong_ValidLong() {
        String input = "123456789";
        long expected = 123456789;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLong_ValidLongWithDefaultValue() {
        String input = "123456789";
        long expected = 123456789;
        long actual = NumberInput.parseAsLong(input, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLong_InvalidInput() {
        String input = "abc";
        long expected = -1;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLong_NegativeNumber() {
        String input = "-123456789";
        long expected = -123456789;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDouble_ValidDouble() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDouble_ValidDoubleWithDefaultValue() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, -1);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDouble_InvalidInput() {
        String input = "abc";
        double expected = 0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDouble_NegativeNumber() {
        String input = "-123.45";
        double expected = -123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseBigDecimal_ValidBigDecimal() {
        String input = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimal_ValidBigDecimalFromCharArray() {
        char[] buffer = "123.45".getBytes(StandardCharsets.UTF_8);
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimal_ValidBigDecimalFromCharArrayWithOffsetAndLength() {
        char[] buffer = "123.45".getBytes(StandardCharsets.UTF_8);
        BigDecimal expected = new BigDecimal("45");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer, 5, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimal_InvalidInput() {
        String input = "abc";
        BigDecimal expected = null;
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }
}