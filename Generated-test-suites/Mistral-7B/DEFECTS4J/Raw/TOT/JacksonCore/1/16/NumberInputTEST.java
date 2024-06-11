package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(12345, result);
 }

 @Test
 public void testParseIntWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(-12345, result);
 }

 @Test
 public void testParseIntWithLongerInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
 int result = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(12345, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntWithNegativeLength() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 4);
 }

 @Test
 public void testParseIntWithInvalidChars() {
 char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(1345, result); // should be an error, but the code continues
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{ '1', '2', '3', '4', '5', '6', '7', '8', '9' };
 long result = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(1234567890L, result);
 }

 @Test
 public void testParseLongWithNegativeInput() {
 char[] digitChars = new char[]{ '-', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
 long result = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(-1234567890L, result);
 }

 @Test
 public void testParseLongWithLongerInput() {
 char[] digitChars = new char[]{ '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1' };
 long result = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(12345678901L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLongWithInvalidLength() {
 char[] digitChars = new char[]{ '1', '2', '3', '4', '5', '6', '7', '8', '9' };
 long result = NumberInput.parseLong(digitChars, 0, 9);
 }

 @Test
 public void testParseLongWithInvalidChars() {
 char[] digitChars = new char[]{ '1', '2', '3', '4', '5', 'a', '6', '7', '8', '9' };
 long result = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(1234567890L, result); // should be an error, but the code continues
 }

 @Test
 public void testParseIntWithStringInput() {
 String str = "12345";
 int result = NumberInput.parseInt(str);
 assertEquals(12345, result);
 }

 @Test
 public void testParseIntWithNegativeStringInput() {
 String str = "-12345";
 int result = NumberInput.parseInt(str);
 assertEquals(-12345, result);
 }

 @Test
 public void testParseIntWithLongerStringInput() {
 String str = "1234567890";
 int result = NumberInput.parseInt(str);
 assertEquals(1234567890, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidStringInput() {
 String str = "1a345";
 int result = NumberInput.parseInt(str);
 }

 @Test
 public void testParseLongWithStringInput() {
 String str = "1234567890123";
 long result = NumberInput.parseLong(str);
 assertEquals(1234567890123L, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidStringInput() {
 String str = "123a4567890";
 long result = NumberInput.parseLong(str);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = null;
 int result = NumberInput.parseAsInt(input, 42);
 assertEquals(42, result);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "123";
 int result = NumberInput.parseAsInt(input, 42);
 assertEquals(123, result);
 }

 @Test
 public void testParseAsIntWithNegativeInput() {
 String input = "-123";
 int result = NumberInput.parseAsInt(input, 42);
 assertEquals(-123, result);
 }

 @Test
 public void testParseAsIntWithLongerInput() {
 String input = "1234567890";
 int result = NumberInput.parseAsInt(input, 42);
 assertEquals(1234567890, result);
 }

 @Test
 public void testParseAsIntWithInvalidInput() {
 String input = "1a345";
 int result = NumberInput.parseAsInt(input, 42);
 assertEquals(42, result);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = null;
 long result = NumberInput.parseAsLong(input, 42);
 assertEquals(42, result);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "123";
 long result = NumberInput.parseAsLong(input, 42);
 assertEquals(123, result);
 }

 @Test
 public void testParseAsLongWithNegativeInput() {
 String input = "-123";
 long result = NumberInput.parseAsLong(input, 42);
 assertEquals(-123, result);
 }

 @Test
 public void testParseAsLongWithLongerInput() {
 String input = "1234567890123";
 long result = NumberInput.parseAsLong(input, 42);
 assertEquals(1234567890123, result);
 }

 @Test
 public void testParseAsLongWithInvalidInput() {
 String input = "1a345";
 long result = NumberInput.parseAsLong(input, 42);
 assertEquals(42, result);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String input = null;
 double result = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(42.0, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.45";
 double result = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(123.45, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithNegativeInput() {
 String input = "-123.45";
 double result = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(-123.45, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithLongerInput() {
 String input = "1234567890.123456";
 double result = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(1234567890.123456, result, 0.000001);
 }

 @Test
 public void testParseAsDoubleWithInvalidInput() {
 String input = "1a3.45";
 double result = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(42.0, result, 0.001);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String numStr = "123.45";
 BigDecimal result = NumberInput.parseBigDecimal(numStr);
 BigDecimal expected = new BigDecimal("123.45");
 assertEquals(expected, result);
 }

 @Test
 public void testParseBigDecimalWithLongerInput() {
 String numStr = "1234567890.123456";
 BigDecimal result = NumberInput.parseBigDecimal(numStr);
 BigDecimal expected = new BigDecimal("1234567890.123456");
 assertEquals(expected, result);
 }

 @Test
 public void testParseBigDecimalWithInvalidInput() {
 char[] buffer = new char[]{'1', 'a', '3', '4', '5'};
 BigDecimal result = NumberInput.parseBigDecimal(buffer);
 BigDecimal expected = BigDecimal.ZERO;
 assertEquals(expected, result);
 }

 @Test
 public void testParseBigDecimalWithValidLongInput() {
 char[] buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 10);
 BigDecimal expected = new BigDecimal("1234567890");
 assertEquals(expected, result);
 }

 @Test
 public void testParseBigDecimalWithLongerLongInput() {
 char[] buffer = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 11);
 BigDecimal expected = new BigDecimal("12345678901");
 assertEquals(expected, result);
 }

}