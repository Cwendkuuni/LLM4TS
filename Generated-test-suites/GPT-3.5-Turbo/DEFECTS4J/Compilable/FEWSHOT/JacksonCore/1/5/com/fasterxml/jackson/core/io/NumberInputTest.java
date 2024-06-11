package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray_offset_len() {
        char[] digitChars = "1234567890".toCharArray();
        assertEquals(1, NumberInput.parseInt(digitChars, 0, 1));
        assertEquals(12, NumberInput.parseInt(digitChars, 0, 2));
        assertEquals(123, NumberInput.parseInt(digitChars, 0, 3));
        assertEquals(1234, NumberInput.parseInt(digitChars, 0, 4));
        assertEquals(12345, NumberInput.parseInt(digitChars, 0, 5));
        assertEquals(123456, NumberInput.parseInt(digitChars, 0, 6));
        assertEquals(1234567, NumberInput.parseInt(digitChars, 0, 7));
        assertEquals(12345678, NumberInput.parseInt(digitChars, 0, 8));
        assertEquals(123456789, NumberInput.parseInt(digitChars, 0, 9));
        assertEquals(1234567890, NumberInput.parseInt(digitChars, 0, 10));
    }

    @Test
    public void testParseInt_String() {
        assertEquals(123, NumberInput.parseInt("123"));
        assertEquals(-456, NumberInput.parseInt("-456"));
        assertEquals(789, NumberInput.parseInt("789"));
    }

    @Test
    public void testParseLong_charArray_offset_len() {
        char[] digitChars = "12345678901234567890".toCharArray();
        assertEquals(1234567890L, NumberInput.parseLong(digitChars, 0, 10));
        assertEquals(2345678901L, NumberInput.parseLong(digitChars, 1, 10));
    }

    @Test
    public void testParseLong_String() {
        assertEquals(1234567890L, NumberInput.parseLong("1234567890"));
        assertEquals(-2345678901L, NumberInput.parseLong("-2345678901"));
        assertEquals(3456789012L, NumberInput.parseLong("3456789012"));
    }

    @Test
    public void testInLongRange_charArray_offset_len_negative() {
        char[] digitChars = "1234567890".toCharArray();
        assertFalse(NumberInput.inLongRange(digitChars, 0, 10, true));
        assertFalse(NumberInput.inLongRange(digitChars, 0, 10, false));
        assertTrue(NumberInput.inLongRange(digitChars, 1, 9, true));
        assertTrue(NumberInput.inLongRange(digitChars, 1, 9, false));
    }

    @Test
    public void testInLongRange_String_negative() {
        assertFalse(NumberInput.inLongRange("1234567890", true));
        assertFalse(NumberInput.inLongRange("1234567890", false));
        assertTrue(NumberInput.inLongRange("2345678901", true));
        assertTrue(NumberInput.inLongRange("2345678901", false));
    }

    @Test
    public void testParseAsInt_String_int() {
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        assertEquals(456, NumberInput.parseAsInt("456", 0));
        assertEquals(7, NumberInput.parseAsInt("7", 0));
    }

    @Test
    public void testParseAsLong_String_long() {
        assertEquals(123L, NumberInput.parseAsLong("123", 0L));
        assertEquals(-456L, NumberInput.parseAsLong("-456", 0L));
        assertEquals(789L, NumberInput.parseAsLong("789", 0L));
    }

    @Test
    public void testParseAsDouble_String_double() {
        assertEquals(1.23, NumberInput.parseAsDouble("1.23", 0.0), 0.0001);
        assertEquals(-4.56, NumberInput.parseAsDouble("-4.56", 0.0), 0.0001);
        assertEquals(7.89, NumberInput.parseAsDouble("7.89", 0.0), 0.0001);
    }

    @Test
    public void testParseDouble_String() {
        assertEquals(1.23, NumberInput.parseDouble("1.23"), 0.0001);
        assertEquals(-4.56, NumberInput.parseDouble("-4.56"), 0.0001);
        assertEquals(7.89, NumberInput.parseDouble("7.89"), 0.0001);
    }

    @Test
    public void testParseBigDecimal_String() {
        assertEquals(new BigDecimal("1.23"),
                NumberInput.parseBigDecimal("1.23"));
        assertEquals(new BigDecimal("-4.56"),
                NumberInput.parseBigDecimal("-4.56"));
        assertEquals(new BigDecimal("7.89"),
                NumberInput.parseBigDecimal("7.89"));
    }

    @Test
    public void testParseBigDecimal_charArray_int_int() {
        char[] buffer1 = "123.45".toCharArray();
        char[] buffer2 = "-678.90".toCharArray();

        assertEquals(new BigDecimal("123.45"),
                NumberInput.parseBigDecimal(buffer1, 0, 6));
        assertEquals(new BigDecimal("-678.90"),
                NumberInput.parseBigDecimal(buffer2, 0, 8));
    }
}