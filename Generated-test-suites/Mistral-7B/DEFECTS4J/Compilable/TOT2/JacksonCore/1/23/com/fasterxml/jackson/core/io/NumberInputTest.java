package com.fasterxml.jackson.core.io; // package name for imports

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'}; // 5 digits
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'}; // 5 digits, with minus sign
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}; // 10 digits
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', 'x'}; // 3 digits, with an invalid character 'x'
        NumberInput.parseLong(digitChars, 0, 3); // will throw NumberFormatException
    }

    @Test
    public void testParseLongWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'}; // 11 digits, with minus sign
        long expected = -1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithMinusSign() {
        String input = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "abc";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "1234567890";
        long expected = 1234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithMinusSign() {
        String input = "-1234567890";
        long expected = -1234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "abc";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0001); // allow small error since it's a float value
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "abc";
        double expected = 0.0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0001);
    }
}