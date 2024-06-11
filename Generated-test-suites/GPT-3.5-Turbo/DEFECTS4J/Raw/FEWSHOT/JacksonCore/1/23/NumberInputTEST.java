package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_CharArray() {
        char[] digitChars1 = "12345".toCharArray();
        char[] digitChars2 = "54321".toCharArray();
        
        assertEquals(12345, NumberInput.parseInt(digitChars1, 0, 5));
        assertEquals(54321, NumberInput.parseInt(digitChars2, 0, 5));
    }

    @Test
    public void testParseInt_String() {
        assertEquals(12345, NumberInput.parseInt("12345"));
        assertEquals(-54321, NumberInput.parseInt("-54321"));
        assertEquals(0, NumberInput.parseInt("0"));
    }

    @Test
    public void testParseLong_CharArray() {
        char[] digitChars1 = "123456789012345678".toCharArray();
        char[] digitChars2 = "987654321098765432".toCharArray();
        
        assertEquals(123456789012345678L, NumberInput.parseLong(digitChars1, 0, 18));
        assertEquals(987654321098765432L, NumberInput.parseLong(digitChars2, 0, 18));
    }

    @Test
    public void testParseLong_String() {
        assertEquals(123456789012345678L, NumberInput.parseLong("123456789012345678"));
        assertEquals(-987654321098765432L, NumberInput.parseLong("-987654321098765432"));
        assertEquals(0L, NumberInput.parseLong("0"));
    }

    @Test
    public void testInLongRange_CharArray() {
        char[] digitChars1 = "1234".toCharArray();
        char[] digitChars2 = "123456789012345678".toCharArray();
        
        assertTrue(NumberInput.inLongRange(digitChars1, 0, 4, false));
        assertFalse(NumberInput.inLongRange(digitChars2, 0, 18, false));
    }

    @Test
    public void testInLongRange_String() {
        assertTrue(NumberInput.inLongRange("1234", false));
        assertFalse(NumberInput.inLongRange("123456789012345678", false));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(12345, NumberInput.parseAsInt("12345", 0));
        assertEquals(-54321, NumberInput.parseAsInt("-54321", 0));
        assertEquals(0, NumberInput.parseAsInt("0", 0));
        assertEquals(0, NumberInput.parseAsInt("", 0));
        assertEquals(0, NumberInput.parseAsInt(null, 0));
        assertEquals(0, NumberInput.parseAsInt("abc", 0));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(123456789012345678L, NumberInput.parseAsLong("123456789012345678", 0L));
        assertEquals(-987654321098765432L, NumberInput.parseAsLong("-987654321098765432", 0L));
        assertEquals(0L, NumberInput.parseAsLong("0", 0L));
        assertEquals(0L, NumberInput.parseAsLong("", 0L));
        assertEquals(0L, NumberInput.parseAsLong(null, 0L));
        assertEquals(0L, NumberInput.parseAsLong("abc", 0L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", 0.0), 0.0);
        assertEquals(-54.321, NumberInput.parseAsDouble("-54.321", 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble("0.0", 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble(null, 0.0), 0.0);
        assertEquals(0.0, NumberInput.parseAsDouble("abc", 0.0), 0.0);
    }

    @Test
    public void testParseDouble() {
        assertEquals(3.14, NumberInput.parseDouble("3.14"), 0.0);
        assertEquals(-2.71, NumberInput.parseDouble("-2.71"), 0.0);
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
    }

    @Test
    public void testParseBigDecimal_String() {
        BigDecimal num1 = new BigDecimal("3.14");
        BigDecimal num2 = new BigDecimal("-2.71");
        
        assertEquals(num1, NumberInput.parseBigDecimal("3.14"));
        assertEquals(num2, NumberInput.parseBigDecimal("-2.71"));
    }

    @Test
    public void testParseBigDecimal_CharArray() {
        char[] buffer1 = "3.14".toCharArray();
        char[] buffer2 = "-2.71".toCharArray();
        
        BigDecimal num1 = new BigDecimal("3.14");
        BigDecimal num2 = new BigDecimal("-2.71");
        
        assertEquals(num1, NumberInput.parseBigDecimal(buffer1));
        assertEquals(num2, NumberInput.parseBigDecimal(buffer2));
    }

    @Test
    public void testParseBigDecimal_CharArrayIntInt() {
        char[] buffer1 = "3.14".toCharArray();
        char[] buffer2 = "-2.71".toCharArray();
        
        BigDecimal num1 = new BigDecimal("3.14");
        BigDecimal num2 = new BigDecimal("-2.71");
        
        assertEquals(num1, NumberInput.parseBigDecimal(buffer1, 0, 4));
        assertEquals(num2, NumberInput.parseBigDecimal(buffer2, 0, 5));
    }
}