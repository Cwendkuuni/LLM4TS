package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_validInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        for (int offset = 0, len = 1; len <= 9; ++len) {
            int num = NumberInput.parseInt(digitChars, offset, len);
            assertEquals(Integer.parseInt(new String(digitChars, offset, len)), num);
            offset += len;
        }
    }

    @Test
    public void testParseInt_invalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'x'};
        int offset = 0, len = 5;
        assertFalse(NumberInput.inLongRange(digitChars, offset, 5, false));
        try {
            NumberInput.parseInt(digitChars, offset, len);
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLong_validInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long num = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(Long.parseLong(new String(digitChars)), num);
    }

    @Test
    public void testParseLong_invalidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'x'};
        long num = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertNotSame(Long.parseLong(new String(digitChars)), num);
        try {
            NumberInput.parseLong(digitChars, 0, digitChars.length + 1);
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseLong_parseLongFallback() {
        String str = "12345678901234567890";
        long num = NumberInput.parseLong(str.toCharArray());
        assertEquals(Long.parseLong(str), num);
    }

    @Test
    public void testParseLong_handleNegativeSign() {
        String str = "-12345678901234567890";
        long num = NumberInput.parseLong(str.toCharArray(), 0, str.length());
        assertEquals(-Long.parseLong(str), num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_longOutOfRange() {
        String str = "12345678901234567890123456789012345678901234567890";
        NumberInput.parseLong(str.toCharArray(), 0, str.length());
    }

    @Test
    public void testParseAsInt_validInput() {
        String input = "12345";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(Integer.parseInt(input), num);
    }

    @Test
    public void testParseAsInt_invalidInput() {
        String input = "12345x";
        int num = NumberInput.parseAsInt(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsLong_validInput() {
        String input = "12345678901234567890";
        long num = NumberInput.parseAsLong(input, 0);
        assertEquals(Long.parseLong(input), num);
    }

    @Test
    public void testParseAsLong_invalidInput() {
        String input = "12345678901234567890x";
        long num = NumberInput.parseAsLong(input, 0);
        assertEquals(0, num);
    }

    @Test
    public void testParseAsDouble_validInput() {
        String input = "123.45";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(Double.parseDouble(input), num, 0.001);
    }

    @Test
    public void testParseAsDouble_invalidInput() {
        String input = "123.45x";
        double num = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(0.0, num, 0.001);
    }

    @Test
    public void testParseDouble() throws NumberFormatException {
        String numStr = "123.45";
        double num = NumberInput.parseDouble(numStr);
        assertEquals(Double.parseDouble(numStr), num, 0.001);

        numStr = NumberInput.NASTY_SMALL_DOUBLE;
        num = NumberInput.parseDouble(numStr);
        assertEquals(Double.MIN_VALUE, num);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_invalidInput() {
        String numStr = "123x";
        NumberInput.parseBigDecimal(numStr.toCharArray());
    }

    @Test
    public void testParseBigDecimal() throws NumberFormatException {
        String numStr = "123.45";
        BigDecimal num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal(numStr), num);

        numStr = "123";
        num = NumberInput.parseBigDecimal(numStr.toCharArray());
        assertEquals(new BigDecimal(numStr), num);

        numStr = "1.23e-308";
        num = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal(NumberInput.NASTY_SMALL_DOUBLE), num);
    }
}