package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.io.NumberInput;

import org.junit.Test;

public class NumberInputTest {

 // Test case for parseInt method with valid input
 @Test
 public void testValidIntParse() {
 String validInput = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(validInput.toCharArray(), 0, validInput.length());
 assertEquals(expected, actual);
 }

 // Test case for parseInt method with edge cases
 @Test
 public void testEdgeCasesIntParse() {
 // Test for minimum valid int
 String minValidInput = Integer.MIN_VALUE+"";
 int expectedMin = Integer.MIN_VALUE;
 int actualMin = NumberInput.parseInt(minValidInput.toCharArray(), 0, minValidInput.length());
 assertEquals(expectedMin, actualMin);

 // Test for maximum valid int
 String maxValidInput = Integer.MAX_VALUE+"";
 int expectedMax = Integer.MAX_VALUE;
 int actualMax = NumberInput.parseInt(maxValidInput.toCharArray(), 0, maxValidInput.length());
 assertEquals(expectedMax, actualMax);
 }

 // Test case for parseInt method with negative input
 @Test(expected = NumberFormatException.class)
 public void testNegativeIntParse() {
 String negativeInput = "-12345";
 NumberInput.parseInt(negativeInput.toCharArray(), 0, negativeInput.length());
 }

 // Test case for parseLong method with valid input
 @Test
 public void testValidLongParse() {
 String validInput = "12345678901234";
 long expected = 12345678901234L;
 long actual = NumberInput.parseLong(validInput);
 assertEquals(expected, actual);
 }

 // Test case for parseLong method with edge cases
 @Test
 public void testEdgeCasesLongParse() {
 // Test for minimum valid long
 long minValidLong = Long.MIN_VALUE;
 long expectedMin = minValidLong;
 long actualMin = NumberInput.parseLong(String.valueOf(minValidLong).substring(1));
 assertEquals(expectedMin, actualMin);

 // Test for maximum valid long
 long maxValidLong = Long.MAX_VALUE;
 long expectedMax = maxValidLong;
 long actualMax = NumberInput.parseLong(String.valueOf(maxValidLong));
 assertEquals(expectedMax, actualMax);
 }

 // Test case for inLongRange method with valid input
 @Test
 public void testInLongRangeValid() {
 String validInput = "12345678901234";
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(validInput.toCharArray(), 0, validInput.length(), false);
 assertEquals(expected, actual);
 }

 // Test case for inLongRange method with edge cases
 @Test
 public void testInLongRangeEdgeCases() {
 // Test for minimum valid long
 char[] minValidLongChars = String.valueOf(Long.MIN_VALUE).toCharArray();
 boolean expectedMin = NumberInput.inLongRange(minValidLongChars, 0, minValidLongChars.length-1, false);
 assertTrue(expectedMin);

 // Test for maximum valid long
 char[] maxValidLongChars = String.valueOf(Long.MAX_VALUE).toCharArray();
 boolean expectedMax = NumberInput.inLongRange(maxValidLongChars, 0, maxValidLongChars.length, false);
 assertTrue(expectedMax);

 // Test for invalid input
 char[] invalidInputChars = {'a','b','c','d'};
 boolean expectedInvalid = false;
 boolean actualInvalid = NumberInput.inLongRange(invalidInputChars, 0, invalidInputChars.length, false);
 assertFalse(actualInvalid);
 }

 // Test case for parseAsInt method with valid input
 @Test
 public void testValidParseAsInt() {
 String validInput = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(validInput, 0);
 assertEquals(expected, actual);
 }

 // Test case for parseAsInt method with edge cases
 @Test
 public void testEdgeCasesParseAsInt() {
 // Test for minimum valid int
 int minValidInput = Integer.MIN_VALUE;
 int expectedMin = minValidInput;
 int actualMin = NumberInput.parseAsInt(""+minValidInput, minValidInput);
 assertEquals(expectedMin, actualMin);

 // Test for maximum valid int
 int maxValidInput = Integer.MAX_VALUE;
 int expectedMax = maxValidInput;
 int actualMax = NumberInput.parseAsInt(""+maxValidInput, maxValidInput);
 assertEquals(expectedMax, actualMax);
 }

