package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int result;

        // Test valid integers
        result = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(1234567890, result);

        // Test valid integers with negative sign
        digitChars[0] = '-';
        result = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(-1234567890, result);

        // Test invalid integers
        digitChars[10] = 'X';
        assertFalse(NumberInput.inLongRange(digitChars, 0, 10, false));

        // Test long integers
        result = NumberInput.parseLong(digitChars, 0, 19);
        assertEquals(-1234567890123456789L, result);

        // Test long integers with negative sign
        digitChars[0] = '-';
        result = NumberInput.parseLong(digitChars, 0, 19);
        assertEquals(1234567890123456789L, result);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result;

        // Test valid long integers
        result = NumberInput.parseLong(digitChars, 0, 19);
        assertEquals(1234567890123456789L, result);

        // Test invalid long integers
        digitChars[19] = 'X';
        assertFalse(NumberInput.inLongRange(digitChars, 0, 19, false));

        // Test long integers with negative sign
        digitChars[0] = '-';
        result = NumberInput.parseLong(digitChars, 0, 19);
        assertEquals(-1234567890123456789L, result);

        // Test long integer parsing using String
        String input = "1234567890123456789";
        result = NumberInput.parseLong(input);
        assertEquals(1234567890123456789L, result);

        // Test long integer parsing with negative sign using String
        input = "-1234567890123456789";
        result = NumberInput.parseLong(input);
        assertEquals(-1234567890123456789L, result);
    }

    @Test
    public void testParseBigDecimal() {
        String numStr = "1234567890.123456";
        BigDecimal result;

        // Test valid BigDecimal numbers
        result = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("1234567890.123456"), result);

        // Test invalid BigDecimal numbers
        numStr = "abc";
        assertNull(NumberInput.parseBigDecimal(numStr));

        // Test BigDecimal numbers with negative sign
        numStr = "-1234567890.123456";
        result = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("-1234567890.123456"), result);

        // Test BigDecimal numbers using char array
        char[] buffer = new char[21];
        numStr.getChars(0, numStr.length(), buffer, 0);
        result = NumberInput.parseBigDecimal(buffer, 0, numStr.length());
        assertEquals(new BigDecimal("1234567890.123456"), result);
    }

    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double result;

        // Test valid doubles
        result = NumberInput.parseAsDouble(numStr, 0.0);
        assertEquals(123.45, result, 0.001);

        // Test invalid doubles
        numStr = "abc";
        assertNull(NumberInput.parseAsDouble(numStr, 0.0));

        // Test doubles with negative sign
        numStr = "-123.45";
        result = NumberInput.parseAsDouble(numStr, 0.0);
        assertEquals(-123.45, result, 0.001);

        // Test doubles using parseDouble method
        try {
            result = NumberInput.parseDouble("123.45");
            assertEquals(123.45, result, 0.001);
        } catch (NumberFormatException e) {
            fail("Unexpected NumberFormatException");
        }

        // Test doubles using parseDouble method with negative sign
        try {
            result = NumberInput.parseDouble("-123.45");
            assertEquals(-123.45, result, 0.001);
        } catch (NumberFormatException e) {
            fail("Unexpected NumberFormatException");
        }
    }
}