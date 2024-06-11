package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // Typical use cases
        assertEquals(123, NumberInput.parseInt("123"));
        assertEquals(-456, NumberInput.parseInt("-456"));

        // Edge cases
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testParseLong() {
        // Typical use cases
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        assertEquals(-987654321L, NumberInput.parseLong("-987654321"));

        // Edge cases
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void testParseAsInt() {
        // Typical use cases
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        assertEquals(-456, NumberInput.parseAsInt("-456", 0));

        // Edge cases
        assertEquals(Integer.MAX_VALUE, NumberInput.parseAsInt(String.valueOf(Integer.MAX_VALUE), 0));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseAsInt(String.valueOf(Integer.MIN_VALUE), 0));

        // Error scenarios
        assertEquals(0, NumberInput.parseAsInt("abc", 0));
        assertEquals(0, NumberInput.parseAsInt("12.34", 0));
    }

    @Test
    public void testParseAsLong() {
        // Typical use cases
        assertEquals(123456789L, NumberInput.parseAsLong("123456789", 0));
        assertEquals(-987654321L, NumberInput.parseAsLong("-987654321", 0));

        // Edge cases
        assertEquals(Long.MAX_VALUE, NumberInput.parseAsLong(String.valueOf(Long.MAX_VALUE), 0));
        assertEquals(Long.MIN_VALUE, NumberInput.parseAsLong(String.valueOf(Long.MIN_VALUE), 0));

        // Error scenarios
        assertEquals(0, NumberInput.parseAsLong("abc", 0));
        assertEquals(0, NumberInput.parseAsLong("12.34", 0));
    }

    @Test
    public void testParseAsDouble() {
        // Typical use cases
        assertEquals(12.34, NumberInput.parseAsDouble("12.34", 0), 0.00001);
        assertEquals(-56.78, NumberInput.parseAsDouble("-56.78", 0), 0.00001);

        // Edge cases
        assertEquals(Double.MAX_VALUE, NumberInput.parseAsDouble(String.valueOf(Double.MAX_VALUE), 0), 0.00001);
        assertEquals(Double.MIN_VALUE, NumberInput.parseAsDouble(String.valueOf(Double.MIN_VALUE), 0), 0.00001);

        // Error scenarios
        assertEquals(0, NumberInput.parseAsDouble("abc", 0), 0.00001);
    }

    @Test
    public void testParseDouble() {
        // Typical use cases
        assertEquals(12.34, NumberInput.parseDouble("12.34"), 0.00001);
        assertEquals(-56.78, NumberInput.parseDouble("-56.78"), 0.00001);

        // Edge cases
        assertEquals(Double.MAX_VALUE, NumberInput.parseDouble(String.valueOf(Double.MAX_VALUE)), 0.00001);
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(String.valueOf(Double.MIN_VALUE)), 0.00001);

        // Error scenarios
        assertEquals(Double.MIN_NORMAL, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.00001);
    }

    @Test
    public void testParseBigDecimal() {
        // Typical use cases
        assertEquals(new BigDecimal("12.34"), NumberInput.parseBigDecimal("12.34"));
        assertEquals(new BigDecimal("-56.78"), NumberInput.parseBigDecimal("-56.78"));

        // Edge cases
        assertEquals(new BigDecimal(String.valueOf(Double.MAX_VALUE)), NumberInput.parseBigDecimal(String.valueOf(Double.MAX_VALUE)));
        assertEquals(new BigDecimal(String.valueOf(Double.MIN_VALUE)), NumberInput.parseBigDecimal(String.valueOf(Double.MIN_VALUE)));

        // Error scenarios
        try {
            NumberInput.parseBigDecimal("abc");
            fail("Expected NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testInLongRange() {
        // Typical use cases
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), false));
        assertFalse(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), true));
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MIN_VALUE), true));
        assertFalse(NumberInput.inLongRange(String.valueOf(Long.MIN_VALUE), false));

        // Edge cases
        assertFalse(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE + 1L), false));
        assertFalse(NumberInput.inLongRange(String.valueOf(Long.MIN_VALUE - 1L), true));
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), true));
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MIN_VALUE), false));

        // Error scenarios
        assertFalse(NumberInput.inLongRange("abc".toCharArray(), 0, 3, false));
        assertFalse(NumberInput.inLongRange("abc".toCharArray(), 0, 3, true));
    }

    @Test
    public void testParseBigDecimalCharArray() {
        // Typical use cases
        assertEquals(new BigDecimal("12.34"), NumberInput.parseBigDecimal("12.34".toCharArray(), 0, 5));
        assertEquals(new BigDecimal("-56.78"), NumberInput.parseBigDecimal("-56.78".toCharArray(), 0, 6));

        // Edge cases
        assertEquals(new BigDecimal(String.valueOf(Double.MAX_VALUE)), NumberInput.parseBigDecimal(String.valueOf(Double.MAX_VALUE).toCharArray(), 0, String.valueOf(Double.MAX_VALUE).length()));
        assertEquals(new BigDecimal(String.valueOf(Double.MIN_VALUE)), NumberInput.parseBigDecimal(String.valueOf(Double.MIN_VALUE).toCharArray(), 0, String.valueOf(Double.MIN_VALUE).length()));

        // Error scenarios
        try {
            NumberInput.parseBigDecimal("abc".toCharArray(), 0, 3);
            fail("Expected NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBigDecimalIntArray() {
        // Typical use cases
        assertEquals(new BigDecimal("12.34"), NumberInput.parseBigDecimal("12.34".toCharArray()));
        assertEquals(new BigDecimal("-56.78"), NumberInput.parseBigDecimal("-56.78".toCharArray()));

        // Error scenarios
        try {
            NumberInput.parseBigDecimal("abc".toCharArray());
            fail("Expected NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

}