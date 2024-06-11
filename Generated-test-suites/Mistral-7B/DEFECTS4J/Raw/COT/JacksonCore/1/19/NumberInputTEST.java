package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.nio.CharBuffer;

import org.junit.Test;

public class NumberInputTest {

    // Test case for parseInt method with valid input of length 1
    @Test
    public void testParseInt_ValidInputLengthOne() {
        char[] digitChars = {'1'};
        int expected = 1;
        int actual = NumberInput.parseInt(digitChars, 0, 1);
        assertEquals(expected, actual);
    }

    // Test case for parseInt method with valid input of length 5
    @Test
    public void testParseInt_ValidInputLengthFive() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    // Test case for parseInt method with negative input of length 2
    @Test
    public void testParseInt_NegativeInputLengthTwo() {
        char[] digitChars = {'-', '1'};
        int expected = -1;
        int actual = NumberInput.parseInt(digitChars, 0, 2);
        assertEquals(expected, actual);
    }

    // Test case for parseInt method with input containing more digits than int can hold
    @Test(expected = NumberFormatException.class)
    public void testParseInt_InvalidInputLength() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6'}; // input length is 6, int can only hold up to 5 digits
        int offset = 0;
        int len = digitChars.length;
        NumberInput.parseInt(digitChars, offset, len);
    }

    // Test case for parseLong method with valid input of length 11
    @Test
    public void testParseLong_ValidInputLengthEleven() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long expected = 12345678901L;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, actual);
    }

    // Test case for parseLong method with negative input of length 12
    @Test
    public void testParseLong_NegativeInputLengthTwelve() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        long expected = -12345678901L;
        long actual = NumberInput.parseLong(digitChars, 0, 12);
        assertEquals(expected, actual);
    }

    // Test case for parseLong method with input containing more digits than long can hold
    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidInputLength() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'}; // input length is 13, long can only hold up to 18 digits
        int offset = 0;
        int len = digitChars.length;
        NumberInput.parseLong(digitChars, offset, len);
    }

    // Test case for inLongRange method with valid input of length 10
    @Test
    public void testInLongRange_ValidInputLengthTen() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 10, false);
        assertEquals(expected, actual);
    }

    // Test case for inLongRange method with valid input of length 11
    @Test
    public void testInLongRange_ValidInputLengthEleven() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 11, false);
        assertEquals(expected, actual);
    }

    // Test case for parseAsInt method with valid input of length 5
    @Test
    public void testParseAsInt_ValidInputLengthFive() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    // Test case for parseAsInt method with valid input of length 1
    @Test
    public void testParseAsInt_ValidInputLengthOne() {
        String input = "1";
        int expected = 1;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    // Test case for parseAsInt method with input containing non-digit characters
    @Test
    public void testParseAsInt_InvalidInput() {
        String input = "1a2bc3";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    // Test case for parseAsLong method with valid input of length 11
    @Test
    public void testParseAsLong_ValidInputLengthEleven() {
        String input = "12345678901";
        long expected = 12345678901L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    // Test case for parseAsLong method with valid input of length 1
    @Test
    public void testParseAsLong_ValidInputLengthOne() {
        String input = "1";
        long expected = 1;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    // Test case for parseAsLong method with input containing non-digit characters
    @Test
    public void testParseAsLong_InvalidInput() {
        String input = "1a2bc3";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    // Test case for parseAsDouble method with valid input of length 6
    @Test
    public void testParseAsDouble_ValidInputLengthSix() {
        String input = "123456.789";
        double expected = 123456.789;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001); // allow for small error due to floating point representation
    }

    // Test case for parseAsDouble method with valid input of length 1
    @Test
    public void testParseAsDouble_ValidInputLengthOne() {
        String input = "1";
        double expected = 1.0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0);
    }

    // Test case for parseAsDouble method with input containing non-digit characters
    @Test
    public void testParseAsDouble_InvalidInput() {
        String input = "1a2bc3.89e10";
        double expected = 0.0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0);
    }

    // Test case for parseDouble method with valid input
    @Test
    public void testParseDouble_ValidInput() {
        String input = "1.23456789e11";
        double expected = 1.23456789e11;
        double actual = NumberInput.parseDouble(input);
        assertEquals(expected, actual, 0.0);
    }

    // Test case for parseDouble method with invalid input ("Infinity")
    @Test(expected = NumberFormatException.class)
    public void testParseDouble_InvalidInputInfinity() {
        String input = "Infinity";
        NumberInput.parseDouble(input);
    }

    // Test case for parseBigDecimal method with valid input
    @Test
    public void testParseBigDecimal_ValidInput() {
        String input = "1234567890.123456";
        BigDecimal expected = new BigDecimal("1234567890.123456");
        BigDecimal actual = NumberInput.parseBigDecimal(input);
        assertEquals(expected, actual);
    }

    // Test case for parseBigDecimal method with valid input in char buffer
    @Test
    public void testParseBigDecimal_ValidInputCharBuffer() {
        char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '0', '1', '2', '3', '4', '5', '6'};
        int offset = 0;
        int length = buffer.length;
        BigDecimal expected = new BigDecimal(new String(buffer, 0, length));
        BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, length);
        assertEquals(expected, actual);
    }

    // Test case for parseBigDecimal method with invalid input in char buffer
    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_InvalidInputCharBuffer() {
        char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '0', '1', '2', 'b', 'c'};
        int offset = 0;
        int length = buffer.length;
        NumberInput.parseBigDecimal(buffer, offset, length);
    }
}