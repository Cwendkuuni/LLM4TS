package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test
 public void testParseIntWithEmptyInput() {
 char[] digitChars = new char[0];
 int actual = NumberInput.parseInt(digitChars, 0, 0);
 assertEquals("Unexpected integer value", 0, actual);
 }

 @Test
 public void testParseIntWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', 'x', '4', '5'};
 NumberInput.parseInt(digitChars, 0, 5);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 123456789;
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test
 public void testParseLongWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = -123456789;
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', 'x', '4', '5', '6', '7', '8', '9'};
 NumberInput.parseLong(digitChars, 0, 10);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInputLongerThanMaxLength() {
 char[] digitChars = new char[19]; // Long.MAX_VALUE is 18 digits
 NumberInput.parseLong(digitChars, 0, 19);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test
 public void testParseAsIntWithEmptyInput() {
 String input = "";
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals("Unexpected integer value", 0, actual);
 }

 @Test
 public void testParseAsIntWithNegativeInput() {
 String input = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals("Unexpected integer value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsIntWithInvalidInput() {
 String input = "123x45";
 NumberInput.parseAsInt(input, 0);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test
 public void testParseAsLongWithEmptyInput() {
 String input = "";
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals("Unexpected long value", 0, actual);
 }

 @Test
 public void testParseAsLongWithNegativeInput() {
 String input = "-1234567890123";
 long expected = -1234567890123L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals("Unexpected long value", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLongWithInvalidInput() {
 String input = "123x45";
 NumberInput.parseAsLong(input, 0);
 }

 @Test
 public void testParseAsLongWithLongerInputThanMaxLength() {
 String input = "12345678901234567890"; // Long.MAX_VALUE is 18 digits
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals("Unexpected long value", Long.MAX_VALUE, actual);
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 5, false);
 assertEquals("Unexpected boolean value", expected, actual);
 }

 @Test
 public void testInLongRangeWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', 'x', '4', '5'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 6, false);
 assertEquals("Unexpected boolean value", expected, actual);
 }

 @Test
 public void testInLongRangeWithLongerInputThanMaxLength() {
 char[] digitChars = new char[19];
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 19, false);
 assertEquals("Unexpected boolean value", expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeInput() {
 String numberStr = "-12345";
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(numberStr, false);
 assertEquals("Unexpected boolean value", expected, actual);
 }

 @Test
 public void testInLongRangeWithLongerNegativeInput() {
 String numberStr = "-1234567890123";
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(numberStr, false);
 assertEquals("Unexpected boolean value", expected, actual);
 }

 @Test
 public void testInLongRangeWithLongerPositiveInput() {
 String numberStr = "12345678901234567890";
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(numberStr, false);
 assertEquals("Unexpected boolean value", expected, actual);
 }
}