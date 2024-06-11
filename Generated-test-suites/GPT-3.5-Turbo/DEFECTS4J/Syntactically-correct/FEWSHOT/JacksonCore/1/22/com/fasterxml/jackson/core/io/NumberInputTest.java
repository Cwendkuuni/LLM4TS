package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
    }

    @Test
    public void testParseIntUsingCharArray() {
        char[] digitChars = "123456789".toCharArray();
        assertEquals(123456789, NumberInput.parseInt(digitChars, 0, digitChars.length));
    }

    @Test
    public void testParseLong() {
        assertEquals(0L, NumberInput.parseLong("0"));
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        assertEquals(-123456789L, NumberInput.parseLong("-123456789"));
    }

    @Test
    public void testParseLongUsingCharArray() {
        char[] digitChars = "123456789".toCharArray();
        assertEquals(123456789L, NumberInput.parseLong(digitChars, 0, digitChars.length));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(0.0, NumberInput.parseAsDouble("0", -1.0), 0.0);
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", -1.0), 0.0);
        assertEquals(-123.45, NumberInput.parseAsDouble("-123.45", -1.0), 0.0);
    }

    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0"));
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45"));
        assertEquals(new BigDecimal("-123.45"), NumberInput.parseBigDecimal("-123.45"));
    }

    @Test
    public void testInLongRange() {
        assertTrue(NumberInput.inLongRange("123456789".toCharArray(), 0, 9, false));
        assertFalse(NumberInput.inLongRange("1234567890".toCharArray(), 0, 10, false));
        assertTrue(NumberInput.inLongRange("-123456789".toCharArray(), 0, 9, true));
        assertFalse(NumberInput.inLongRange("-1234567890".toCharArray(), 0, 10, true));
    }

}