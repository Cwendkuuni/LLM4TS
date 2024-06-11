package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

public class NumberInputTest {

	@Test
	public void testParseIntWithValidInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4', '5' };
		int expected = 12345;
		int result = NumberInput.parseInt(digitChars, 0, 5);
		assertEquals(expected, result);
	}

	@Test
	public void testParseIntWithLongInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		long expected = 123456789;
		int result = NumberInput.parseInt(digitChars, 0, 9);
		assertEquals(expected, (int) expected);
		assertEquals(expected, result);
	}

	@Test
	public void testParseIntWithNegativeInput() {
		char[] digitChars = new char[] { '-', '1', '2', '3', '4', '5' };
		int expected = -12345;
		int result = NumberInput.parseInt(digitChars, 0, 5);
		assertEquals(expected, result);
	}

	@Test
	public void testParseIntWithInvalidInput() {
		char[] digitChars = new char[] { 'a', 'b', 'c' };
		int result = NumberInput.parseInt(digitChars, 0, 2);
		assertEquals(0, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseIntWithTooLongInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a' };
		NumberInput.parseInt(digitChars, 0, 10);
	}

	@Test
	public void testParseLongWithValidInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4' };
		long expected = 1234;
		long result = NumberInput.parseLong(digitChars, 0, 4);
		assertEquals(expected, result);
	}

	@Test
	public void testParseLongWithNegativeInput() {
		char[] digitChars = new char[] { '-', '1', '2', '3', '4' };
		long expected = -1234;
		long result = NumberInput.parseLong(digitChars, 0, 4);
		assertEquals(expected, result);
	}

	@Test
	public void testParseLongWithLongInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		long expected = 1234567890;
		long result = NumberInput.parseLong(digitChars, 0, 10);
		assertEquals(expected, result);
	}

	@Test
	public void testParseLongWithInvalidInput() {
		char[] digitChars = new char[] { 'a', 'b', 'c' };
		long result = NumberInput.parseLong(digitChars, 0, 2);
		assertEquals(0, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLongWithTooLongInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a' };
		NumberInput.parseLong(digitChars, 0, 11);
	}

	@Test
	public void testParseLongWithLargeInput() {
		char[] digitChars = new char[] { '9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '7', '8', '9', '0', '1' };
		long expected = Long.MAX_VALUE;
		long result = NumberInput.parseLong(digitChars, 0, 19);
		assertEquals(expected, result);
	}

	@Test
	public void testParseLongWithMinusLargeInput() {
		char[] digitChars = new char[] { '-', '9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '7', '8', '9', '0', '1' };
		long expected = Long.MIN_VALUE;
		long result = NumberInput.parseLong(digitChars, 0, 19);
		assertEquals(expected, result);
	}

	@Test
	public void testParseLongWithInRangeInput() {
		char[] digitChars = new char[] { '1', '2', '3' };
		assertTrue(NumberInput.inLongRange(digitChars, 0, 2, false));
		assertTrue(NumberInput.inLongRange(digitChars, 0, 2, true));
		assertTrue(NumberInput.inLongRange("123", false));
		assertTrue(NumberInput.inLongRange("123", true));
		assertTrue(NumberInput.inLongRange("999999999999999999", false));
		assertTrue(NumberInput.inLongRange("999999999999999999", true));
	}

	@Test
	public void testParseLongWithOutOfRangeInput() {
		char[] digitChars = new char[] { '1', '2', '3', '4', '5' };
		assertFalse(NumberInput.inLongRange(digitChars, 0, 2, false));
		assertFalse(NumberInput.inLongRange(digitChars, 0, 2, true));
		assertFalse(NumberInput.inLongRange("12345678901234567890", false));
		assertFalse(NumberInput.inLongRange("12345678901234567890", true));
		assertFalse(NumberInput.inLongRange("123456789012345678901234567890", false));
		assertFalse(NumberInput.inLongRange("123456789012345678901234567890", true));
	}

	@Test
	public void testParseAsIntWithValidInput() {
		String input = "12345";
		int expected = 12345;
		int result = NumberInput.parseAsInt(input, 0);
		assertEquals(expected, result);
	}

	@Test
	public void testParseAsIntWithNegativeInput() {
		String input = "-12345";
		int expected = -12345;
		int result = NumberInput.parseAsInt(input, 0);
		assertEquals(expected, result);
	}

	@Test
	public void testParseAsIntWithDefaultValue() {
		String input = "abc";
		int defaultValue = 0;
		int result = NumberInput.parseAsInt(input, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void testParseAsIntWithInvalidInput() {
		String input = "abcd";
		int defaultValue = 0;
		int result = NumberInput.parseAsInt(input, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsIntWithInvalidInputWithNoDefaultValue() {
		String input = "abcd";
		NumberInput.parseAsInt(input, 0);
	}

	@Test
	public void testParseAsLongWithValidInput() {
		String input = "12345678901234567890";
		long expected = 12345678901234567890L;
		long result = NumberInput.parseAsLong(input, 0);
		assertEquals(expected, result);
	}

	@Test
	public void testParseAsLongWithNegativeInput() {
		String input = "-12345678901234567890";
		long expected = -12345678901234567890L;
		long result = NumberInput.parseAsLong(input, 0);
		assertEquals(expected, result);
	}

	@Test
	public void testParseAsLongWithDefaultValue() {
		String input = "abc";
		long defaultValue = 0;
		long result = NumberInput.parseAsLong(input, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void testParseAsLongWithInvalidInput() {
		String input = "abcd";
		long defaultValue = 0;
		long result = NumberInput.parseAsLong(input, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsLongWithInvalidInputWithNoDefaultValue() {
		String input = "abcd";
		NumberInput.parseAsLong(input, 0);
	}

	@Test
	public void testParseAsDoubleWithValidInput() {
		String input = "123.45";
		double expected = 123.45;
		double result = NumberInput.parseAsDouble(input, 0);
		assertEquals(expected, result, 0.001);
	}

	@Test
	public void testParseAsDoubleWithNegativeInput() {
		String input = "-123.45";
		double expected = -123.45;
		double result = NumberInput.parseAsDouble(input, 0);
		assertEquals(expected, result, 0.001);
	}

	@Test
	public void testParseAsDoubleWithDefaultValue() {
		String input = "abc";
		double defaultValue = 0;
		double result = NumberInput.parseAsDouble(input, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test
	public void testParseAsDoubleWithInvalidInput() {
		String input = "abcd";
		double defaultValue = 0;
		double result = NumberInput.parseAsDouble(input, defaultValue);
		assertEquals(defaultValue, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsDoubleWithInvalidInputWithNoDefaultValue() {
		String input = "abcd";
		NumberInput.parseAsDouble(input, 0);
	}

	@Test
	public void testParseBigDecimalWithValidInput() {
		String input = "123.45";
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal result = NumberInput.parseBigDecimal(input);
		assertEquals(expected, result);
	}

	@Test
	public void testParseBigDecimalWithNegativeInput() {
		String input = "-123.45";
		BigDecimal expected = new BigDecimal("-123.45");
		BigDecimal result = NumberInput.parseBigDecimal(input);
		assertEquals(expected, result);
	}

	@Test
	public void testParseBigDecimalWithInvalidInput() {
		String input = "abcd";
		assertThrows(NumberFormatException.class, () -> {
			NumberInput.parseBigDecimal(input);
		});
	}

	@Test
	public void testParseBigDecimalWithCharArrayInput() {
		char[] buffer = new char[] { '1', '2', '3', '.', '4', '5' };
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 6);
		assertEquals(expected, result);
	}

	@Test
	public void testParseBigDecimalWithCharArrayInputWithNegativeSign() {
		char[] buffer = new char[] { '-', '1', '2', '3', '.', '4', '5' };
		BigDecimal expected = new BigDecimal("-123.45");
		BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 6);
		assertEquals(expected, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseBigDecimalWithCharArrayInputWithInvalidLength() {
		char[] buffer = new char[] { '1', '2', '3', '.' };
		NumberInput.parseBigDecimal(buffer, 0, 4);
	}

	@Test
	public void testParseBigDecimalWithBigIntegerInput() {
		String input = "12345678901234567890";
		BigInteger bigInteger = new BigInteger(input);
		BigDecimal expected = new BigDecimal(bigInteger);
		BigDecimal result = NumberInput.parseBigDecimal(input);
		assertEquals(expected, result);
	}

	@Test
	public void testParseBigDecimalWithLongInput() {
		long number = 12345678901234567890L;
		BigDecimal expected = new BigDecimal(number);
		BigDecimal result = NumberInput.parseBigDecimal(String.valueOf(number));
		assertEquals(expected, result);
	}

	@Test
	public void testParseBigDecimalWithDoubleInput() {
		double number = 123.45;
		BigDecimal expected = new BigDecimal(number);
		BigDecimal result = NumberInput.parseBigDecimal(String.valueOf(number));
		assertEquals(expected, result);
	}

	@Test
	public void testParseBigDecimalWithInvalidInput() {
		String input = "abcd";
		assertThrows(NumberFormatException.class, () -> {
			NumberInput.parseBigDecimal(input);
		});
	}
}