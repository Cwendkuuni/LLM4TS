package com.fasterxml.jackson.core.io;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidInput() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int expected = 12345;
        int actual = NumberInput.parseInt(validDigits, 0, validDigits.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_ValidInputWithOffset() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(validDigits, 1, validDigits.length - 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_ValidInputWithLength() {
        char[] validDigits = new char[]{'1', '2', '3', '4'};
        int expected = 1234;
        int actual = NumberInput.parseInt(validDigits, 0, 4);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_InvalidInput() {
        char[] invalidDigits = new char[]{'a', 'b', 'c'};
        int expected = 0;
        int actual = NumberInput.parseInt(invalidDigits, 0, invalidDigits.length);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_InvalidInput_ThrowsException() {
        char[] invalidDigits = new char[]{'a', 'b', 'c'};
        NumberInput.parseInt(invalidDigits, 0, invalidDigits.length);
    }

    @Test
    public void testParseLong_ValidInput() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long expected = 1234567890123L;
        long actual = NumberInput.parseLong(validDigits, 0, validDigits.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_ValidInputWithOffset() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 1234567890123L;
        long actual = NumberInput.parseLong(validDigits, 0, validDigits.length - 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_ValidInputWithLength() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5', '6'};
        long expected = 123456;
        long actual = NumberInput.parseLong(validDigits, 0, 6);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_LongRange() {
        char[] longRangeDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(longRangeDigits, false);
        Assert.assertEquals(expected, actual);

        char[] negativeLongRangeDigits = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        expected = true;
        actual = NumberInput.inLongRange(negativeLongRangeDigits, true);
        Assert.assertEquals(expected, actual);

        char[] longRangeExceedsDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        expected = false;
        actual = NumberInput.inLongRange(longRangeExceedsDigits, false);
        Assert.assertEquals(expected, actual);

        char[] negativeLongRangeExceedsDigits = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        expected = false;
        actual = NumberInput.inLongRange(negativeLongRangeExceedsDigits, true);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidInput_ThrowsException() {
        char[] invalidDigits = new char[]{'a', 'b', 'c'};
        NumberInput.parseLong(invalidDigits, 0, invalidDigits.length);
    }

    @Test
    public void testParseBigDecimal_ValidInput() {
        String validNumStr = "1234567890.123456";
        BigDecimal expected = new BigDecimal(validNumStr);
        BigDecimal actual = NumberInput.parseBigDecimal(validNumStr);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimal_ValidInputWithBuffer() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '1', '2', '3', '4', '5'};
        BigDecimal expected = new BigDecimal("1234567890.123456");
        BigDecimal actual = NumberInput.parseBigDecimal(validDigits, 0, validDigits.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimal_ValidInputWithOffset() {
        char[] validDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '1', '2', '3', '4', '5'};
        BigDecimal expected = new BigDecimal("1234567890.123456");
        BigDecimal actual = NumberInput.parseBigDecimal(validDigits, 0, validDigits.length - 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimal_InvalidInput() {
        char[] invalidDigits = new char[]{'a', 'b', 'c'};
        try {
            NumberInput.parseBigDecimal(invalidDigits, 0, invalidDigits.length);
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseBigDecimal_InvalidInputWithBuffer() {
        char[] invalidDigits = new char[]{'a', 'b', 'c', '.'};
        try {
            NumberInput.parseBigDecimal(invalidDigits, 0, invalidDigits.length);
        } catch (NumberFormatException e) {
            // expected
        }
    }
}