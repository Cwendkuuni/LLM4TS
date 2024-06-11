package com.fasterxml.jackson.core.io;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberInputTest {
    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithMinusSign() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithLengthGreaterThanNine() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int expected = 1234567890;
        int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNonDigitCharacter() {
        char[] digitChars = {'1', '2', '3', '4', '5', 'A'};
        NumberInput.parseInt(digitChars, 0, digitChars.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNegativeLength() {
        char[] digitChars = {'1'};
        NumberInput.parseInt(digitChars, 0, -1);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', '0', '0', '0', '0'};
        long expected = 12345678900000000L;
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithMinusSign() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', '0', '0', '0'};
        long expected = -12345678900000000L;
        long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInputLength() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6'};
        NumberInput.parseLong(digitChars, 0, digitChars.length);
    }

    // Add test cases for other methods similarly
}