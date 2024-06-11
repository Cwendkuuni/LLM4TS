package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_ValidInput() {
 char[] validIntChars = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(validIntChars, 0, validIntChars.length);
 assertEquals("Unexpected number parsed from valid input", expected, actual);
 }

 @Test
 public void testParseInt_ValidInputWithOffset() {
 char[] validIntChars = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(validIntChars, 2, 3);
 assertEquals("Unexpected number parsed from valid input with offset", 3, actual);
 }

 @Test
 public void testParseInt_ValidInputString() {
 String validInput = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(validInput);
 assertEquals("Unexpected number parsed from valid input string", expected, actual);
 }

 @Test
 public void testParseInt_NegativeInput() {
 char[] negativeIntChars = { '-', '1', '2', '3', '4', '5' };
 int expected = -12345;
 int actual = NumberInput.parseInt(negativeIntChars, 0, negativeIntChars.length);
 assertEquals("Unexpected number parsed from negative input", expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_InvalidInput() {
 char[] invalidInputChars = { 'a', 'b', 'c', 'd' };
 NumberInput.parseInt(invalidInputChars, 0, invalidInputChars.length);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_InvalidInputString() {
 String invalidInput = "abcd";
 NumberInput.parseInt(invalidInput);
 }

 @Test
 public void testParseLong_ValidInput() {
 char[] validLongChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(validLongChars, 0, validLongChars.length);
 assertEquals("Unexpected long parsed from valid input", expected, actual);
 }

 @Test
 public void testParseLong_ValidInputString() {
 String validInput = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(validInput);
 assertEquals("Unexpected long parsed from valid input string", expected, actual);
 }

 @Test
 public void testParseLong_ValidInputWithOffset() {
 char[] validLongChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(validLongChars, 2, 8);
 assertEquals("Unexpected long parsed from valid input with offset", 12345678, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidInput() {
 char[] invalidInputChars = { 'a', 'b', 'c', 'd' };
 NumberInput.parseLong(invalidInputChars, 0, invalidInputChars.length);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidInputString() {
 String invalidInput = "abcd";
 NumberInput.parseLong(invalidInput);
 }

 @Test
 public void testParseLong_OutOfRangeInput() {
 char[] maxLongInputChars = NumberInput.MAX_LONG_STR.toCharArray();
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(maxLongInputChars, false);
 assertEquals("Unexpected result from long range test for max long input", expected, actual);
 }

 @Test
 public void testParseLong_OutOfRangeInputString() {
 String maxLongInputStr = NumberInput.MAX_LONG_STR;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(maxLongInputStr, false);
 assertEquals("Unexpected result from long range test for max long input string", expected, actual);
 }

 @Test
 public void testParseLong_InRangeInput() {
 char[] minLongInputChars = NumberInput.MIN_LONG_STR_NO_SIGN.toCharArray();
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(minLongInputChars, false);
 assertEquals("Unexpected result from long range test for min long input", expected, actual);
 }

 @Test
 public void testParseLong_InRangeInputString() {
 String minLongInputStr = NumberInput.MIN_LONG_STR_NO_SIGN;
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(minLongInputStr, false);
 assertEquals("Unexpected result from long range test for min long input string", expected, actual);
 }

 @Test
 public void testParseAsInt_ValidInput() {
 String validInput = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(validInput, 0);
 assertEquals("Unexpected int parsed from valid input", expected, actual);
 }

 @Test
 public void testParseAsInt_ValidInputWithDefaultValue() {
 String validInput = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(validInput, -1);
 assertEquals("Unexpected int parsed from valid input with default value", expected, actual);
 }

 @Test
 public void testParseAsInt_InvalidInput() {
 String invalidInput = "abcd";
 int expected = -1;
 int actual = NumberInput.parseAsInt(invalidInput, 1);
 assertEquals("Unexpected int parsed from invalid input with default value", expected, actual);
 }

 @Test
 public void testParseAsLong_ValidInput() {
 String validInput = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong(validInput, 0);
 assertEquals("Unexpected long parsed from valid input", expected, actual);
 }

 @Test
 public void testParseAsLong_ValidInputWithDefaultValue() {
 String validInput = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong(validInput, -1);
 assertEquals("Unexpected long parsed from valid input with default value", expected, actual);
 }

 @Test
 public void testParseAsLong_InvalidInput() {
 String invalidInput = "abcd";
 long expected = -1;
 long actual = NumberInput.parseAsLong(invalidInput, 1);
 assertEquals("Unexpected long parsed from invalid input with default value", expected, actual);
 }

 @Test
 public void testParseBigDecimal_ValidInput() {
 String validInput = "12345.6789";
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(validInput);
 assertEquals("Unexpected BigDecimal parsed from valid input", expected, actual);
 }

 @Test
 public void testParseBigDecimal_ValidInputWithDefaultValue() {
 String validInput = "12345.6789";
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(validInput, new BigDecimal("0"));
 assertEquals("Unexpected BigDecimal parsed from valid input with default value", expected, actual);
 }

 @Test
 public void testParseBigDecimal_InvalidInput() {
 String invalidInput = "abcd";
 BigDecimal expected = null;
 BigDecimal actual = NumberInput.parseBigDecimal(invalidInput);
 assertEquals("Unexpected BigDecimal parsed from invalid input", expected, actual);
 }

 @Test
 public void testParseBigDecimal_ValidInputWithOffset() {
 char[] validInputChars = {'1', '2', '3', '4', '5', '.', '6', '7', '8', '9'};
 int offset = 6;
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(validInputChars, 0, validInputChars.length, offset);
 assertEquals("Unexpected BigDecimal parsed from valid input with offset", expected, actual);
 }

 @Test
 public void testParseBigDecimal_ValidInputWithLength() {
 char[] validInputChars = {'1', '2', '3', '4', '5', '.', '6', '7', '8', '9'};
 int length = 9;
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(validInputChars, 0, validInputChars.length, 0, length);
 assertEquals("Unexpected BigDecimal parsed from valid input with length", expected, actual);
 }
}