 // Test case for parseAsInt method with negative input
 @Test(expected = NumberFormatException.class)
 public void testNegativeParseAsInt() {
 String negativeInput = "-12345";
 NumberInput.parseAsInt(negativeInput, 0);
 }

 // Test case for parseAsLong method with valid input
 @Test
 public void testValidParseAsLong() {
 String validInput = "12345678901234";
 long expected = 12345678901234L;
 long actual = NumberInput.parseAsLong(validInput, 0L);
 assertEquals(expected, actual);
 }

 // Test case for parseAsLong method with edge cases
 @Test
 public void testEdgeCasesParseAsLong() {
 // Test for minimum valid long
 long minValidLong = Long.MIN_VALUE;
 long expectedMin = minValidLong;
 long actualMin = NumberInput.parseAsLong(String.valueOf(minValidLong), minValidLong);
 assertEquals(expectedMin, actualMin);

 // Test for maximum valid long
 long maxValidLong = Long.MAX_VALUE;
 long expectedMax = maxValidLong;
 long actualMax = NumberInput.parseAsLong(String.valueOf(maxValidLong), maxValidLong);
 assertEquals(expectedMax, actualMax);
 }

 // Test case for parseAsLong method with negative input
 @Test(expected = NumberFormatException.class)
 public void testNegativeParseAsLong() {
 String negativeInput = "-12345678901234";
 NumberInput.parseAsLong(negativeInput, 0L);
 }

 // Test case for parseAsDouble method with valid input
 @Test
 public void testValidParseAsDouble() {
 String validInput = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(validInput);
 assertEquals(expected, actual, 0.001);
 }

 // Test case for parseAsDouble method with edge cases
 @Test
 public void testEdgeCasesParseAsDouble() {
 // Test for minimum valid double
 double minValidDouble = Double.MIN_VALUE;
 double expectedMin = minValidDouble;
 double actualMin = NumberInput.parseAsDouble(String.valueOf(minValidDouble));
 assertEquals(expectedMin, actualMin, 0.001);

 // Test for maximum valid double
 double maxValidDouble = Double.MAX_VALUE;
 double expectedMax = maxValidDouble;
 double actualMax = NumberInput.parseAsDouble(String.valueOf(maxValidDouble));
 assertEquals(expectedMax, actualMax, 0.001);
 }

 // Test case for parseDouble method with valid input
 @Test
 public void testValidParseDouble() {
 String validInput = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseDouble(validInput);
 assertEquals(expected, actual, 0.001);
 }

 // Test case for parseDouble method with edge cases
 @Test(expected = NumberFormatException.class)
 public void testEdgeCasesParseDouble() {
 String edgeCaseInput1 = "1E-308";
 String edgeCaseInput2 = "1E+308";
 NumberInput.parseDouble(edgeCaseInput1);
 NumberInput.parseDouble(edgeCaseInput2);
 }

 // Test case for parseBigDecimal method with valid input
 @Test
 public void testValidParseBigDecimal() {
 String validInput = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(validInput);
 assertEquals(expected, actual);
 }

 // Test case for parseBigDecimal method with edge cases
 @Test
 public void testEdgeCasesParseBigDecimal() {
 String edgeCaseInput1 = "1E-308";
 String edgeCaseInput2 = "1E+308";
 BigDecimal expectedEdgeCase1 = new BigDecimal("0");
 BigDecimal actualEdgeCase1 = NumberInput.parseBigDecimal(edgeCaseInput1);
 assertEquals(expectedEdgeCase1, actualEdgeCase1);

 BigDecimal expectedEdgeCase2 = new BigDecimal("1.1234567890123456789E+308");
 BigDecimal actualEdgeCase2 = NumberInput.parseBigDecimal(edgeCaseInput2);
 assertEquals(expectedEdgeCase2, actualEdgeCase2);
 }
}