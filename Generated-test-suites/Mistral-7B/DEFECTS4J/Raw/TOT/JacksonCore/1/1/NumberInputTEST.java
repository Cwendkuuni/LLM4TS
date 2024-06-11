package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntFromCharArray_smallPositiveNumber() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromCharArray_largePositiveNumber() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '9'};
 long expected = 1234567899L;
 long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromCharArray_negativeNumber() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromCharArray_invalidInput() {
 char[] digitChars = new char[] {'1', 'a', '3', '4', '5'};
 int expected = -1;
 int actual = NumberInput.parseInt(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromString_smallPositiveNumber() {
 String strNum = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromString_largePositiveNumber() {
 String strNum = "1234567899";
 long expected = 1234567899L;
 long actual = NumberInput.parseLong(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromString_negativeNumber() {
 String strNum = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseInt(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntFromString_invalidInput() {
 String strNum = "12a345";
 int expected = -1;
 int actual = NumberInput.parseInt(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromCharArray_smallNumber() {
 char[] digitChars = new char[] {'1', '2', '3'};
 long expected = 123L;
 long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromCharArray_largeNumber() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9'};
 long expected = 1234567899999L;
 long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromCharArray_negativeNumber() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5'};
 long expected = -12345L;
 long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromCharArray_invalidInput() {
 char[] digitChars = new char[] {'1', 'a', '3', '4', '5'};
 long expected = -1L;
 long actual = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromString_smallNumber() {
 String strNum = "123";
 long expected = 123L;
 long actual = NumberInput.parseLong(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromString_largeNumber() {
 String strNum = "1234567899999999999";
 long expected = 1234567899999999999L;
 long actual = NumberInput.parseLong(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromString_negativeNumber() {
 String strNum = "-12345";
 long expected = -12345L;
 long actual = NumberInput.parseLong(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongFromString_invalidInput() {
 String strNum = "12a345";
 long expected = -1L;
 long actual = NumberInput.parseLong(strNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRange_validNumbers() {
 char[] num1 = new char[] {'1', '2', '3', '4'};
 char[] num2 = new char[] {'9', '9', '9', '9'};
 String num3 = "999999999999999999";
 assertTrue(NumberInput.inLongRange(num1, false));
 assertTrue(NumberInput.inLongRange(num2, false));
 assertTrue(NumberInput.inLongRange(num3, false));
 }

 @Test
 public void testInLongRange_invalidNumbers() {
 char[] num1 = new char[] {'1', '2', '3', '5'};
 char[] num2 = new char[] {'9', '9', '9', '10'};
 String num3 = "10000000000000000001";
 assertFalse(NumberInput.inLongRange(num1, false));
 assertFalse(NumberInput.inLongRange(num2, false));
 assertFalse(NumberInput.inLongRange(num3, false));
 }

 @Test
 public void testInLongRange_negativeNumbers() {
 char[] num1 = new char[] {'-', '1', '2', '3', '4'};
 String num2 = "-12345";
 assertTrue(NumberInput.inLongRange(num1, true));
 assertTrue(NumberInput.inLongRange(num2, true));
 }

 @Test
 public void testParseBigDecimalFromCharArray() {
 char[] digitChars = new char[] {'1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalFromString() {
 String strNum = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(strNum);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalFromInvalidInput() {
 char[] invalidInput = new char[] {'1', 'a', '3', '4', '5'};
 NumberInput.parseBigDecimal(invalidInput, 0, invalidInput.length);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalFromInvalidInputString() {
 String invalidInput = "12a345";
 NumberInput.parseBigDecimal(invalidInput);
 }
}