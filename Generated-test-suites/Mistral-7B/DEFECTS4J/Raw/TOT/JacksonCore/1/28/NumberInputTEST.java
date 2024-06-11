package com.fasterxml.jackson.core.io;

import org.junit.Assert;
import org.junit.Test;

public class NumberInputTest {

 @Test
 public void testParseInt_SmallPositiveNumber() {
 char[] digitChars = {'1', '2', '3'};
 int expected = 123;
 int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
 Assert.assertEquals("Incorrect integer parsed from character array", expected, result);
 }

 @Test
 public void testParseInt_LargePositiveNumber() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 123456789;
 long result = NumberInput.parseLong(digitChars, 0, digitChars.length) / 1000000000L;
 Assert.assertEquals("Incorrect integer parsed from character array as long and then divided by billion", expected, result);
 }

 @Test
 public void testParseInt_NegativeNumber() {
 char[] digitChars = {'-', '1', '2', '3'};
 int expected = -123;
 int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
 Assert.assertEquals("Incorrect negative integer parsed from character array", expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_NonIntegerString() {
 String nonIntegerStr = "abcdef";
 NumberInput.parseInt(nonIntegerStr.toCharArray(), 0, nonIntegerStr.length());
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_InputLongerThanInt() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 }

 @Test
 public void testParseLong_SmallPositiveNumber() {
 char[] digitChars = {'1', '2', '3'};
 long expected = 123;
 long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
 Assert.assertEquals("Incorrect long parsed from character array", expected, result);
 }

 @Test
 public void testParseLong_LargePositiveNumber() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 123456789123;
 long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
 Assert.assertEquals("Incorrect large positive long parsed from character array", expected, result);
 }

 @Test
 public void testParseLong_NegativeNumber() {
 char[] digitChars = {'-', '1', '2', '3'};
 long expected = -123;
 long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
 Assert.assertEquals("Incorrect negative long parsed from character array", expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_NonLongString() {
 String nonLongStr = "abcd";
 NumberInput.parseLong(nonLongStr.toCharArray(), 0, nonLongStr.length());
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InputLongerThanLong() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 }

 @Test
 public void testParseLong_ParseLongFromString() {
 String longStr = "123456789123";
 long expected = 123456789123;
 long result = NumberInput.parseLong(longStr);
 Assert.assertEquals("Incorrect long parsed from string", expected, result);
 }

 @Test
 public void testParseLong_ParseLongFromStringWithLeadingMinusSign() {
 String longStr = "-123456789123";
 long expected = -123456789123;
 long result = NumberInput.parseLong(longStr);
 Assert.assertEquals("Incorrect negative long parsed from string", expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_StringLongerThanLong() {
 String longStr = "123456789123456789123";
 }

 @Test
 public void testInLongRange_true() {
 char[] digitChars = {'1', '2', '3'};
 boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
 Assert.assertTrue("InLongRange should return true for small positive integers", result);
 }

 @Test
 public void testInLongRange_false() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length, false);
 Assert.assertFalse("InLongRange should return false for integers larger than long range", result);
 }

 @Test
 public void testInLongRange_trueWithNegative() {
 char[] digitChars = {'-', '1', '2', '3'};
 boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length, true);
 Assert.assertTrue("InLongRange should return true for negative numbers within long range", result);
 }

 @Test
 public void testInLongRange_falseWithNegative() {
 char[] digitChars = {'-', '1', '2', '3', '4'};
 boolean result = NumberInput.inLongRange(digitChars, 0, digitChars.length, true);
 Assert.assertFalse("InLongRange should return false for negative numbers larger than long range", result);
 }

 @Test
 public void testInLongRange_trueWithLongStrings() {
 String numberStr = "123";
 boolean result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), false);
 Assert.assertTrue("InLongRange should return true for strings representing small positive integers", result);
 }

 @Test
 public void testInLongRange_falseWithLongStrings() {
 String numberStr = "123456789123456789123";
 boolean result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), false);
 Assert.assertFalse("InLongRange should return false for strings representing integers larger than long range", result);
 }

 @Test
 public void testInLongRange_trueWithNegativeLongStrings() {
 String numberStr = "-123";
 boolean result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), true);
 Assert.assertTrue("InLongRange should return true for negative strings within long range", result);
 }

 @Test
 public void testInLongRange_falseWithNegativeLongStrings() {
 String numberStr = "-123456789123456789123";
 boolean result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), true);
 Assert.assertFalse("InLongRange should return false for negative strings larger than long range", result);
 }

 @Test
 public void testParseAsInt_ValidInput() {
 String input = "123";
 int expected = 123;
 int result = NumberInput.parseAsInt(input, expected);
 Assert.assertEquals("Incorrect integer parsed from string", expected, result);
 }

 @Test
 public void testParseAsInt_EmptyString() {
 String input = "";
 int defaultValue = 0;
 int result = NumberInput.parseAsInt(input, defaultValue);
 Assert.assertEquals("Default value returned as empty string is empty", defaultValue, result);
 }

 @Test
 public void testParseAsInt_InputLongerThanInt() {
 String input = "123456789123";
 int defaultValue = 0;
 int result = NumberInput.parseAsInt(input, defaultValue);
 Assert.assertEquals("Default value returned as input larger than int", defaultValue, result);
 }

 @Test
 public void testParseAsInt_NonNumericString() {
 String input = "abc";
 int defaultValue = 0;
 int result = NumberInput.parseAsInt(input, defaultValue);
 Assert.assertEquals("Default value returned as non-numeric string", defaultValue, result);
 }

 @Test
 public void testParseAsLong_ValidInput() {
 String input = "123456789123";
 long expected = 123456789123;
 long result = NumberInput.parseAsLong(input, expected);
 Assert.assertEquals("Incorrect long parsed from string", expected, result);
 }

 @Test
 public void testParseAsLong_EmptyString() {
 String input = "";
 long defaultValue = 0;
 long result = NumberInput.parseAsLong(input, defaultValue);
 Assert.assertEquals("Default value returned as empty string is empty", defaultValue, result);
 }

 @Test
 public void testParseAsLong_InputLongerThanLong() {
 String input = "123456789123456789123";
 long defaultValue = 0;
 long result = NumberInput.parseAsLong(input, defaultValue);
 Assert.assertEquals("Default value returned as input larger than long", defaultValue, result);
 }

 @Test
 public void testParseAsLong_NonNumericString() {
 String input = "abc";
 long defaultValue = 0;
 long result = NumberInput.parseAsLong(input, defaultValue);
 Assert.assertEquals("Default value returned as non-numeric string", defaultValue, result);
 }

 @Test
 public void testParseAsDouble_ValidInput() {
 String input = "123.45";
 double expected = 123.45;
 double result = NumberInput.parseAsDouble(input, expected);
 Assert.assertEquals("Incorrect double parsed from string", expected, result, 0.0001);
 }

 @Test
 public void testParseAsDouble_EmptyString() {
 String input = "";
 double defaultValue = 0;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 Assert.assertEquals("Default value returned as empty string is empty", defaultValue, result);
 }

 @Test
 public void testParseAsDouble_InputLongerThanDouble() {
 String input = "123456789123.456789123";
 double defaultValue = 0;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 Assert.assertEquals("Default value returned as input larger than double", defaultValue, result);
 }

 @Test
 public void testParseAsDouble_NonNumericString() {
 String input = "abc";
 double defaultValue = 0;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 Assert.assertEquals("Default value returned as non-numeric string", defaultValue, result);
 }

 @Test
 public void testParseBigDecimal_ValidInput() {
 String input = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input);
 Assert.assertEquals("Incorrect BigDecimal parsed from string", expected, result);
 }

 @Test
 public void testParseBigDecimal_EmptyString() {
 String input = "";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal result = NumberInput.parseBigDecimal(input);
 Assert.assertEquals("Default value returned as empty string is zero", defaultValue, result);
 }

 @Test
 public void testParseBigDecimal_InputLongerThanBigDecimal() {
 String input = "123456789123.456789123";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal result = NumberInput.parseBigDecimal(input);
 Assert.assertEquals("Default value returned as input larger than BigDecimal", defaultValue, result);
 }

 @Test
 public void testParseBigDecimal_NonNumericString() {
 String input = "abc";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal result = NumberInput.parseBigDecimal(input);
 Assert.assertEquals("Default value returned as non-numeric string is zero", defaultValue, result);
 }

 @Test
 public void testParseBigDecimal_CharacterArray() {
 char[] input = {'1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input, 0, input.length);
 Assert.assertEquals("Incorrect BigDecimal parsed from character array", expected, result);
 }

 @Test
 public void testParseBigDecimal_CharacterArrayWithLength() {
 char[] input = {'1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input, 0, 6);
 Assert.assertEquals("Incorrect BigDecimal parsed from character array with specified length", expected, result);
 }

 @Test
 public void testParseBigDecimal_CharacterArrayWithNegativeSign() {
 char[] input = {'-', '1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("-123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input, 0, input.length);
 Assert.assertEquals("Incorrect BigDecimal parsed from character array with negative sign", expected, result);
 }

 @Test
 public void testParseBigDecimal_CharacterArrayWithNegativeSignAndLength() {
 char[] input = {'-', '1', '2', '3', '.', '4', '5'};
 BigDecimal expected = new BigDecimal("-123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input, 0, 6);
 Assert.assertEquals("Incorrect BigDecimal parsed from character array with negative sign and specified length", expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_MalformedInput() {
 char[] input = {'1', '2', 'x'};
 NumberInput.parseBigDecimal(input, 0, input.length);
 }
}