
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import org.junit.Assert;
import java.math.BigDecimal;

public class NumberInputTest {

    @Test
    public void testNASTY_SMALL_DOUBLE() {
        String expected = "2.2250738585072012e-308";
        String actual = NumberInput.NASTY_SMALL_DOUBLE;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseIntCharArrayIntInt() {
        int expected = 12345;
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int offset = 0;
        int len = digitChars.length;
        int actual = NumberInput.parseInt(digitChars, offset, len);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseIntString() {
        int expected = 54321;
        String str = "54321";
        int actual = NumberInput.parseInt(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseLongCharArrayIntInt() {
        long expected = 123456789012345678L;
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8'};
        int offset = 0;
        int len = digitChars.length;
        long actual = NumberInput.parseLong(digitChars, offset, len);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseLongString() {
        long expected = 876543210987654321L;
        String str = "876543210987654321";
        long actual = NumberInput.parseLong(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeCharArrayIntIntBoolean() {
        boolean expected = true;
        char[] digitChars = {'0', '1', '2'};
        int offset = 0;
        int len = digitChars.length;
        boolean negative = false;
        boolean actual = NumberInput.inLongRange(digitChars, offset, len, negative);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeStringBoolean() {
        boolean expected = false;
        String numberStr = "123456789123456789";
        boolean negative = false;
        boolean actual = NumberInput.inLongRange(numberStr, negative);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntStringInt() {
        int expected = 98765;
        String input = "98765";
        int defaultValue = -1;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongStringLong() {
        long expected = 5678901234567890L;
        String input = "5678901234567890";
        long defaultValue = -1;
        long actual = NumberInput.parseAsLong(input, defaultValue);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleStringDouble() {
        double expected = 1.23456789;
        String input = "1.23456789";
        double defaultValue = -1;
        double actual = NumberInput.parseAsDouble(input, defaultValue);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseDouble() throws NumberFormatException {
        double expected = 3.14159;
        String numStr = "3.14159";
        double actual = NumberInput.parseDouble(numStr);
        Assert.assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testParseBigDecimalString() throws NumberFormatException {
        BigDecimal expected = new BigDecimal("1234567890");
        String numStr = "1234567890";
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalCharArray() throws NumberFormatException {
        BigDecimal expected = new BigDecimal("9876543210");
        char[] buffer = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBigDecimalCharArrayIntInt() throws NumberFormatException {
        BigDecimal expected = new BigDecimal("3456789012");
        char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int offset = 2;
        int len = 10;
        BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, len);
        Assert.assertEquals(expected, actual);
    }
}

