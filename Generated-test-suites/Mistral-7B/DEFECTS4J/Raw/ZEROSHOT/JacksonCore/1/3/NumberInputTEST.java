package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int len = 3;
 int expected = 123;
 int actual = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInputLength() {
 char[] digitChars = new char[]{'1', '2'};
 int len = 2;
 NumberInput.parseInt(digitChars, 0, len); // Should throw NumberFormatException
 }

 @Test
 public void testParseIntWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3'};
 int len = 4;
 int expected = -123;
 int actual = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithNegativeInputNoMinusSign() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int len = 3;
 NumberInput.parseInt(digitChars, 0, len); // Should throw NumberFormatException
 }

 @Test
 public void testParseIntWithOverflowInputLength() {
 char[] digitChars = new char[NumberInput.MAX_LONG_STR.length() + 1];
 int offset = 0;
 int len = NumberInput.MAX_LONG_STR.length() + 1;
 NumberInput.parseInt(digitChars, offset, len); // Should throw NumberFormatException
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int len = 10;
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInputLength() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int len = 5;
 NumberInput.parseLong(digitChars, 0, len); // Should throw NumberFormatException
 }

 @Test
 public void testParseLongWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int len = 6;
 long expected = -123456;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithNegativeInputNoMinusSign() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int len = 5;
 NumberInput.parseLong(digitChars, 0, len); // Should throw NumberFormatException
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithOverflowInputLength() {
 char[] digitChars = new char[NumberInput.MAX_LONG_STR.length() + 1];
 int offset = 0;
 int len = NumberInput.MAX_LONG_STR.length() + 1;
 NumberInput.parseLong(digitChars, offset, len); // Should throw NumberFormatException
 }

 @Test
 public void testParseLongAsIntWithValidInput() {
 String str = "123";
 int expected = 123;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongAsIntWithInvalidInput() {
 String str = "123a";
 NumberInput.parseInt(str); // Should throw NumberFormatException
 }

 @Test
 public void testParseLongAsLongWithValidInput() {
 String str = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongAsLongWithInvalidInput() {
 String str = "123a";
 NumberInput.parseLong(str); // Should throw NumberFormatException
 }

 @Test
 public void testParseDoubleWithValidInput() {
 String str = "1.23";
 double expected = 1.23;
 double actual = NumberInput.parseDouble(str);
 assertEquals(expected, actual, 0.001); // Allow for a tiny error
 }

 @Test(expected = NumberFormatException.class)
 public void testParseDoubleWithInvalidInput() {
 String str = "1.23a";
 NumberInput.parseDouble(str); // Should throw NumberFormatException
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String str = "1.23";
 BigDecimal expected = new BigDecimal("1.23");
 BigDecimal actual = NumberInput.parseBigDecimal(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInput() {
 String str = "1.23a";
 NumberInput.parseBigDecimal(str); // Should throw NumberFormatException
 }

}