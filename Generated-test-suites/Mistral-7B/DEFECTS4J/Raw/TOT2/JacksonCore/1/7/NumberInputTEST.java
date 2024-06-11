package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

 // Test case for parseInt method with valid input
 @Test
 public void testValidIntParse() {
 int num = NumberInput.parseInt(new char[]{'1'}, 0, 1);
 assertEquals(1, num);
 }

 // Test case for parseInt method with multiple digits
 @Test
 public void testMultipleDigitsIntParse() {
 int num = NumberInput.parseInt(new char[]{'1', '2', '3', '4', '5'}, 0, 5);
 assertEquals(12345, num);
 }

 // Test case for parseInt method with negative number
 @Test
 public void testNegativeIntParse() {
 int num = NumberInput.parseInt(new char[]{'-', '1', '2', '3'}, 0, 4);
 assertEquals(-123, num);
 }

 // Test case for parseInt method with input length greater than the number of digits
 @Test
 public void testLongerInputIntParse() {
 int num = NumberInput.parseInt(new char[]{'1', '2', '3', '4', '5', '6'}, 0, 6);
 assertEquals(12345, num);
 }

 // Test case for parseInt method with digits out of order
 @Test
 public void testDigitsOutOfOrderIntParse() {
 int num = NumberInput.parseInt(new char[]{'5', '3', '2', '1'}, 0, 4);
 assertEquals(123, num);
 }

 // Test case for parseInt method with non-digit characters
 @Test
 public void testNonDigitCharactersIntParse() {
 int num = NumberInput.parseInt(new char[]{'1', 'a'}, 0, 2);
 assertEquals(0, num);
 }

 // Test case for parseLong method with valid input
 @Test
 public void testValidLongParse() {
 long num = NumberInput.parseLong(new char[]{'1', '2', '3', '4', '5', '6', '7', '8'}, 0, 8);
 assertEquals(12345678L, num);
 }

 // Test case for parseLong method with negative number
 @Test
 public void testNegativeLongParse() {
 long num = NumberInput.parseLong(new char[]{'-', '1', '2', '3', '4', '5'}, 0, 5);
 assertEquals(-12345, num);
 }

 // Test case for parseLong method with input length greater than the number of digits
 @Test
 public void testLongerInputLongParse() {
 long num = NumberInput.parseLong(new char[]{'1', '2', '3', '4', '5', '6'}, 0, 6);
 assertEquals(12345, num);
 }

 // Test case for parseLong method with digits out of order
 @Test
 public void testDigitsOutOfOrderLongParse() {
 long num = NumberInput.parseLong(new char[]{'5', '3', '2', '1', '6', '7', '8'}, 0, 7);
 assertEquals(1236578, num);
 }

 // Test case for parseLong method with non-digit characters
 @Test
 public void testNonDigitCharactersLongParse() {
 long num = NumberInput.parseLong(new char[]{'1', 'a'}, 0, 2);
 assertEquals(0, num);
 }

 // Test case for inLongRange method with valid input
 @Test
 public void testValidInLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean result = NumberInput.inLongRange(digitChars, 0, 5, false);
 assertTrue(result);
 }

 // Test case for inLongRange method with input length smaller than required
 @Test
 public void testShortInputInLongRange() {
 char[] digitChars = new char[]{'1', '2'};
 boolean result = NumberInput.inLongRange(digitChars, 0, 2, false);
 assertTrue(result);
 }

 // Test case for inLongRange method with input length greater than required
 @Test
 public void testLongerInputInLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
 boolean result = NumberInput.inLongRange(digitChars, 0, 6, false);
 assertFalse(result);
 }

 // Test case for inLongRange method with digits out of order
 @Test
 public void testDigitsOutOfOrderInLongRange() {
 char[] digitChars = new char[]{'5', '3', '2', '1'};
 boolean result = NumberInput.inLongRange(digitChars, 0, 4, false);
 assertFalse(result);
 }

 // Test case for parseAsInt method with valid input
 @Test
 public void testValidParseAsInt() {
 String input = "123";
 int num = NumberInput.parseAsInt(input, 0);
 assertEquals(123, num);
 }

 // Test case for parseAsInt method with default value
 @Test
 public void testDefaultValueParseAsInt() {
 String input = "";
 int num = NumberInput.parseAsInt(input, 42);
 assertEquals(42, num);
 }

 // Test case for parseAsInt method with input containing only whitespace
 @Test
 public void testWhitespaceParseAsInt() {
 String input = "   ";
 int num = NumberInput.parseAsInt(input, 42);
 assertEquals(42, num);
 }

 // Test case for parseAsInt method with input containing non-digit characters
 @Test
 public void testNonDigitCharactersParseAsInt() {
 String input = "1a2b";
 int num = NumberInput.parseAsInt(input, 42);
 assertEquals(42, num);
 }

 // Test case for parseAsLong method with valid input
 @Test
 public void testValidParseAsLong() {
 String input = "12345678901234567890";
 long num = NumberInput.parseAsLong(input, 0L);
 assertEquals(12345678901234567890L, num);
 }

 // Test case for parseAsLong method with default value
 @Test
 public void testDefaultValueParseAsLong() {
 String input = "";
 long num = NumberInput.parseAsLong(input, 42L);
 assertEquals(42L, num);
 }

 // Test case for parseAsLong method with input containing only whitespace
 @Test
 public void testWhitespaceParseAsLong() {
 String input = "   ";
 long num = NumberInput.parseAsLong(input, 42L);
 assertEquals(42L, num);
 }

 // Test case for parseAsLong method with input containing non-digit characters
 @Test
 public void testNonDigitCharactersParseAsLong() {
 String input = "1a2b";
 long num = NumberInput.parseAsLong(input, 42L);
 assertEquals(42L, num);
 }

 // Test case for parseAsDouble method with valid input
 @Test
 public void testValidParseAsDouble() {
 String input = "123.45";
 double num = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(123.45, num, 0.001);
 }

 // Test case for parseAsDouble method with default value
 @Test
 public void testDefaultValueParseAsDouble() {
 String input = "";
 double num = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(42.0, num, 0.001);
 }

 // Test case for parseAsDouble method with input containing only whitespace
 @Test
 public void testWhitespaceParseAsDouble() {
 String input = "   ";
 double num = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(42.0, num, 0.001);
 }

 // Test case for parseAsDouble method with input containing non-digit characters
 @Test
 public void testNonDigitCharactersParseAsDouble() {
 String input = "1a2b.3c";
 double num = NumberInput.parseAsDouble(input, 42.0);
 assertEquals(42.0, num, 0.001);
 }

 // Test case for parseBigDecimal method with valid input
 @Test
 public void testValidParseBigDecimal() {
 String input = "123.45";
 BigDecimal num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("123.45"), num);
 }

 // Test case for parseBigDecimal method with input containing only whitespace
 @Test
 public void testWhitespaceParseBigDecimal() {
 String input = "   ";
 try {
 NumberInput.parseBigDecimal(input);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 // Test case for parseBigDecimal method with non-digit characters
 @Test
 public void testNonDigitCharactersParseBigDecimal() {
 String input = "1a2b.3c";
 try {
 NumberInput.parseBigDecimal(input);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 // Test case for parseBigDecimal method with valid input in char array
 @Test
 public void testValidParseBigDecimalCharArray() {
 char[] input = new char[]{'1', '2', '.', '3', '4', '5'};
 BigDecimal num = NumberInput.parseBigDecimal(input, 0, 6);
 assertEquals(new BigDecimal("1.2345"), num);
 }

 // Test case for parseBigDecimal method with input length greater than the number of digits
 @Test
 public void testLongerInputParseBigDecimal() {
 char[] input = new char[]{'1', '2', '.', '3', '4', '5', '6'};
 try {
 NumberInput.parseBigDecimal(input, 0, 7);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 // Test case for parseBigDecimal method with digits out of order
 @Test
 public void testDigitsOutOfOrderParseBigDecimal() {
 char[] input = new char[]{'5', '3', '2', '1', '.', '4', '5'};
 try {
 NumberInput.parseBigDecimal(input, 0, 7);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 // Test case for parseBigDecimal method with negative number
 @Test
 public void testNegativeParseBigDecimal() {
 String input = "-123.45";
 BigDecimal num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("-123.45"), num);
 }
}