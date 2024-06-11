package com.yourpackagename.test;

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
    public void testParseIntWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int offset = 0;
        int len = 5;

        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(12345, result);
    }

    @Test
    public void testParseIntWithMinusSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int offset = 0;
        int len = 6;

        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(-12345, result);
    }

    @Test
    public void testParseIntWithValidStringInput() {
        String input = "12345";

        int result = NumberInput.parseInt(input);
        assertEquals(12345, result);
    }

    @Test
    public void testParseIntWithLongInput() {
        String input = "123456789012345";

        int result = NumberInput.parseInt(input);
        assertEquals(123456789012345, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', 'x', '3', '4', '5'};
        int offset = 0;
        int len = 6;

        NumberInput.parseInt(digitChars, offset, len);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNegativeInputLength() {
        char[] digitChars = new char[]{'1'};
        int offset = 0;
        int len = -1;

        NumberInput.parseInt(digitChars, offset, len);
    }

    @Test
    public void testParseLongWithValidInputs() {
        char[] digitChars = new char[]{'1', '2', '3'};
        int offset = 0;
        int len = 3;

        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(123L, result);
    }

    @Test
    public void testParseLongWithNegativeSign() {
        char[] digitChars = new char[]{'-', '1', '2', '3'};
        int offset = 0;
        int len = 4;

        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(-123L, result);
    }

    @Test
    public void testParseLongWithValidStringInput() {
        String input = "123";

        long result = NumberInput.parseLong(input);
        assertEquals(123L, result);
    }

    @Test
    public void testParseLongWithBigInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0;
        int len = 10;

        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(1234567890L, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'1', '2', 'x', '3'};
        int offset = 0;
        int len = 4;

        NumberInput.parseLong(digitChars, offset, len);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithNegativeInputLength() {
        char[] digitChars = new char[]{'1'};
        int offset = 0;
        int len = -1;

        NumberInput.parseLong(digitChars, offset, len);
    }

    @Test
    public void testParseLongWithBigInputOutOfRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        int offset = 0;
        int len = 11;

        assertFalse(NumberInput.inLongRange(digitChars, offset, len, false));
    }

    @Test
    public void testParseLongWithBigInputInRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int offset = 0;
        int len = 10;

        assertTrue(NumberInput.inLongRange(digitChars, offset, len, false));
    }

    @Test
    public void testParseLongWithNegativeNumber() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int offset = 0;
        int len = 6;

        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(-12345, result);
    }

    @Test
    public void testParseLongWithParseInt() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int offset = 0;
        int len = 5;

        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(12345L, result);

        int intermediateResult = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(12345, intermediateResult);
    }

    @Test
    public void testParseBigDecimalWithValidInputs() {
        String input = "12345.67";

        BigDecimal result = NumberInput.parseBigDecimal(input);
        BigDecimal expected = new BigDecimal("12345.67");
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidCharsInput() {
        char[] buffer = new char[]{'1', '2', '3'};

        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, buffer.length);
        BigDecimal expected = new BigDecimal("123");
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalWithValidLongInput() {
        long num = 123456789012345L;

        BigDecimal result = NumberInput.parseBigDecimal(NumberInput.parseInt(String.valueOf(num), 0, String.valueOf(num).length()));
        BigDecimal expected = new BigDecimal(Long.toString(num));
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        char[] buffer = new char[]{'1', '2', 'x', '3'};
        int offset = 0;
        int len = 4;

        NumberInput.parseBigDecimal(buffer, offset, len);
    }

    @Test
    public void testParseDoubleWithValidInputs() {
        String input = "123.45";

        double result = NumberInput.parseAsDouble(input);
        assertEquals(123.45, result, 0.00001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInput() {
        String input = "abc";

        NumberInput.parseAsDouble(input);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithNastyInput() {
        String input = NumberInput.NASTY_SMALL_DOUBLE;

        assertThrows(NumberFormatException.class, () -> NumberInput.parseAsDouble(input));
    }
}