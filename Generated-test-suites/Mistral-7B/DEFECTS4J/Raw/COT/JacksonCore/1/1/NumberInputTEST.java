package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromCharArrayWithNegativeNumber() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromCharArrayWithInvalidDigit() {
        char[] digitChars = new char[]{'1', '2', '3', '5', 'a'};
        int expected = -1;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromString() {
        String str = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromStringWithNegativeNumber() {
        String str = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseInt(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntFromStringWithInvalidCharacter() {
        String str = "123a45";
        int expected = -1;
        int actual = NumberInput.parseInt(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789L;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromString() {
        String str = "1234567890";
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongFromStringWithNegativeNumber() {
        String str = "-1234567890";
        long expected = -1234567890L;
        long actual = NumberInput.parseLong(str);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongFromStringWithInvalidCharacter() {
        String str = "123a4567890";
        NumberInput.parseLong(str);
    }

    @Test
    public void testInLongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 5, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithNegativeNumber() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 6, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithLongNumber() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 10, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithNegativeLongNumber() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 11, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithNumberOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 12, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String str = "1234567890.1234567890";
        BigDecimal expected = new BigDecimal("1234567890.1234567890");
        BigDecimal actual = NumberInput.parseBigDecimal(str);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalFromStringWithInvalidCharacter() {
        String str = "123a4567890.1234567890";
        NumberInput.parseBigDecimal(str);
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] buffer = new char[]{'1', '2', '3', '4', '5', '.', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
        BigDecimal expected = new BigDecimal("1234567890.12345678901234567890");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalFromCharArrayWithInvalidDigit() {
        char[] buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        NumberInput.parseBigDecimal(buffer);
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithNegativeSign() {
        char[] buffer = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal expected = new BigDecimal("-123456789");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithFractionPart() {
        char[] buffer = new char[]{'1', '2', '3', '4', '5', '.', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal expected = new BigDecimal("1234567890.1234567890");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, actual);
    }
}