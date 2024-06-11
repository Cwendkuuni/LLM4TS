package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        assertEquals(1, NumberInput.parseInt("1"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-1, NumberInput.parseInt("-1"));
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(0, NumberInput.parseInt("-0"));
    }

    @Test
    public void testParseLong() {
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        assertEquals(-123456789L, NumberInput.parseLong("-123456789"));
        assertEquals(0L, NumberInput.parseLong("0"));
        assertEquals(0L, NumberInput.parseLong("-0"));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(1, NumberInput.parseAsInt("1", 0));
        assertEquals(123456789, NumberInput.parseAsInt("123456789", 0));
        assertEquals(-1, NumberInput.parseAsInt("-1", 0));
        assertEquals(-123456789, NumberInput.parseAsInt("-123456789", 0));
        assertEquals(0, NumberInput.parseAsInt("0", 0));
        assertEquals(0, NumberInput.parseAsInt("-0", 0));
        assertEquals(0, NumberInput.parseAsInt("abc", 0));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(123456789L, NumberInput.parseAsLong("123456789", 0L));
        assertEquals(-123456789L, NumberInput.parseAsLong("-123456789", 0L));
        assertEquals(0L, NumberInput.parseAsLong("0", 0L));
        assertEquals(0L, NumberInput.parseAsLong("-0", 0L));
        assertEquals(0L, NumberInput.parseAsLong("abc", 0L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(1.0, NumberInput.parseAsDouble("1", 0.0), 0.0);
        assertEquals(123456789.0, NumberInput.parseAsDouble("123456789", 0.0), 0.0);
        assertEquals(-1.0, NumberInput.parseAsDouble("-1", 0.0), 0.0);
        assertEquals(-123456789.0, NumberInput.parseAsDouble("-123456789", 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble("0", 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble("-0", 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble("abc", 0.0), 0.0);
    }

    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("123456789"), NumberInput.parseBigDecimal("123456789"));
        assertEquals(new BigDecimal("-123456789"), NumberInput.parseBigDecimal("-123456789"));
    }

    @Test
    public void testInLongRange() {
        assertTrue(NumberInput.inLongRange("9223372036854775807", true));
        assertFalse(NumberInput.inLongRange("9223372036854775808", true));
        assertTrue(NumberInput.inLongRange("-9223372036854775808", true));
        assertFalse(NumberInput.inLongRange("-9223372036854775809", true));
    }

    @Test
    public void testParseDouble() {
        assertEquals(2.2250738585072014E-308, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
    }

}