package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithValidInputFromString() {
 String str = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithNegativeSign() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithNegativeSignFromString() {
 String str = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithLengthLessThanOne() {
 char[] digitChars = {'1'};
 int expected = NumberInput.parseInt(digitChars, 0, 0);
 assertEquals(0, expected);
 }

 @Test
 public void testParseIntWithLengthGreaterThanInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = {'1', 'a'};
 NumberInput.parseInt(digitChars, 0, 2);
 }

 @Test
 public void testParseIntWithInvalidInputFromString() {
 String str = "1a";
 try {
 NumberInput.parseInt(str);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidInputFromString() {
 String str = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithNegativeSign() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithNegativeSignFromString() {
 String str = "-1234567890";
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithLengthLessThanTen() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 long expected = NumberInput.parseLong(digitChars, 0, 5);
 assertEquals(0L, expected);
 }

 @Test
 public void testParseLongWithLengthGreaterThanInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 long expected = 12345678901L;
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = {'1', 'a'};
 NumberInput.parseLong(digitChars, 0, 2);
 }

 @Test
 public void testParseLongWithInvalidInputFromString() {
 String str = "1a";
 try {
 NumberInput.parseLong(str);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 7, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeInput() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 8, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithInputGreaterThanMaxLong() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '0'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 11, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithStringInput() {
 String str = "12345678910";
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(str.toCharArray(), 0, 11, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithNegativeInput() {
 String input = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsIntWithInvalidInput() {
 String input = "1a";
 NumberInput.parseAsInt(input, 0);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = "123a";
 int expected = 123;
 int actual = NumberInput.parseAsInt(input, 45);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong(input, 0L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithNegativeInput() {
 String input = "-1234567890";
 long expected = -1234567890L;
 long actual = NumberInput.parseAsLong(input, 0L);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLongWithInvalidInput() {
 String input = "1a";
 NumberInput.parseAsLong(input, 0L);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = "123a";
 long expected = 123;
 long actual = NumberInput.parseAsLong(input, 12345L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.456";
 double expected = 123.456;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.00001);
 }

 @Test
 public void testParseAsDoubleWithNegativeInput() {
 String input = "-123.456";
 double expected = -123.456;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.00001);
 }

 @Test
 public void testParseAsDoubleWithInvalidInput() {
 String input = "1a";
 NumberInput.parseAsDouble(input, 0);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String input = "123a";
 double expected = 123;
 double actual = NumberInput.parseAsDouble(input, 56.78);
 assertEquals(expected, actual, 0.00001);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String input = "123.456";
 BigDecimal expected = new BigDecimal("123.456");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithNegativeInput() {
 String input = "-123.456";
 BigDecimal expected = new BigDecimal("-123.456");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithInvalidInput() {
 String input = "1a";
 try {
 NumberInput.parseBigDecimal(input);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseBigDecimalWithDefaultValue() {
 String input = "123a";
 BigDecimal expected = new BigDecimal("123");
 BigDecimal actual = NumberInput.parseBigDecimal(input, new BigDecimal("56.78"));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayInput() {
 char[] input = {'1', '2', '3', '.', '4', '5', '6'};
 BigDecimal expected = new BigDecimal("123.456");
 BigDecimal actual = NumberInput.parseBigDecimal(input, 0, 7);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayNegativeInput() {
 char[] input = {'-', '1', '2', '3', '.', '4', '5', '6'};
 BigDecimal expected = new BigDecimal("-123.456");
 BigDecimal actual = NumberInput.parseBigDecimal(input, 0, 8);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayInvalidInput() {
 char[] input = {'1', 'a'};
 try {
 NumberInput.parseBigDecimal(input, 0, 1);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }
}