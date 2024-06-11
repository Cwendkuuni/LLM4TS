package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test
 public void testParseIntWithNegativeSign() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntInvalidLength() {
 char[] digitChars = new char[]{'1', '2'};
 NumberInput.parseInt(digitChars, 0, 2);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseIntInvalidCharacters() {
 char[] digitChars = new char[]{'1', '2', 'x', '3', '4'};
 int expected = 1234;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test
 public void testParseIntWithLeadingZeros() {
 char[] digitChars = new char[]{'0', '0', '1', '2', '3', '4'};
 int expected = 1234;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test
 public void testParseIntMaxValue() {
 char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9'};
 long maxValue = Long.MAX_VALUE;
 long actual = NumberInput.parseInt(digitChars, 0, 11);
 assertEquals("Unexpected long value", maxValue, actual);
 }

 @Test
 public void testParseIntMinValue() {
 char[] digitChars = new char[]{'-', '9', '2', '2', '3', '5', '0', '7', '3', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5'};
 long minValue = Long.MIN_VALUE;
 long actual = NumberInput.parseInt(digitChars, 0, 31);
 assertEquals("Unexpected long value", minValue, actual);
 }

 @Test
 public void testParseLong() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
 long expected = 12345678;
 long actual = NumberInput.parseLong(digitChars, 0, 8);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test
 public void testParseLongWithNegativeSign() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 long expected = -12345678;
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLongInvalidLength() {
 char[] digitChars = new char[]{'1', '2'};
 NumberInput.parseLong(digitChars, 0, 2);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseLongInvalidCharacters() {
 char[] digitChars = new char[]{'1', '2', 'x', '3', '4'};
 long expected = 1234;
 long actual = NumberInput.parseLong(digitChars, 0, 5);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test
 public void testParseLongWithLeadingZeros() {
 char[] digitChars = new char[]{'0', '0', '1', '2', '3', '4', '5', '6'};
 long expected = 123456;
 long actual = NumberInput.parseLong(digitChars, 0, 8);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test
 public void testParseLongMaxValue() {
 char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9'};
 long maxValue = Long.MAX_VALUE;
 long actual = NumberInput.parseLong(digitChars, 0, 19);
 assertEquals("Unexpected long value", maxValue, actual);
 }

 @Test
 public void testParseLongMinValue() {
 char[] digitChars = new char[]{'-', '9', '2', '2', '3', '5', '0', '7', '3', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5', '8', '5'};
 long minValue = Long.MIN_VALUE;
 long actual = NumberInput.parseLong(digitChars, 0, 31);
 assertEquals("Unexpected long value", minValue, actual);
 }

 @Test
 public void testIsInLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean inRange = NumberInput.inLongRange(digitChars, 0, 5, false);
 assertTrue("Unexpected result", inRange);

 char[] digitCharsNegative = new char[]{'-', '1', '2', '3', '4', '5'};
 boolean inRangeNegative = NumberInput.inLongRange(digitCharsNegative, 0, 5, true);
 assertTrue("Unexpected result", inRangeNegative);

 char[] digitCharsTooShort = new char[]{'1', '2'};
 boolean inRangeTooShort = NumberInput.inLongRange(digitCharsTooShort, 0, 2, false);
 assertTrue("Unexpected result", inRangeTooShort);

 char[] digitCharsTooLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 boolean inRangeTooLong = NumberInput.inLongRange(digitCharsTooLong, 0, 10, false);
 assertFalse("Unexpected result", inRangeTooLong);

 char[] digitCharsInvalid = new char[]{'1', 'x', '2', '3', '4'};
 boolean inRangeInvalid = NumberInput.inLongRange(digitCharsInvalid, 0, 5, false);
 assertFalse("Unexpected result", inRangeInvalid);
 }

 @Test
 public void testIsInLongRangeString() {
 String numberStr = "12345";
 boolean inRange = NumberInput.inLongRange(numberStr, false);
 assertTrue("Unexpected result", inRange);

 String numberStrNegative = "-12345";
 boolean inRangeNegative = NumberInput.inLongRange(numberStrNegative, true);
 assertTrue("Unexpected result", inRangeNegative);

 String numberStrTooShort = "12";
 boolean inRangeTooShort = NumberInput.inLongRange(numberStrTooShort, false);
 assertTrue("Unexpected result", inRangeTooShort);

 String numberStrTooLong = "12345678901234567890";
 boolean inRangeTooLong = NumberInput.inLongRange(numberStrTooLong, false);
 assertFalse("Unexpected result", inRangeTooLong);

 String numberStrInvalid = "12x345";
 boolean inRangeInvalid = NumberInput.inLongRange(numberStrInvalid, false);
 assertFalse("Unexpected result", inRangeInvalid);
 }

 @Test
 public void testParseAsInt() {
 String input = "12345";
 int defaultValue = 0;
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, defaultValue);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test
 public void testParseAsIntWithNegativeSign() {
 String input = "-12345";
 int defaultValue = 0;
 int expected = -12345;
 int actual = NumberInput.parseAsInt(input, defaultValue);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsIntInvalidInput() {
 String input = "abcd";
 int defaultValue = 0;
 NumberInput.parseAsInt(input, defaultValue);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseAsLong() {
 String input = "12345678901234567890";
 long defaultValue = 0;
 long expected = 12345678901234567890L;
 long actual = NumberInput.parseAsLong(input, defaultValue);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test
 public void testParseAsLongWithNegativeSign() {
 String input = "-12345678901234567890";
 long defaultValue = 0;
 long expected = -12345678901234567890L;
 long actual = NumberInput.parseAsLong(input, defaultValue);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLongInvalidInput() {
 String input = "abcd";
 long defaultValue = 0;
 NumberInput.parseAsLong(input, defaultValue);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseAsDouble() {
 String input = "123.45";
 double defaultValue = 0.0;
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals("Unexpected double value", expected, actual, 0.001);
 }

 @Test
 public void testParseAsDoubleWithNegativeSign() {
 String input = "-123.45";
 double defaultValue = 0.0;
 double expected = -123.45;
 double actual = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals("Unexpected double value", expected, actual, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDoubleInvalidInput() {
 String input = "abcd";
 double defaultValue = 0.0;
 NumberInput.parseAsDouble(input, defaultValue);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseBigDecimal() {
 String input = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals("Unexpected BigDecimal value", expected, actual);
 }

 @Test
 public void testParseBigDecimalWithNegativeSign() {
 String input = "-123.45";
 BigDecimal expected = new BigDecimal("-123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals("Unexpected BigDecimal value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalInvalidInput() {
 String input = "abcd";
 NumberInput.parseBigDecimal(input);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseBigDecimalFromCharArray() {
 char[] input = new char[]{'1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input, 0, 6);
 assertEquals("Unexpected BigDecimal value", expected, actual);
 }

 @Test
 public void testParseBigDecimalFromCharArrayWithNegativeSign() {
 char[] input = new char[]{'-', '1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("-123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input, 0, 7);
 assertEquals("Unexpected BigDecimal value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalFromCharArrayInvalidInput() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
 NumberInput.parseBigDecimal(input, 0, 5);
 fail("Expected NumberFormatException");
 }

}