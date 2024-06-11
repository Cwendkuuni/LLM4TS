package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_MaxLength() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int actual = NumberInput.parseInt(digitChars, 0, 9);
        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test
    public void testParseInt_InvalidInput() {
        char[] digitChars = {'1', '2', '3', 'X'};
        int actual = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(0, actual);
    }

    @Test
    public void testParseInt_EmptyInput() {
        char[] digitChars = {};
        int actual = NumberInput.parseInt(digitChars, 0, 0);
        assertEquals(0, actual);
    }

    @Test
    public void testParseInt_NegativeInput() {
        char[] digitChars = {'-', '1', '2', '3', '4'};
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(-12345, actual);
    }

    @Test
    public void testParseLong_ValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0'};
        long expected = 12345678900L;
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_MaxLength() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', '0'};
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(Long.MAX_VALUE, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidInput() {
        char[] digitChars = {'1', '2', '3', 'X'};
        NumberInput.parseLong(digitChars, 0, 3);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_NegativeInput() {
        char[] digitChars = {'-', '1', '2', '3'};
        NumberInput.parseLong(digitChars, 0, 3);
    }

    @Test
    public void testParseLong_ValidStringInput() {
        String str = "12345678900";
        long actual = NumberInput.parseLong(str);
        long expected = 12345678900L;
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidStringInput() {
        String str = "123X45";
        NumberInput.parseLong(str);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_NegativeStringInput() {
        String str = "-12345";
        NumberInput.parseLong(str);
    }

    @Test
    public void testParseAsInt_ValidInput() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt_EmptyInput() {
        String input = "";
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(0, actual);
    }

    @Test
    public void testParseAsInt_NegativeInput() {
        String input = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt_InvalidInput() {
        String input = "12X45";
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(0, actual);
    }

    @Test
    public void testParseAsLong_ValidInput() {
        String input = "12345678900";
        long expected = 12345678900L;
        long actual = NumberInput.parseAsLong(input, 0L);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLong_EmptyInput() {
        String input = "";
        long actual = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseAsLong_NegativeInput() {
        String input = "-12345";
        long expected = -12345L;
        long actual = NumberInput.parseAsLong(input, 0L);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLong_InvalidInput() {
        String input = "12X45";
        long actual = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseAsDouble_ValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDouble_EmptyInput() {
        String input = "";
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(0, actual, 0.001);
    }

    @Test
    public void testParseAsDouble_InvalidInput() {
        String input = "12X45.6";
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(0, actual, 0.001);
    }
}