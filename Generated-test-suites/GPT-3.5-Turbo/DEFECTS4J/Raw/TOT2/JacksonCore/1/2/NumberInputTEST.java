package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int result = NumberInput.parseInt(digitChars, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntEdgeCase() {
        char[] digitChars = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        int result = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0', '0', '0', '0', '0', '0', '0', '0'};
        long result = NumberInput.parseLong(digitChars, 0, 18);
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseLongEdgeCase() {
        char[] digitChars = {'-', '9', '2', '2', '5', '0', '7', '3', '8', '5', '8', '5', '0', '7', '2', '0', '1', '2'};
        long result = NumberInput.parseLong(digitChars, 0, 18);
        assertEquals(Long.MIN_VALUE, result);
    }

    @Test
    public void testParseIntFromString() {
        String str = "456";
        int result = NumberInput.parseInt(str);
        assertEquals(456, result);
    }

    @Test
    public void testParseLongFromString() {
        String str = "1234567890";
        long result = NumberInput.parseLong(str);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseAsInt() {
        String input = "789";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(789, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "456789";
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(456789L, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "3.14";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(3.14, result, 0.0);
    }

    @Test
    public void testParseAsDoubleErrorScenario() {
        String input = "abc";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testParseDouble() {
        String numStr = "3.14159";
        try {
            double result = NumberInput.parseDouble(numStr);
            assertEquals(3.14159, result, 0.0);
        } catch (NumberFormatException e) {
            fail("Failed to parse double value.");
        }
    }

    @Test
    public void testParseBigDecimal() {
        String numStr = "12345.6789";
        try {
            BigDecimal result = NumberInput.parseBigDecimal(numStr);
            assertEquals(new BigDecimal("12345.6789"), result);
        } catch (NumberFormatException e) {
            fail("Failed to parse BigDecimal value.");
        }
    }

    @Test
    public void testParseBigDecimalFromArray() {
        char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        try {
            BigDecimal result = NumberInput.parseBigDecimal(buffer);
            assertEquals(new BigDecimal("123456789"), result);
        } catch (NumberFormatException e) {
            fail("Failed to parse BigDecimal value.");
        }
    }

    @Test
    public void testParseBigDecimalFromArrayWithOffsetAndLength() {
        char[] buffer = {'9', '8', '7', ',', '6', '5', '4', '3', '2', '1'};
        try {
            BigDecimal result = NumberInput.parseBigDecimal(buffer, 2, 6);
            assertEquals(new BigDecimal("7.654321"), result);
        } catch (NumberFormatException e) {
            fail("Failed to parse BigDecimal value.");
        }
    }
}