package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NumberInputTest {

 @Test
 public void testParseInt_validInput_positive() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_validInput_negative() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_emptyInput() {
 char[] digitChars = new char[0];
 int actual = NumberInput.parseInt(digitChars, 0, 0);
 assertEquals(0, actual);
 }

 @Test
 public void testParseInt_invalidInput_longerThanInt() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
 assertThrows(IllegalArgumentException.class, () -> NumberInput.parseInt(digitChars, 0, 6));
 }

 @Test
 public void testParseInt_invalidInput_nonDigit() {
 char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
 assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitChars, 0, 5));
 }

 @Test
 public void testParseLong_validInput_positive() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_validInput_negative() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_emptyInput() {
 char[] digitChars = new char[0];
 long actual = NumberInput.parseLong(digitChars, 0, 0);
 assertEquals(0, actual);
 }

 @Test
 public void testParseLong_invalidInput_longerThanLong() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '1'};
 assertThrows(IllegalArgumentException.class, () -> NumberInput.parseLong(digitChars, 0, 11));
 }

 @Test
 public void testParseLong_invalidInput_nonDigit() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'a'};
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digitChars, 0, 6));
 }

 @Test
 public void testParseLong_largeInput_parsesCorrectly() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '1', '5', '9', '2', '1', '4', '7', '4', '8', '0', '9', '8', '7'};
 long expected = 12341592147480987L;
 long actual = NumberInput.parseLong(digitChars, 0, 20);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRange_validInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 assertTrue(NumberInput.inLongRange(digitChars, 0, 5, false));
 assertTrue(NumberInput.inLongRange("12345", false));
 }

 @Test
 public void testInLongRange_invalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
 assertFalse(NumberInput.inLongRange(digitChars, 0, 5, false));
 assertFalse(NumberInput.inLongRange("123456", false));
 }

 @Test
 public void testParseAsInt_validInput() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_invalidInput() {
 String input = "123a45";
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(0, actual);
 }

 @Test
 public void testParseAsLong_validInput() {
 String input = "12345678901234567890";
 long expected = 12345678901234567890L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_invalidInput() {
 String input = "123a4567890";
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(0, actual);
 }

 @Test
 public void testParseAsDouble_validInput() {
 String input = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testParseAsDouble_invalidInput() {
 String input = "123a45.67";
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(0, actual, 0.0001);
 }

 @Test
 public void testParseBigDecimal_validInput() {
 String input = "1234567890.1234567890";
 BigDecimal expected = new BigDecimal("1234567890.1234567890");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_invalidInput() {
 String input = "123a4567890.";
 assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(input));
 }
}