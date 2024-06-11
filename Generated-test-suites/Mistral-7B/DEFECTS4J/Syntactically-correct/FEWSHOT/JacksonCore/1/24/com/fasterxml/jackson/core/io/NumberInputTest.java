package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digits1 = {'1', '2', '3', '4', '5'};
        assertEquals(12345, NumberInput.parseInt(digits1, 0, 5));

        char[] digits2 = {'5', '4', '3', '2', '1'};
        assertEquals(12345, NumberInput.parseInt(digits2, 0, 5));

        char[] digits3 = {'1', '2'};
        assertEquals(12, NumberInput.parseInt(digits3, 0, 2));

        char[] digits4 = {'0'};
        assertEquals(0, NumberInput.parseInt(digits4, 0, 1));

        char[] digits5 = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertEquals(1111111111111, NumberInput.parseInt(digits5, 0, 10));

        char[] digits6 = {'0', '-'};
        assertNull(NumberInput.parseInt(digits6, 0, 2));

        char[] digits7 = {'-'};
        assertNull(NumberInput.parseInt(digits7, 0, 1));

        char[] digits8 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-'};
        assertFalse(NumberInput.inLongRange(digits8, false));
    }

    @Test
    public void testParseIntFromString() {
        String numStr1 = "12345";
        assertEquals(12345, NumberInput.parseInt(numStr1));

        String numStr2 = "54321";
        assertEquals(12345, NumberInput.parseInt(numStr2));

        String numStr3 = "12";
        assertEquals(12, NumberInput.parseInt(numStr3));

        String numStr4 = "0";
        assertEquals(0, NumberInput.parseInt(numStr4));

        String numStr5 = "1111111111111";
        assertEquals(1111111111111, NumberInput.parseInt(numStr5));

        String numStr6 = "-12345";
        assertEquals(-12345, NumberInput.parseInt(numStr6));

        String numStr7 = "-0";
        assertEquals(0, NumberInput.parseInt(numStr7));

        String numStr8 = "123456789012345";
        assertFalse(NumberInput.inLongRange(numStr8, false));
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digits1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result = NumberInput.parseLong(digits1, 0, 9);
        assertEquals(123456789, result);

        char[] digits2 = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        long result2 = NumberInput.parseLong(digits2, 0, 10);
        assertEquals(1111111111111, result2);

        char[] digits3 = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseLong(digits3, 0, 3));

        char[] digits4 = {'0'};
        assertNull(NumberInput.parseLong(digits4, 0, 1));

        char[] digits5 = {'9'};
        assertEquals(9L, NumberInput.parseLong(digits5, 0, 1));

        char[] digits6 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        assertFalse(NumberInput.inLongRange(digits6, false));
    }

    @Test
    public void testParseLongFromString() {
        String numStr1 = "123456789";
        long result = NumberInput.parseLong(numStr1);
        assertEquals(123456789, result);

        String numStr2 = "1111111111111";
        long result2 = NumberInput.parseLong(numStr2);
        assertEquals(1111111111111, result2);

        String numStr3 = "123";
        assertEquals(123, NumberInput.parseLong(numStr3));

        String numStr4 = "0";
        assertEquals(0, NumberInput.parseLong(numStr4));

        String numStr5 = "1";
        assertEquals(1, NumberInput.parseLong(numStr5));

        String numStr6 = "123456789012345";
        assertFalse(NumberInput.inLongRange(numStr6, false));
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] digits1 = {'1', '2', '3', '4', '5', '.'};
        BigDecimal result = NumberInput.parseBigDecimal(digits1, 0, 6);
        assertEquals(new BigDecimal("1.2345"), result);

        char[] digits2 = {'1', '2', '3', '4', '5'};
        BigDecimal result2 = NumberInput.parseBigDecimal(digits2, 0, 5);
        assertEquals(new BigDecimal("12345"), result2);

        char[] digits3 = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        BigDecimal result3 = NumberInput.parseBigDecimal(digits3, 0, 12);
        assertEquals(new BigDecimal("111111111111111111111111111111"), result3);

        char[] digits4 = {'0'};
        assertNull(NumberInput.parseBigDecimal(digits4, 0, 1));

        char[] digits5 = {'9'};
        assertEquals(new BigDecimal("9"), NumberInput.parseBigDecimal(digits5, 0, 1));

        char[] digits6 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '1'};
        assertFalse(NumberInput.inLongRange(digits6, false));
    }

    @Test
    public void testParseBigDecimalFromString() {
        String numStr1 = "123.45";
        BigDecimal result = NumberInput.parseBigDecimal(numStr1);
        assertEquals(new BigDecimal("123.45"), result);

        String numStr2 = "12345";
        BigDecimal result2 = NumberInput.parseBigDecimal(numStr2);
        assertEquals(new BigDecimal("12345"), result2);

        String numStr3 = "111111111111111111111111111111";
        BigDecimal result3 = NumberInput.parseBigDecimal(numStr3);
        assertEquals(new BigDecimal("111111111111111111111111111111"), result3);

        String numStr4 = "0";
        assertNull(NumberInput.parseBigDecimal(numStr4));

        String numStr5 = "1";
        BigDecimal result5 = NumberInput.parseBigDecimal(numStr5);
        assertEquals(new BigDecimal("1"), result5);

        String numStr6 = "123456789012345";
        assertFalse(NumberInput.inLongRange(numStr6, false));
    }
}