package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // typical use case
        assertEquals(123, NumberInput.parseInt("123"));

        // edge cases
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(1, NumberInput.parseInt("1"));
        assertEquals(-1, NumberInput.parseInt("-1"));

        // error scenarios
        assertEquals(0, NumberInput.parseInt(""));
        assertEquals(0, NumberInput.parseInt("abc"));
    }

    @Test
    public void testParseLong() {
        // typical use case
        assertEquals(1234567890L, NumberInput.parseLong("1234567890"));

        // edge cases
        assertEquals(0L, NumberInput.parseLong("0"));
        assertEquals(1L, NumberInput.parseLong("1"));
        assertEquals(-1L, NumberInput.parseLong("-1"));

        // error scenarios
        assertEquals(0L, NumberInput.parseLong(""));
        assertEquals(0L, NumberInput.parseLong("abc"));
    }

    @Test
    public void testInLongRange() {
        // typical use case
        assertTrue(NumberInput.inLongRange("123456", false));

        // edge cases
        assertTrue(NumberInput.inLongRange("0", false));
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
        assertTrue(NumberInput.inLongRange("-9223372036854775808", true));

        // error scenarios
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
        assertFalse(NumberInput.inLongRange("-9223372036854775809", false));
        assertFalse(NumberInput.inLongRange("", false));
        assertFalse(NumberInput.inLongRange("abc", false));
    }

    @Test
    public void testParseAsInt() {
        // typical use case
        assertEquals(123, NumberInput.parseAsInt("123", 0));

        // edge cases
        assertEquals(0, NumberInput.parseAsInt("0", 0));
        assertEquals(1, NumberInput.parseAsInt("1", 0));
        assertEquals(-1, NumberInput.parseAsInt("-1", 0));

        // error scenarios
        assertEquals(0, NumberInput.parseAsInt("", 0));
        assertEquals(0, NumberInput.parseAsInt("abc", 0));
        assertEquals(10, NumberInput.parseAsInt("10abc", 0));
        assertEquals(10, NumberInput.parseAsInt("10.5", 0));
    }

    @Test
    public void testParseAsLong() {
        // typical use case
        assertEquals(1234567890L, NumberInput.parseAsLong("1234567890", 0L));

        // edge cases
        assertEquals(0L, NumberInput.parseAsLong("0", 0L));
        assertEquals(1L, NumberInput.parseAsLong("1", 0L));
        assertEquals(-1L, NumberInput.parseAsLong("-1", 0L));

        // error scenarios
        assertEquals(0L, NumberInput.parseAsLong("", 0L));
        assertEquals(0L, NumberInput.parseAsLong("abc", 0L));
        assertEquals(10L, NumberInput.parseAsLong("10abc", 0L));
        assertEquals(10L, NumberInput.parseAsLong("10.5", 0L));
    }

    @Test
    public void testParseAsDouble() {
        // typical use case
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", 0.0), 0.00001);

        // edge cases
        assertEquals(0.0, NumberInput.parseAsDouble("0", 0.0), 0.00001);
        assertEquals(1.0, NumberInput.parseAsDouble("1", 0.0), 0.00001);
        assertEquals(-1.0, NumberInput.parseAsDouble("-1", 0.0), 0.00001);

        // error scenarios
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.00001);
        assertEquals(0.0, NumberInput.parseAsDouble("abc", 0.0), 0.00001);
        assertEquals(10.0, NumberInput.parseAsDouble("10abc", 0.0), 0.00001);
    }

    @Test
    public void testParseBigDecimal() {
        // typical use case
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45"));

        // edge case
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0"));

        // error scenarios
        try {
            NumberInput.parseBigDecimal("");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            NumberInput.parseBigDecimal("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseBigDecimalFromArray() {
        // typical use case
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45".toCharArray()));

        // edge case
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0".toCharArray()));

        // error scenarios
        try {
            NumberInput.parseBigDecimal("".toCharArray());
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            NumberInput.parseBigDecimal("abc".toCharArray());
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

}