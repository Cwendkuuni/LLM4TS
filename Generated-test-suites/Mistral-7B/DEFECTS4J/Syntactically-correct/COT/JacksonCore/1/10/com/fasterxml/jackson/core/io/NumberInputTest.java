package com.example.junit; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] validInput = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(validInput, 0, validInput.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithValidInputFromString() {
        String validInput = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(validInput);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithNegativeValidInputFromString() {
        String validInput = "-12345";
        int expected = -12345;
        int actual = NumberInput.parseInt(validInput);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithInvalidInput() {
        char[] invalidInput = {'x', '2', '3', '4'};
        int actual = NumberInput.parseInt(invalidInput, 0, invalidInput.length);
        assertEquals(0, actual);
    }

    @Test
    public void testParseIntWithInvalidInputFromString() {
        String invalidInput = "x234";
        int actual = NumberInput.parseInt(invalidInput);
        assertEquals(0, actual);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] longInput = {'1', '2', '3', '4'};
        long expected = 1234;
        long actual = NumberInput.parseLong(longInput, 0, longInput.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithValidInputFromString() {
        String longInput = "1234";
        long expected = 1234;
        long actual = NumberInput.parseLong(longInput);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithNegativeValidInputFromString() {
        String longInput = "-1234";
        long expected = -1234;
        long actual = NumberInput.parseLong(longInput);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithInvalidInput() {
        char[] invalidInput = {'x', '2', '3', '4'};
        long actual = NumberInput.parseLong(invalidInput, 0, invalidInput.length);
        assertEquals(0, actual);
    }

    @Test
    public void testParseLongWithInvalidInputFromString() {
        String invalidInput = "x234";
        long actual = NumberInput.parseLong(invalidInput);
        assertEquals(0, actual);
    }

    @Test
    public void testParseLongWithMaxValueInput() {
        char[] maxValueInput = NumberInput.MAX_LONG_STR.toCharArray();
        long actual = NumberInput.parseLong(maxValueInput, 0, maxValueInput.length);
        assertEquals(Long.MAX_VALUE, actual);
    }

    @Test
    public void testParseLongWithMinusMaxValueInput() {
        char[] minusMaxValueInput = {'-', NumberInput.MIN_LONG_STR.charAt(0), NumberInput.MIN_LONG_STR.substring(1).toCharArray()};
        long actual =  NumberInput.parseLong(minusMaxValueInput, 0, minusMaxValueInput.length);
        assertEquals(Long.MIN_VALUE, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInputBiggerThanLongType() {
        char[] inputBiggerThanLongType = NumberInput.NASTY_SMALL_DOUBLE.toCharArray();
        NumberInput.parseLong(inputBiggerThanLongType, 0, inputBiggerThanLongType.length);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInputFromString() {
        String invalidInput = "x234";
        NumberInput.parseLong(invalidInput);
    }

    @Test
    public void testInLongRangeWithValidInput() {
        char[] validInput = {'1', '2', '3', '4'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(validInput, 0, validInput.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithMaxValueInput() {
        char[] maxValueInput = NumberInput.MAX_LONG_STR.toCharArray();
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(maxValueInput, 0, maxValueInput.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithMinusMaxValueInput() {
        char[] minusMaxValueInput = {'-', NumberInput.MIN_LONG_STR_NO_SIGN.charAt(0), NumberInput.MIN_LONG_STR_NO_SIGN.substring(1).toCharArray()};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(minusMaxValueInput, 0, minusMaxValueInput.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithInputBiggerThanLongType() {
        char[] inputBiggerThanLongType = NumberInput.NASTY_SMALL_DOUBLE.toCharArray();
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(inputBiggerThanLongType, 0, inputBiggerThanLongType.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String validInput = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(validInput, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String validInput = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt("", 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String invalidInput = "x234";
        int expected = 0;
        int actual = NumberInput.parseAsInt(invalidInput, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntWithInvalidInputThrowsException() {
        String invalidInput = "x234";
        NumberInput.parseAsInt(invalidInput, 0);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String validInput = "12345678901234567890";
        long expected = 12345678901234567890L;
        long actual = NumberInput.parseAsLong(validInput, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String validInput = "12345678901234567890";
        long expected = 12345678901234567890L;
        long actual = NumberInput.parseAsLong("", 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String invalidInput = "x234";
        long expected = 0;
        long actual = NumberInput.parseAsLong(invalidInput, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsLongWithInvalidInputThrowsException() {
        String invalidInput = "x234";
        NumberInput.parseAsLong(invalidInput, 0);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String validInput = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(validInput, 0.0);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidInputThrowsException() {
        String invalidInput = "x234";
        NumberInput.parseAsDouble(invalidInput, 0.0);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String validInput = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(validInput);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInputThrowsException() {
        String invalidInput = "x234";
        NumberInput.parseBigDecimal(invalidInput);
    }

    @Test
    public void testParseBigDecimalWithValidInputFromCharArray() {
        char[] validInput = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(validInput, 0, validInput.length);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInputFromCharArrayThrowsException() {
        char[] invalidInput = {'x', '2', '3', '.', '4', '5'};
        NumberInput.parseBigDecimal(invalidInput, 0, invalidInput.length);
    }

}