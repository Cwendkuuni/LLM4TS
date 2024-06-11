package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

	@Test
	public void testParseInt_charArray() {
		char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
		int expected = 12345;
		int actual = NumberInput.parseInt(digitChars, 0, 5);
		assertEquals("Failed to parse correct int from char array", expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseInt_charArray_invalid() {
		char[] digitChars = new char[] {'1', '2', '3', 'a', '5'};
		NumberInput.parseInt(digitChars, 0, 5);
	}

	@Test
	public void testParseInt_String() {
		String str = "12345";
		int expected = 12345;
		int actual = NumberInput.parseInt(str);
		assertEquals("Failed to parse correct int from String", expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseInt_String_invalid() {
		String str = "123ab5";
		NumberInput.parseInt(str);
	}

	@Test
	public void testParseLong_charArray() {
		char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		long expected = 123456789L;
		long actual = NumberInput.parseLong(digitChars, 0, 10);
		assertEquals("Failed to parse correct long from char array", expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLong_charArray_invalid() {
		char[] digitChars = new char[] {'1', '2', '3', '4', '5', 'a', '6', '7', '8', '9'};
		NumberInput.parseLong(digitChars, 0, 10);
	}

	@Test
	public void testParseLong_String() {
		String str = "1234567890123";
		long expected = 1234567890123L;
		long actual = NumberInput.parseLong(str);
		assertEquals("Failed to parse correct long from String", expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLong_String_invalid() {
		String str = "123abcd123";
		NumberInput.parseLong(str);
	}

	@Test
	public void testInLongRange_charArray() {
		char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		boolean expected = true;
		boolean actual = NumberInput.inLongRange(digitChars, 0, 10, false);
		assertEquals("Failed to determine if char array is within long range", expected, actual);
	}

	@Test
	public void testInLongRange_String() {
		String str = "1234567890123";
		boolean expected = true;
		boolean actual = NumberInput.inLongRange(str, false);
		assertEquals("Failed to determine if String is within long range", expected, actual);
	}

	@Test
	public void testParseAsInt_String() {
		String input = "123";
		int expected = 123;
		int actual = NumberInput.parseAsInt(input, 0);
		assertEquals("Failed to parse correct int from String", expected, actual);
	}

	@Test
	public void testParseAsInt_String_defaultValue() {
		String input = "";
		int expected = 0;
		int actual = NumberInput.parseAsInt(input, expected);
		assertEquals("Failed to parse correct default value from String", expected, actual);
	}

	@Test
	public void testParseAsLong_String() {
		String input = "1234567890123";
		long expected = 1234567890123L;
		long actual = NumberInput.parseAsLong(input, 0L);
		assertEquals("Failed to parse correct long from String", expected, actual);
	}

	@Test
	public void testParseAsLong_String_defaultValue() {
		String input = "";
		long expected = 0L;
		long actual = NumberInput.parseAsLong(input, expected);
		assertEquals("Failed to parse correct default value from String", expected, actual);
	}

	@Test
	public void testParseAsDouble_String() {
		String input = "123.45";
		double expected = 123.45;
		double actual = NumberInput.parseAsDouble(input, 0.0);
		assertEquals("Failed to parse correct double from String", expected, actual, 0.001);
	}

	@Test
	public void testParseAsDouble_String_defaultValue() {
		String input = "";
		double expected = 0.0;
		double actual = NumberInput.parseAsDouble(input, expected);
		assertEquals("Failed to parse correct default value from String", expected, actual, 0.001);
	}

	@Test
	public void testParseBigDecimal_String() {
		String input = "123.45";
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal actual = NumberInput.parseBigDecimal(input);
		assertEquals("Failed to parse correct BigDecimal from String", expected, actual);
	}

	@Test
	public void testParseBigDecimal_charArray() {
		char[] buffer = new char[] {'1', '2', '3', '.', '4', '5'};
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 6);
		assertEquals("Failed to parse correct BigDecimal from char array", expected, actual);
	}

	@Test
	public void testParseBigDecimal_charArray_offsetAndLength() {
		char[] buffer = new char[] {'1', '2', '3', '.', '4', '5'};
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal actual = NumberInput.parseBigDecimal(buffer, 1, 6);
		assertEquals("Failed to parse correct BigDecimal from char array with offset and length", expected, actual);
	}
}