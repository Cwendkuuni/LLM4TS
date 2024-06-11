package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharacterArray() {
        char[] digits1 = new char[]{'1', '2', '3', '4'};
        int expected = 1234;
        int result = NumberInput.parseInt(digits1, 0, 4);
        assertEquals(expected, result);

        char[] digits2 = new char[]{'1', '2'};
        int result2 = NumberInput.parseInt(digits2, 0, 2);
        assertEquals(12, result2);

        char[] digits3 = new char[]{'9'};
        int result3 = NumberInput.parseInt(digits3, 0, 1);
        assertEquals(9, result3);

        char[] digits4 = new char[]{'0'};
        int result4 = NumberInput.parseInt(digits4, 0, 1);
        assertEquals(0, result4);

        char[] digits5 = new char[]{'5', 'a'};
        int result5 = NumberInput.parseInt(digits5, 0, 2);
        assertNull(result5);

        char[] digits6 = new char[]{'-'};
        int result6 = NumberInput.parseInt(digits6, 0, 1);
        assertNull(result6);

        char[] digits7 = new char[]{'-'};
        int result7 = NumberInput.parseInt(digits7, 0, 1);
        assertNull(result7);

        char[] digits8 = new char[]{'-', '1'};
        int result8 = NumberInput.parseInt(digits8, 0, 2);
        assertEquals(-1, result8);
    }

    @Test
    public void testParseLongFromCharacterArray() {
        char[] digits1 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 12345678;
        long result = NumberInput.parseLong(digits1, 0, 8);
        assertEquals(expected, result);

        char[] digits2 = new char[]{'1', '2', '3'};
        long result2 = NumberInput.parseLong(digits2, 0, 3);
        assertEquals(123, result2);

        char[] digits3 = new char[]{'0'};
        long result3 = NumberInput.parseLong(digits3, 0, 1);
        assertEquals(0, result3);

        char[] digits4 = new char[]{'9'};
        long result4 = NumberInput.parseLong(digits4, 0, 1);
        assertEquals(9, result4);

        char[] digits5 = new char[]{'-'};
        long result5 = NumberInput.parseLong(digits5, 0, 1);
        assertNull(result5);

        char[] digits6 = new char[]{'-'};
        long result6 = NumberInput.parseLong(digits6, 0, 1);
        assertNull(result6);

        char[] digits7 = new char[]{'-', '1'};
        long result7 = NumberInput.parseLong(digits7, 0, 2);
        assertEquals(-1, result7);

        char[] digits8 = new char[]{'1', '2', '3', '4', '5', 'e'};
        long result8 = NumberInput.parseLong(digits8, 0, 6);
        assertNull(result8);

        char[] digits9 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'};
        long result9 = NumberInput.parseLong(digits9, 0, 11);
        assertNull(result9);
    }

    @Test
    public void testIsInLongRange() {
        char[] digits1 = new char[]{'1', '2', '3', '4'};
        boolean inLongRange = NumberInput.inLongRange(digits1, 0, 4, false);
        assertTrue(inLongRange);

        char[] digits2 = new char[]{'-', '1', '2', '3', '4'};
        boolean inLongRange2 = NumberInput.inLongRange(digits2, 0, 5, false);
        assertTrue(inLongRange2);

        char[] digits3 = new char[]{'1', '2', '3', '4', '5'};
        boolean inLongRange3 = NumberInput.inLongRange(digits3, 0, 5, true);
        assertFalse(inLongRange3);

        char[] digits4 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean inLongRange4 = NumberInput.inLongRange(digits4, 0, 10, false);
        assertTrue(inLongRange4);

        char[] digits5 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'};
        boolean inLongRange5 = NumberInput.inLongRange(digits5, 0, 11, false);
        assertFalse(inLongRange5);

        String numberStr1 = "1234";
        boolean inLongRange6 = NumberInput.inLongRange(numberStr1.toCharArray(), 0, 4, false);
        assertTrue(inLongRange6);

        String numberStr2 = "-1234";
        boolean inLongRange7 = NumberInput.inLongRange(numberStr2.toCharArray(), 0, 5, false);
        assertTrue(inLongRange7);

        String numberStr3 = "12345";
        boolean inLongRange8 = NumberInput.inLongRange(numberStr3.toCharArray(), 0, 5, true);
        assertFalse(inLongRange8);

        String numberStr4 = "12345678901234567890";
        boolean inLongRange9 = NumberInput.inLongRange(numberStr4.toCharArray(), 0, 21, false);
        assertTrue(inLongRange9);

        String numberStr5 = "12345678901234567890a";
        boolean inLongRange10 = NumberInput.inLongRange(numberStr5.toCharArray(), 0, 22, false);
        assertFalse(inLongRange10);
    }

    @Test
    public void testParseAsInt() {
        String input1 = "1234";
        int expected1 = 1234;
        int result1 = NumberInput.parseAsInt(input1, 0);
        assertEquals(expected1, result1);

        String input2 = "-1234";
        int expected2 = -1234;
        int result2 = NumberInput.parseAsInt(input2, 0);
        assertEquals(expected2, result2);

        String input3 = "1a";
        int expected3 = 0;
        int result3 = NumberInput.parseAsInt(input3, 0);
        assertEquals(expected3, result3);

        String input4 = "1234.5";
        int expected4 = 0;
        int result4 = NumberInput.parseAsInt(input4, 0);
        assertEquals(expected4, result4);

        String input5 = "1234e5";
        int expected5 = 0;
        int result5 = NumberInput.parseAsInt(input5, 0);
        assertEquals(expected5, result5);

        String input6 = "1234p5";
        int expected6 = 0;
        int result6 = NumberInput.parseAsInt(input6, 0);
        assertEquals(expected6, result6);
    }

    @Test
    public void testParseAsLong() {
        String input1 = "12345678901234567890";
        long expected1 = 12345678901234567890L;
        long result1 = NumberInput.parseAsLong(input1, 0);
        assertEquals(expected1, result1);

        String input2 = "-12345678901234567890";
        long expected2 = -12345678901234567890L;
        long result2 = NumberInput.parseAsLong(input2, 0);
        assertEquals(expected2, result2);

        String input3 = "1a";
        long expected3 = 0;
        long result3 = NumberInput.parseAsLong(input3, 0);
        assertEquals(expected3, result3);

        String input4 = "1234.5";
        long expected4 = 0;
        long result4 = NumberInput.parseAsLong(input4, 0);
        assertEquals(expected4, result4);

        String input5 = "1234e5";
        long expected5 = 0;
        long result5 = NumberInput.parseAsLong(input5, 0);
        assertEquals(expected5, result5);

        String input6 = "1234p5";
        long expected6 = 0;
        long result6 = NumberInput.parseAsLong(input6, 0);
        assertEquals(expected6, result6);

        String input7 = "12345678901234567890e5";
        long expected7 = 1234567890123456789000000L;
        long result7 = NumberInput.parseAsLong(input7, 0);
        assertEquals(expected7, result7);

        String input8 = "12345678901234567890p5";
        long expected8 = 12345678901234567890000000L;
        long result8 = NumberInput.parseAsLong(input8, 0);
        assertEquals(expected8, result8);
    }

    @Test
    public void testParseAsDouble() {
        String input1 = "1234.5";
        double expected1 = 1234.5;
        double result1 = NumberInput.parseAsDouble(input1, 0);
        assertEquals(expected1, result1, 0.0001);

        String input2 = "1234e5";
        double expected2 = 123400000.0;
        double result2 = NumberInput.parseAsDouble(input2, 0);
        assertEquals(expected2, result2, 0.0001);

        String input3 = "1234p5";
        double expected3 = 123400000000.0;
        double result3 = NumberInput.parseAsDouble(input3, 0);
        assertEquals(expected3, result3, 0.0001);
    }

    @Test
    public void testParseBigDecimalFromCharacterArray() {
        char[] digits1 = new char[]{'1', '2', '3', '4', '5', 'e', '5'};
        BigDecimal expected1 = new BigDecimal("12345E5");
        BigDecimal result1 = NumberInput.parseBigDecimal(digits1, 0, 7);
        assertEquals(expected1, result1);

        char[] digits2 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        BigDecimal expected2 = new BigDecimal("12345678");
        BigDecimal result2 = NumberInput.parseBigDecimal(digits2, 0, 8);
        assertEquals(expected2, result2);

        char[] digits3 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        BigDecimal expected3 = new BigDecimal("123456789");
        BigDecimal result3 = NumberInput.parseBigDecimal(digits3, 0, 9);
        assertEquals(expected3, result3);

        char[] digits4 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'};
        BigDecimal expected4 = null;
        BigDecimal result4 = NumberInput.parseBigDecimal(digits4, 0, 11);
        assertEquals(expected4, result4);
    }

    @Test
    public void testParseBigDecimalFromString() {
        String numStr1 = "1234.5";
        BigDecimal expected1 = new BigDecimal("1234.5");
        BigDecimal result1 = NumberInput.parseBigDecimal(numStr1);
        assertEquals(expected1, result1);

        String numStr2 = "1234e5";
        BigDecimal expected2 = new BigDecimal("123400000.0");
        BigDecimal result2 = NumberInput.parseBigDecimal(numStr2);
        assertEquals(expected2, result2);

        String numStr3 = "1234p5";
        BigDecimal expected3 = new BigDecimal("123400000000.0");
        BigDecimal result3 = NumberInput.parseBigDecimal(numStr3);
        assertEquals(expected3, result3);

        String numStr4 = "12345678901234567890";
        BigDecimal expected4 = new BigDecimal("12345678901234567890");
        BigDecimal result4 = NumberInput.parseBigDecimal(numStr4);
        assertEquals(expected4, result4);

        String numStr5 = "12345678901234567890a";
        BigDecimal expected5 = null;
        BigDecimal result5 = NumberInput.parseBigDecimal(numStr5);
        assertEquals(expected5, result5);
    }
}