package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] validInput = {'1', '2', '3', '4', '5'};
        int expectedResult = 12345;
        int result = NumberInput.parseInt(validInput, 0, validInput.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseIntWithValidInputString() {
        String validInput = "12345";
        int expectedResult = 12345;
        int result = NumberInput.parseInt(validInput);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseIntWithOutOfRangeInput() {
        char[] outOfRangeInput = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '1'};
        boolean result = NumberInput.inLongRange(outOfRangeInput, false);
        assertFalse(result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] invalidInput = {'a', 'b', 'c', 'd'};
        NumberInput.parseInt(invalidInput, 0, invalidInput.length);
    }

    @Test
    public void testParseIntWithNegativeInput() {
        char[] negativeInput = {'-', '1', '2', '3', '4', '5'};
        int expectedResult = -12345;
        int result = NumberInput.parseInt(negativeInput, 0, negativeInput.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] validInput = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        long expectedResult = 123456789123123L;
        long result = NumberInput.parseLong(validInput, 0, validInput.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseLongWithValidInputString() {
        String validInput = "123456789123123";
        long expectedResult = 123456789123123L;
        long result = NumberInput.parseLong(validInput);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseLongWithNegativeInput() {
        char[] negativeInput = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        long expectedResult = -123456789123123L;
        long result = NumberInput.parseLong(negativeInput, 0, negativeInput.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] invalidInput = {'a', 'b', 'c', 'd'};
        NumberInput.parseLong(invalidInput, 0, invalidInput.length);
    }

    @Test
    public void testParseLongWithOutOfRangeInput() {
        char[] outOfRangeInput = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'};
        boolean result = NumberInput.inLongRange(outOfRangeInput, false);
        assertFalse(result);
    }

    @Test
    public void testParseLongWithValidInputStringLongerThan18Digits() {
        String validInput = "123456789123123123123123123123123123123123123123123123123";
        long expectedResult = 123456789123123123123123123123123123123123123123123123123123123123L;
        long result = NumberInput.parseLong(validInput);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseLongWithLargeValidInput() {
        char[] largeValidInput = {'9', '2', '2', '3', '3', '7', '2', '2', '1', '1', '0', '4', '5', '3', '3', '1', '0', '4', '5', '3', '3', '1', '0', '4', '5', '3', '3', '1', '0', '4', '5', '3', '3', '1'};
        long expectedResult = Long.parseLong("9223372036854775807");
        long result = NumberInput.parseLong(largeValidInput, 0, largeValidInput.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInputLongerThan18Digits() {
        char[] invalidInput = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        NumberInput.parseLong(invalidInput, 0, invalidInput.length);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        char[] validInput = {'1', '2', '3', '4', '5', '.'};
        BigDecimal expectedResult = new BigDecimal("12345.0");
        BigDecimal result = NumberInput.parseBigDecimal(validInput, 0, validInput.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseBigDecimalWithValidInputString() {
        String validInput = "12345.";
        BigDecimal expectedResult = new BigDecimal("12345.0");
        BigDecimal result = NumberInput.parseBigDecimal(validInput);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        char[] invalidInput = {'a', 'b', 'c', 'd'};
        NumberInput.parseBigDecimal(invalidInput, 0, invalidInput.length);
    }

    @Test
    public void testParseBigDecimalWithNegativeInput() {
        char[] negativeInput = {'-', '1', '2', '3', '4', '5', '.'};
        BigDecimal expectedResult = new BigDecimal("-12345.0");
        BigDecimal result = NumberInput.parseBigDecimal(negativeInput, 0, negativeInput.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseBigDecimalWithValidInputLongerThan18Digits() {
        char[] validInput = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        BigDecimal expectedResult = new BigDecimal("123456789123123123123123123123123123123123123123123123123123123123123");
        BigDecimal result = NumberInput.parseBigDecimal(validInput, 0, validInput.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseBigDecimalWithInvalidInputLongerThan18Digits() {
        char[] invalidInput = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(invalidInput, 0, invalidInput.length));
    }
}