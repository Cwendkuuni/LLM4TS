package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidInts() {
        char[] intDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int len = intDigits.length;
        for (int offset = 0; offset < len; offset++) {
            int result = NumberInput.parseInt(intDigits, offset, len - offset);
            assertEquals(Integer.parseInt(new String(intDigits, offset, len - offset)), result);
        }
    }

    @Test
    public void testParseInt_EmptyArray() {
        int result = NumberInput.parseInt(new char[0], 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseInt_InvalidInput() {
        char[] input = new char[]{'1', '0', 'x'};
        try {
            NumberInput.parseInt(input, 0, input.length);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLong_ValidLongs() {
        char[] longDigits1 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] longDigits2 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        int len1 = longDigits1.length;
        int len2 = longDigits2.length;
        long expected1 = Long.parseLong(new String(longDigits1));
        long expected2 = Long.parseLong(new String(longDigits2)) * 10L;
        for (int offset = 0; offset < len1; offset++) {
            long result = NumberInput.parseLong(longDigits1, offset, len1 - offset);
            assertEquals(expected1 / (long) Math.pow(10, len1 - offset - 1), result);
        }
        for (int offset = 0; offset < len2; offset++) {
            long result = NumberInput.parseLong(longDigits2, offset, len2 - offset);
            assertEquals(expected2 / (long) Math.pow(10, len2 - offset - 1), result);
        }
    }

    @Test
    public void testParseLong_EmptyArray() {
        long result = NumberInput.parseLong(new char[0], 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseLong_InvalidInput() {
        char[] input = new char[]{'1', '0', 'x'};
        try {
            NumberInput.parseLong(input, 0, input.length);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLongInRange() {
        char[] bigNumber = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean result = NumberInput.inLongRange(bigNumber, false);
        assertTrue(result);

        char[] smallNumber = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean result2 = NumberInput.inLongRange(smallNumber, false);
        assertFalse(result2);

        char[] negativeBigNumber = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean result3 = NumberInput.inLongRange(negativeBigNumber, true);
        assertTrue(result3);

        char[] negativeSmallNumber = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        boolean result4 = NumberInput.inLongRange(negativeSmallNumber, true);
        assertFalse(result4);
    }

    @Test
    public void testParseAsInt() {
        String input1 = "123";
        int result1 = NumberInput.parseAsInt(input1, 0);
        assertEquals(123, result1);

        String input2 = " 123";
        int result2 = NumberInput.parseAsInt(input2, 0);
        assertEquals(123, result2);

        String input3 = " 123 ";
        int result3 = NumberInput.parseAsInt(input3, 0);
        assertEquals(123, result3);

        String input4 = "-123";
        int result4 = NumberInput.parseAsInt(input4, 0);
        assertEquals(-123, result4);

        String input5 = "123x";
        int result5 = NumberInput.parseAsInt(input5, 0);
        assertEquals(0, result5);

        String input6 = "123.5";
        int result6 = NumberInput.parseAsInt(input6, 0);
        assertEquals(0, result6);
    }

    @Test
    public void testParseAsLong() {
        String input1 = "123";
        long result1 = NumberInput.parseAsLong(input1, 0);
        assertEquals(123, result1);

        String input2 = " 123";
        long result2 = NumberInput.parseAsLong(input2, 0);
        assertEquals(123, result2);

        String input3 = " 123 ";
        long result3 = NumberInput.parseAsLong(input3, 0);
        assertEquals(123, result3);

        String input4 = "-123";
        long result4 = NumberInput.parseAsLong(input4, 0);
        assertEquals(-123, result4);

        String input5 = "123x";
        long result5 = NumberInput.parseAsLong(input5, 0);
        assertEquals(0, result5);

        String input6 = "123.5";
        long result6 = NumberInput.parseAsLong(input6, 0);
        assertEquals(0, result6);

        String input7 = "12345678901234567890";
        long result7 = NumberInput.parseAsLong(input7, 0);
        assertEquals(12345678901234567890, result7);

        String input8 = "-12345678901234567890";
        long result8 = NumberInput.parseAsLong(input8, 0);
        assertEquals(-12345678901234567890, result8);

        String input9 = "123e3";
        long result9 = NumberInput.parseAsLong(input9, 0);
        assertEquals(123000000, result9);

        String input10 = "123e+3";
        long result10 = NumberInput.parseAsLong(input10, 0);
        assertEquals(123000000, result10);
    }

    @Test
    public void testParseAsDouble() {
        String input1 = "123";
        double result1 = NumberInput.parseAsDouble(input1, 0);
        assertEquals(123, result1, 0.0);

        String input2 = " 123";
        double result2 = NumberInput.parseAsDouble(input2, 0);
        assertEquals(123, result2, 0.0);

        String input3 = " 123 ";
        double result3 = NumberInput.parseAsDouble(input3, 0);
        assertEquals(123, result3, 0.0);

        String input4 = "-123";
        double result4 = NumberInput.parseAsDouble(input4, 0);
        assertEquals(-123, result4, 0.0);

        String input5 = "123x";
        double result5 = NumberInput.parseAsDouble(input5, 0);
        assertEquals(0, result5, 0.0);

        String input6 = "123.5";
        double result6 = NumberInput.parseAsDouble(input6, 0);
        assertEquals(123.5, result6, 0.0);

        String input7 = "123e3";
        double result7 = NumberInput.parseAsDouble(input7, 0);
        assertEquals(123000.0, result7, 0.0);

        String input8 = "123e+3";
        double result8 = NumberInput.parseAsDouble(input8, 0);
        assertEquals(123000.0, result8, 0.0);

        String input9 = "123e-3";
        double result9 = NumberInput.parseAsDouble(input9, 0);
        assertEquals(0.0123, result9, 0.0);

        String input10 = "123e+3.5";
        double result10 = NumberInput.parseAsDouble(input10, 0);
        assertEquals(123000.5, result10, 0.0);
    }

    @Test
    public void testParseBigDecimal() {
        String input1 = "123";
        BigDecimal result1 = NumberInput.parseBigDecimal(input1);
        BigDecimal expected1 = new BigDecimal("123");
        assertEquals(expected1, result1);

        String input2 = "-123";
        BigDecimal result2 = NumberInput.parseBigDecimal(input2);
        BigDecimal expected2 = new BigDecimal("-123");
        assertEquals(expected2, result2);

        String input3 = "123.5";
        BigDecimal result3 = NumberInput.parseBigDecimal(input3);
        BigDecimal expected3 = new BigDecimal("123.5");
        assertEquals(expected3, result3);

        String input4 = "123e3";
        BigDecimal result4 = NumberInput.parseBigDecimal(input4);
        BigDecimal expected4 = new BigDecimal("123000000");
        assertEquals(expected4, result4);

        String input5 = "123e+3";
        BigDecimal result5 = NumberInput.parseBigDecimal(input5);
        BigDecimal expected5 = new BigDecimal("123000000");
        assertEquals(expected5, result5);

        String input6 = "123e-3";
        BigDecimal result6 = NumberInput.parseBigDecimal(input6);
        BigDecimal expected6 = new BigDecimal("0.0123");
        assertEquals(expected6, result6);

        String input7 = "123e+3.5";
        BigDecimal result7 = NumberInput.parseBigDecimal(input7);
        BigDecimal expected7 = new BigDecimal("123000500");
        assertEquals(expected7, result7);

        char[] input8 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal result8 = NumberInput.parseBigDecimal(input8, 0, input8.length);
        BigDecimal expected8 = new BigDecimal(new String(input8));
        assertEquals(expected8, result8);
    }

}