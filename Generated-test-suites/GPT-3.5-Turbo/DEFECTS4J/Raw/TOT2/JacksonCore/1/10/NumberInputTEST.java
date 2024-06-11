package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // Typical use case
        assertEquals(12345, NumberInput.parseInt("12345"));
        
        // Edge cases
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt("-2147483648"));
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt("2147483647"));
        
        // Error scenario
        try {
            NumberInput.parseInt("123abc");
            fail("NumberFormatException should have been thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseLong() {
        // Typical use case
        assertEquals(12345678901234L, NumberInput.parseLong("12345678901234"));
        
        // Edge cases
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong("-9223372036854775808"));
        assertEquals(0, NumberInput.parseLong("0"));
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong("9223372036854775807"));
        
        // Error scenario
        try {
            NumberInput.parseLong("123abc");
            fail("NumberFormatException should have been thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseAsInt() {
        // Typical use case
        assertEquals(123, NumberInput.parseAsInt("123", -1));
        
        // Edge cases
        assertEquals(-1, NumberInput.parseAsInt("-2147483649", -1));
        assertEquals(0, NumberInput.parseAsInt("+0", -1));
        assertEquals(2147483647, NumberInput.parseAsInt("2147483647", -1));
        
        // Error scenario
        assertEquals(-1, NumberInput.parseAsInt("123abc", -1));
    }

    @Test
    public void testParseAsLong() {
        // Typical use case
        assertEquals(12345678901234L, NumberInput.parseAsLong("12345678901234", -1));
        
        // Edge cases
        assertEquals(-1, NumberInput.parseAsLong("-9223372036854775809", -1));
        assertEquals(0, NumberInput.parseAsLong("+0", -1));
        assertEquals(9223372036854775807L, NumberInput.parseAsLong("9223372036854775807", -1));
        
        // Error scenario
        assertEquals(-1, NumberInput.parseAsLong("123abc", -1));
    }

    @Test
    public void testParseAsDouble() {
        // Typical use case
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", -1.0), 0.01);
        
        // Edge cases
        assertEquals(Double.MIN_VALUE, NumberInput.parseAsDouble("2.2250738585072012e-308", -1.0), 0.01);
        
        // Error scenario
        assertEquals(-1.0, NumberInput.parseAsDouble("123.abc", -1.0), 0.01);
    }

    @Test
    public void testParseDouble() {
        // Typical use case
        assertEquals(123.45, NumberInput.parseDouble("123.45"), 0.01);
        
        // Edge cases
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble("2.2250738585072012e-308"), 0.01);
        
        // Error scenario
        try {
            NumberInput.parseDouble("123.abc");
            fail("NumberFormatException should have been thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseBigDecimal() {
        // Typical use case
        assertEquals(new BigDecimal("12345.67"), NumberInput.parseBigDecimal("12345.67"));
        
        // Edge cases
        assertEquals(new BigDecimal("2.2250738585072012E-308"), NumberInput.parseBigDecimal("2.2250738585072012e-308"));
        
        // Error scenario
        try {
            NumberInput.parseBigDecimal("123.abc");
            fail("NumberFormatException should have been thrown");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

}