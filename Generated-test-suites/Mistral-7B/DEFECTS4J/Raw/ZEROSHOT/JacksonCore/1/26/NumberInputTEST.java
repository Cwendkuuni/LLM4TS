package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int expected = 123;
 int actual = NumberInput.parseInt(digitChars, 0, 3);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithValidInputString() {
 String input = "123";
 int expected = 123;
 int actual = NumberInput.parseInt(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithNegativeInputString() {
 String input = "-123";
 int expected = -123;
 int actual = NumberInput.parseInt(input);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInputString() {
 String input = "abc";
 NumberInput.parseInt(input);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInputLengthGreaterThanExpected() {
 char[] digitChars = new char[]{'1', '2', '3', '4'};
 NumberInput.parseInt(digitChars, 0, 3);
 NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitChars, 0, 4));
 assertTrue(exception.getMessage().contains("NumberFormatException: For input string: \"[1, 2, 3, 4]\""));
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 1234567890123L;
 long actual = NumberInput.parseLong(digitChars, 0, 20);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidInputString() {
 String input = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseLong(input);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInputString() {
 String input = "abc";
 NumberInput.parseLong(input);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInputLengthLessThanExpected() {
 char[] digitChars = new char[]{'1', '2', '3'};
 NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digitChars, 0, 2));
 assertTrue(exception.getMessage().contains("For input string: \"[1, 2, 3]\""));
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInputLengthGreaterThanExpected() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digitChars, 0, 20));
 assertTrue(exception.getMessage().contains("For input string: \"[1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1]\""));
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 5, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithValidInputNegative() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 5, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithInputLengthLessThanExpected() {
 char[] digitChars = new char[]{'1', '2', '3'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 3, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithInputLengthGreaterThanExpected() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 3, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithInputLongerThanMaxLong() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 13, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "123";
 int expected = 123;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = "abc";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseAsLong(input, 0L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = "abc";
 long expected = 0L;
 long actual = NumberInput.parseAsLong(input, 0L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String input = "abc";
 double expected = 0;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String input = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInput() {
 String input = "abc";
 NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(input));
 assertTrue(exception.getMessage().contains("For input string: \"abc\""));
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArray() {
 char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 0;
 int length = 6;
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInputCharArray() {
 char[] buffer = new char[]{'a', 'b', 'c'};
 int offset = 0;
 int length = 3;
 NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(buffer, offset, length));
 assertTrue(exception.getMessage().contains("For input character array: [a, b, c]"));
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArrayLongOffset() {
 char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 3;
 int length = 4;
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInputCharArrayLongOffset() {
 char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 7;
 int length = 4;
 NumberFormatException exception = assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(buffer, offset, length));
 assertTrue(exception.getMessage().contains("For input character array: [1, 2, 3, ., 4, 5]"));
 }
}