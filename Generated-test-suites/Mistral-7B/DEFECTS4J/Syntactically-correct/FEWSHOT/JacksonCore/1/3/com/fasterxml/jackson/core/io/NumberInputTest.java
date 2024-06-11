package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntFromCharArray() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
        for (int i = 0; i <= 9; i++) {
            int result = NumberInput.parseInt(digitChars, 0, i + 1);
            assertEquals(i, result);
        }
        int[] expectedNegativeNumbers = {-1, -2, -3, -4, -5};
        for (int i = 0; i < expectedNegativeNumbers.length; i++) {
            int result = NumberInput.parseInt(digitChars, 0, 1 + i + 1);
            assertEquals(expectedNegativeNumbers[i], result);
        }
    }

    @Test
    public void testParseIntFromString() {
        String testCases = "1 2 3 4 5 -1 -2 -3 -4 -5";
        String[] numbers = testCases.split(" ");
        for (String number : numbers) {
            int result = NumberInput.parseInt(number);
            int expected = Integer.parseInt(number);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testParseLongFromCharArray() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 123456789L;
        long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
        assertEquals(expected, result);

        char[] negDigitChars = new char[] {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expectedNegative = -123456789L;
        long resultNegative = NumberInput.parseLong(negDigitChars, 0, negDigitChars.length);
        assertEquals(expectedNegative, resultNegative);
    }

    @Test
    public void testParseLongFromString() {
        String testCases = "123456789 123456789L -123456789 -123456789L";
        String[] numbers = testCases.split(" ");
        for (String number : numbers) {
            long result = NumberInput.parseLong(number);
            long expected = Long.parseLong(number.replace("L", ""));
            assertEquals(expected, result);
            if (number.charAt(0) == '-') {
                assertEquals(expected * -1, result);
            }
        }
    }

    @Test
    public void testInLongRange() {
        char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8'};
        boolean negative = false;
        for (String numberStr : new String[] {"12345678", "123456789", "1234567890", "-1234567890"}) {
            boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length - 1, negative);
            assertTrue(result);
            negative = true;
        }
        for (String numberStr : new String[] {"12345678901", "123456789012", "1234567890123"}) {
            boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length - 1, negative);
            assertFalse(result);
        }
    }

    @Test
    public void testParseAsInt() {
        String[] testCases = new String[] {"1", "12", "123", "1234", "12345", "123456", "1234567", "12345678", "123456789", "-1234567890", "1+1", "1a"};
        int[] expected = new int[] {1, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789, -1234567890, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int i = 0; i < testCases.length; i++) {
            int result = NumberInput.parseAsInt(testCases[i], i);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testParseAsLong() {
        String[] testCases = new String[] {"1", "12", "123", "1234", "12345", "123456", "1234567", "12345678", "123456789", "-1234567890", "1+1", "1a"};
        long[] expected = new long[] {1, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789, -1234567890, Long.MIN_VALUE, Long.MAX_VALUE};
        for (int i = 0; i < testCases.length; i++) {
            long result = NumberInput.parseAsLong(testCases[i], i);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testParseAsDouble() {
        String[] testCases = new String[] {"1", "1.1", "1.12", "1.123", "1.1234", "1.12345", "1.123456", "1.1234567", "1.12345678", "1.123456789", "-1.1234567890", "1+1.0", "1a"};
        double[] expected = new double[] {1.0, 1.1, 1.12, 1.123, 1.1234, 1.12345, 1.123456, 1.1234567, 1.12345678, 1.123456789, -1.1234567890, Double.MIN_VALUE, Double.MAX_VALUE};
        for (int i = 0; i < testCases.length; i++) {
            double result = NumberInput.parseAsDouble(testCases[i], i);
            assertEquals(expected[i], result, 0.00001);
        }
    }

    @Test
    public void testParseBigDecimal() {
        String[] testCases = new String[] {"1", "1.1", "1.12", "1.123", "1.1234", "1.12345", "1.123456", "1.1234567", "1.12345678", "1.123456789", "1.10E1", "1.10E2", "1.10E3", "-1.10E3", "1a"};
        BigDecimal[] expected = new BigDecimal[] {BigDecimal.ONE, new BigDecimal("1.1"), new BigDecimal("1.12"), new BigDecimal("1.123"), new BigDecimal("1.1234"), new BigDecimal("1.12345"), new BigDecimal("1.123456"), new BigDecimal("1.1234567"), new BigDecimal("1.12345678"), new BigDecimal("1.123456789"), new BigDecimal("1.1E1"), new BigDecimal("1.1E2"), new BigDecimal("1.1E3"), new BigDecimal("-1.1E3"), BigDecimal.ZERO};
        for (int i = 0; i < testCases.length; i++) {
            BigDecimal result = NumberInput.parseBigDecimal(testCases[i]);
            assertEquals(expected[i], result);
        }
    }

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] testCases = new char[] {'1', '.', '1', '2', '.', '3', '.', '4', '.', '5', '.', '6', '.', '7', '.', '8', '.', '9', 'E', '-', '3'};
        BigDecimal expected = new BigDecimal("1.123456789E-3");
        BigDecimal result = NumberInput.parseBigDecimal(testCases, 0, testCases.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimalFromCharArrayWithOffset() {
        char[] testCases = new char[] {'1', '.', '1', '2', '.', '3', '.', '4', '.', '5', '.', '6', '.', '7', '.', '8', '.', '9', 'E', '-', '3'};
        BigDecimal expected = new BigDecimal("1.123456789E-3");
        BigDecimal result = NumberInput.parseBigDecimal(testCases, 0, 12);
        assertEquals(expected, result);
    }

}