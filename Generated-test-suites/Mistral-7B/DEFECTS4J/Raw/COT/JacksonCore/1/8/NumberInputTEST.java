package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int len = digitChars.length;
        int expected = 123;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithNegativeInput() {
        char[] digitChars = new char[]{'-', '1', '2', '3'};
        int len = digitChars.length;
        int expected = -123;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithLongInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int len = digitChars.length;
        int expected = 123456789;
        int actual = NumberInput.parseInt(digitChars, 0, len);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'a', '1', '2'};
        int len = digitChars.length;
        NumberInput.parseInt(digitChars, 0, len);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int len1 = len1 - 9;
        long expected = 123L * NumberInput.L_BILLION;
        long actual = NumberInput.parseLong(digitChars, 0, len1);
        assertEquals(expected, actual);

        long num2 = 456789L;
        char[] digitChars2 = new char[18];
        System.arraycopy(NumberInput.MIN_LONG_STR.toCharArray(), 0, digitChars2, 0, 10);
        System.arraycopy(new Character[]{'(', '0', '0'}, 0, digitChars2, 10, 3);
        System.arraycopy(new Character[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}, 0, digitChars2, 13, 9);
        long actual2 = NumberInput.parseLong(digitChars2, 0, digitChars2.length);
        assertEquals(expected + num2, actual2);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'a', '1', '2'};
        int len = digitChars.length;
        NumberInput.parseLong(digitChars, 0, len);
    }

    @Test
    public void testParseLongWithLongInputBeyondIntRange() {
        char[] digitChars = new char[19];
        System.arraycopy(NumberInput.MAX_LONG_STR.toCharArray(), 0, digitChars, 0, 18);
        digitChars[18] = '1';
        Boolean expected = true;
        Boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithLongInputWithinIntRange() {
        char[] digitChars = new char[10];
        System.arraycopy(NumberInput.MAX_LONG_STR.toCharArray(), 0, digitChars, 0, 10);
        Boolean expected = true;
        Boolean actual = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithStringInput() {
        String str = "123";
        long expected = 123L;
        long actual = NumberInput.parseLong(str.toCharArray(), 0, str.length());
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithStringInputBeyondLongRange() {
        String str = "9223372036854775808";
        Boolean expected = true;
        Boolean actual = NumberInput.inLongRange(str.toCharArray(), 0, str.length(), false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithStringInputWithinLongRange() {
        String str = "1234567890123456789";
        Boolean expected = true;
        Boolean actual = NumberInput.inLongRange(str.toCharArray(), 0, str.length(), false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "123";
        int defaultValue = 0;
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "abcd";
        int defaultValue = 0;
        int expected = defaultValue;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = null;
        int defaultValue = 0;
        int expected = defaultValue;
        int actual = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsIntWithNegativeInput() {
        String input = "-123";
        int defaultValue = 0;
        NumberInput.parseAsInt(input, defaultValue);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "123";
        long defaultValue = 0L;
        long expected = 123;
        long actual = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "abcd";
        long defaultValue = 0L;
        long expected = defaultValue;
        long actual = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = null;
        long defaultValue = 0L;
        long expected = defaultValue;
        long actual = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double defaultValue = 0.0;
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "abcd";
        double defaultValue = 0.0;
        double expected = defaultValue;
        double actual = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = null;
        double defaultValue = 0.0;
        double expected = defaultValue;
        double actual = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testParseDoubleWithValidInput() {
        String numStr = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseDouble(numStr);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInput() {
        String numStr = "abcd";
        NumberInput.parseDouble(numStr);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        char[] buffer = new char[]{'a', 'b'};
        NumberInput.parseBigDecimal(buffer);
    }

    @Test
    public void testParseBigDecimalWithBigInteger() {
        char[] buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', '0', '1'};
        BigInteger expected = new BigInteger("123456789012345678901");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, actual.toBigIntegerExact());
    }

}