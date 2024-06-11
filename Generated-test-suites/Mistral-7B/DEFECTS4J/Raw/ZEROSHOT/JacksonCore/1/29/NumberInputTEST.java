package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class NumberInputTest {

 @Test
 public void testParseInt_ValidInputs() {
 char[] validIntDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 for (int length = 1; length <= 9; length++) {
 int expected = 0;
 for (int i = 0; i < length; i++) {
 expected = expected * 10 + (validIntDigits[i] - '0');
 }
 int result = NumberInput.parseInt(validIntDigits, 0, length);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testParseInt_InvalidInput() {
 char[] invalidInput = new char[]{'1', 'a', '3'};
 int result = NumberInput.parseInt(invalidInput, 0, invalidInput.length);
 assertEquals(0, result);
 }

 @Test
 public void testParseInt_MinusInput() {
 char[] minusInput = new char[]{'-', '1', '2', '3'};
 int result = NumberInput.parseInt(minusInput, 0, minusInput.length);
 assertEquals(-123, result);
 }

 @Test
 public void testParseInt_LongInput() {
 char[] longInput = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 int result = NumberInput.parseInt(longInput, 0, longInput.length);
 assertEquals(1234567890, result);
 }

 @Test
 public void testParseLong_ValidInputs() {
 char[] validLongDigits1 = new char[]{'1', '2', '3'};
 long expected1 = 123;
 long result1 = NumberInput.parseLong(validLongDigits1, 0, validLongDigits1.length);
 assertEquals(expected1, result1);

 char[] validLongDigits2 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected2 = 123456789000L;
 long result2 = NumberInput.parseLong(validLongDigits2, 0, validLongDigits2.length);
 assertEquals(expected2, result2);
 }

 @Test
 public void testParseLong_InvalidInput() {
 char[] invalidInput = new char[]{'1', 'a', '3'};
 long result = NumberInput.parseLong(invalidInput, 0, invalidInput.length);
 assertEquals(0, result);
 }

 @Test
 public void testParseLong_MinusInput() {
 char[] minusInput = new char[]{'-', '1', '2', '3'};
 long result = NumberInput.parseLong(minusInput, 0, minusInput.length);
 assertEquals(-123, result);
 }

 @Test
 public void testParseLong_MaxLongInput() {
 char[] maxLongInput = new char[]{'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9'};
 long result = NumberInput.parseLong(maxLongInput, 0, maxLongInput.length);
 assertEquals(Long.MAX_VALUE, result);
 }

 @Test
 public void testParseLong_MinusMaxLongInput() {
 char[] minusMaxLongInput = new char[]{'-', '9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9'};
 long result = NumberInput.parseLong(minusMaxLongInput, 0, minusMaxLongInput.length);
 assertEquals(Long.MIN_VALUE, result);
 }

 @Test
 public void testParseLong_LongerThanMaxLongInput() {
 char[] longerThanMaxLongInput = new char[]{'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9'};
 long result = NumberInput.parseLong(longerThanMaxLongInput, 0, longerThanMaxLongInput.length);
 assertEquals(Long.parseLong("9223372036854775807"), result);
 }

 @Test
 public void testIsInLongRange_ValidInputs() {
 String validLongStrings[] = {
 "1", "12", "123", "1234", "12345", "123456", "1234567", "1234567890",
 "-1", "-12", "-123", "-1234", "-12345", "-123456", "-1234567", "-1234567890"
 };
 for (String validLongString : validLongStrings) {
 boolean result = NumberInput.inLongRange(validLongString, false);
 assertTrue(result);
 }
 }

 @Test
 public void testIsInLongRange_InvalidInputs() {
 String invalidInputs[] = {
 "", "a", "1a", "ab", "12a", "12ab", "123a", "0123", "-123a", "123abc"
 };
 for (String invalidInput : invalidInputs) {
 boolean result = NumberInput.inLongRange(invalidInput, false);
 assertFalse(result);
 }
 }

 @Test
 public void testIsInLongRange_NegativeInputs() {
 String negativeInputs[] = {"-1", "-1234567890", "-9223372036854775808"};
 for (String negativeInput : negativeInputs) {
 boolean result = NumberInput.inLongRange(negativeInput, true);
 assertTrue(result);
 }
 }

 @Test
 public void testParseAsInt_ValidInputs() {
 String validInputs[] = {"1", "12", "123", "1234", "12345", "123456", "1234567", "1234567890", "-1", "-1234567890", "123e45", "0x12F"};
 int defaultValue = 42;
 for (String validInput : validInputs) {
 int result = NumberInput.parseAsInt(validInput, defaultValue);
 assertEquals(Integer.parseInt(validInput), result);
 }
 }

 @Test
 public void testParseAsInt_InvalidInputs() {
 String invalidInputs[] = {"", "a", "1a", "ab", "12a", "12ab", "123a", "0123", "-123a", "123abc"};
 int defaultValue = 42;
 for (String invalidInput : invalidInputs) {
 int result = NumberInput.parseAsInt(invalidInput, defaultValue);
 assertEquals(defaultValue, result);
 }
 }

 @Test
 public void testParseAsLong_ValidInputs() {
 String validInputs[] = {"1", "12", "123", "1234", "12345", "123456", "1234567", "1234567890", "-1", "-1234567890", "123e45", "0x12F", "12345678901234567890", "123456789012345678901234567890", "9223372036854775807", "-9223372036854775808"};
 long defaultValue = 42L;
 for (String validInput : validInputs) {
 long result = NumberInput.parseAsLong(validInput, defaultValue);
 assertEquals(Long.parseLong(validInput), result);
 }
 }

 @Test
 public void testParseAsLong_InvalidInputs() {
 String invalidInputs[] = {"", "a", "1a", "ab", "12a", "12ab", "123a", "0123", "-123a", "123abc"};
 long defaultValue = 42L;
 for (String invalidInput : invalidInputs) {
 long result = NumberInput.parseAsLong(invalidInput, defaultValue);
 assertEquals(defaultValue, result);
 }
 }

 @Test
 public void testParseAsDouble_ValidInputs() {
 String validInputs[] = {"1.2", "1.23", "1.234", "1.2345", "1.23456", "1.234567", "1.234567890", "-1.234567890", "1.23e4", "1.23e-4", "0x1p1024"};
 double defaultValue = 42.0;
 for (String validInput : validInputs) {
 double result = NumberInput.parseAsDouble(validInput, defaultValue);
 assertEquals(Double.parseDouble(validInput), result, 0.000001);
 }
 }

 @Test
 public void testParseAsDouble_InvalidInputs() {
 String invalidInputs[] = {"", "a", "1a", "ab", "12a", "12ab", "123a", "0123", "-123a", "123abc", "123e", "123e4", "123e-4"};
 double defaultValue = 42.0;
 for (String invalidInput : invalidInputs) {
 double result = NumberInput.parseAsDouble(invalidInput, defaultValue);
 assertEquals(defaultValue, result, 0.0);
 }
 }

 @Test
 public void testParseBigDecimal_ValidInputs() {
 String validInputs[] = {"1.2", "1.23", "1.234", "1.2345", "1.23456", "1.234567", "1.234567890", "-1.234567890", "1.23e4", "1.23e-4", "0x1p1024"};
 BigDecimal defaultValue = new BigDecimal(42);
 for (String validInput : validInputs) {
 BigDecimal result = NumberInput.parseBigDecimal(validInput);
 assertEquals(new BigDecimal(validInput), result);
 }
 }

 @Test
 public void testParseBigDecimal_InvalidInputs() {
 String invalidInputs[] = {"", "a", "1a", "ab", "12a", "12ab", "123a", "0123", "-123a", "123abc", "123e", "123e4", "123e-4"};
 BigDecimal defaultValue = new BigDecimal(42);
 for (String invalidInput : invalidInputs) {
 BigDecimal result = NumberInput.parseBigDecimal(invalidInput);
 assertEquals(defaultValue, result);
 }
 }

 @Test
 public void testParseBigDecimal_CharArrayInputs() {
 char[] charArrayValidInputs[] = {
 new char[]{'4', '9'}, new char[]{'4', '9', '.'}, new char[]{'4', '9', '.', '1'},
 new char[]{'4', '9', '.', '1', '2'}, new char[]{'4', '9', '.', '1', '2', '3'},
 new char[]{'4', '9', '.', '1', '2', '3', '5'}, new char[]{'4', '9', '.', '1', '2', '3', '5', '6'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7'}, new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2', '3'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'}
 };
 BigDecimal defaultValue = new BigDecimal(42);
 for (char[] charArrayValidInput : charArrayValidInputs) {
 BigDecimal result = NumberInput.parseBigDecimal(charArrayValidInput);
 assertEquals(new BigDecimal(new String(charArrayValidInput)), result);
 }
 }

 @Test
 public void testParseBigDecimal_CharArrayInputs_WithOffsetAndLength() {
 char[] charArrayValidInputs[] = {
 new char[]{'4', '9'}, new char[]{'4', '9', '.'}, new char[]{'4', '9', '.', '1'},
 new char[]{'4', '9', '.', '1', '2'}, new char[]{'4', '9', '.', '1', '2', '3'},
 new char[]{'4', '9', '.', '1', '2', '3', '5'}, new char[]{'4', '9', '.', '1', '2', '3', '5', '6'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7'}, new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2', '3'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4'},
 new char[]{'4', '9', '.', '1', '2', '3', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'}
 };
 BigDecimal defaultValue = new BigDecimal(42);
 for (int i = 0; i < charArrayValidInputs.length; i++) {
 BigDecimal result = NumberInput.parseBigDecimal(charArrayValidInputs[i], 0, charArrayValidInputs[i].length);
 assertEquals(new BigDecimal(new String(charArrayValidInputs[i])), result);
 }
 }
}