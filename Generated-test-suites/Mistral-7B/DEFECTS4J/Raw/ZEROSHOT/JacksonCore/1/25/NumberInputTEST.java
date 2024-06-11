package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int offset = 0;
 int len = 3;
 int expectedResult = 123;
 int result = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expectedResult, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', 'a'};
 int offset = 0;
 int len = 3;
 NumberInput.parseInt(digitChars, offset, len);
 }

 @Test
 public void testParseIntFromString() {
 String input = "123";
 int expectedResult = 123;
 int result = NumberInput.parseInt(input);
 assertEquals(expectedResult, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntFromStringWithInvalidInput() {
 String input = "123a";
 NumberInput.parseInt(input);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 int offset = 0;
 int len = digitChars.length;
 long expectedResult = 1234567890123L;
 long result = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expectedResult, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', 'a'};
 int offset = 0;
 int len = 3;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test
 public void testParseLongFromString() {
 String input = "1234567890123";
 long expectedResult = 1234567890123L;
 long result = NumberInput.parseLong(input);
 assertEquals(expectedResult, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongFromStringWithInvalidInput() {
 String input = "1234567890123a";
 NumberInput.parseLong(input);
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean negative = false;
 assertTrue(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
 }

 @Test
 public void testInLongRangeWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '5', '6'};
 boolean negative = false;
 assertFalse(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
 }

 @Test
 public void testInLongRangeWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 boolean negative = true;
 assertTrue(NumberInput.inLongRange(digitChars, 0, digitChars.length, negative));
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "123";
 int expectedResult = 123;
 int result = NumberInput.parseAsInt(input, 0);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = "abcd";
 int expectedDefaultValue = 0;
 int result = NumberInput.parseAsInt(input, expectedDefaultValue);
 assertEquals(expectedDefaultValue, result);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890123";
 long expectedResult = 1234567890123L;
 long result = NumberInput.parseAsLong(input, 0);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = "abcd";
 long expectedDefaultValue = 0;
 long result = NumberInput.parseAsLong(input, expectedDefaultValue);
 assertEquals(expectedDefaultValue, result);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.45";
 double expectedResult = 123.45;
 double result = NumberInput.parseAsDouble(input, 0);
 assertEquals(expectedResult, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String input = "abcd";
 double expectedDefaultValue = 0.0;
 double result = NumberInput.parseAsDouble(input, expectedDefaultValue);
 assertEquals(expectedDefaultValue, result, 0.001);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String input = "123.45";
 BigDecimal expectedResult = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArray() {
 char[] input = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 0;
 int length = input.length;
 BigDecimal expectedResult = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input, offset, length);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArrayLongLength() {
 char[] input = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 0;
 long length = 6;
 BigDecimal expectedResult = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input, offset, (int)length);
 assertEquals(expectedResult, result);
 }
}