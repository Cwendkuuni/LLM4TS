package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithLongInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int actual = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(123456789, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
        NumberInput.parseInt(digitChars, 0, 5); // should throw NumberFormatException
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long expected = 12345678901L;
        long actual = NumberInput.parseLong(digitChars, 0, 12);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long expected = -12345678901L;
        long actual = NumberInput.parseLong(digitChars, 0, 13);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'a', '6', '7', '8', '9'};
        NumberInput.parseLong(digitChars, 0, 10); // should throw NumberFormatException
    }

    @Test
    public void testParseLongWithLongInputBeyondIntRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        long actual = NumberInput.parseLong(digitChars, 0, 12);
        assertTrue(NumberInput.inLongRange(new BigInteger(Long.toString(actual)).toString().toCharArray(), false));
    }

    @Test
    public void testParseLongWithLongInputWithinIntRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        long actual = NumberInput.parseLong(digitChars, 0, 5);
        assertTrue(NumberInput.inLongRange(new BigInteger(Long.toString(actual)).toString().toCharArray(), false));
    }

    @Test
    public void testParseLongWithLongInputNegative() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        long actual = NumberInput.parseLong(digitChars, 0, 6);
        assertFalse(NumberInput.inLongRange(new BigInteger(Long.toString(actual)).toString().toCharArray(), true));
    }

    @Test
    public void testParseLongWithStringInputValid() {
        String str = "12345678901";
        long expected = 12345678901L;
        long actual = NumberInput.parseLong(str.toCharArray(), 0, str.length());
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithStringInputInvalid() {
        String str = "123ABC456";
        NumberInput.parseLong(str.toCharArray(), 0, str.length()); // should throw NumberFormatException
    }

    @Test
    public void testParseIntWithStringInputValid() {
        String str = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(str.toCharArray(), 0, str.length());
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithStringInputInvalid() {
        String str = "1A2B3";
        NumberInput.parseInt(str.toCharArray(), 0, str.length()); // should throw NumberFormatException
    }

    @Test
    public void testParseIntWithStringInputNegative() {
        String str = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseInt(str.toCharArray(), 0, str.length());
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithStringInputNegative() {
        String str = "-12345678901";
        long expected = -12345678901L;
        long actual = NumberInput.parseLong(str.toCharArray(), 0, str.length());
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String str = "12345.6789";
        BigDecimal expected = new BigDecimal("12345.6789");
        BigDecimal actual = NumberInput.parseBigDecimal(str);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String str = "123ABC";
        NumberInput.parseBigDecimal(str.toCharArray()); // should throw NumberFormatException
    }

    @Test
    public void testParseBigDecimalWithValidInputCharArray() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '.', '6', '7', '8', '9'};
        BigDecimal expected = new BigDecimal("12345.6789");
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalWithValidInputCharArrayLongLength() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '.', '6', '7', '8', '9'};
        BigDecimal expected = new BigDecimal("12345.6789");
        BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length-1);
        assertEquals(expected, actual);
    }

}