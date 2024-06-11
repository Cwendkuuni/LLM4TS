package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

	@Test
	public void testParseInt_validInputs() {
		char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
		int offset = 0, len = 5;
		int expectedResult = 12345;
		int num = NumberInput.parseInt(digitChars, offset, len);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseInt_edgeCases() {
		char[] digitChars = new char[]{'9', '9', '9', '9', '9'};
		int offset = 0, len = 5;
		long maxIntValue = Long.MAX_VALUE;
		int num = NumberInput.parseInt(digitChars, offset, len);
		assertEquals(maxIntValue, num);

		digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
		len = 6;
		int num2 = NumberInput.parseInt(digitChars, offset, len);
		assertEquals(52345, num2);
	}

	@Test
	public void testParseInt_invalidInputs() {
		char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
		int offset = 0, len = 5;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitChars, offset, len));

		digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7'};
		len = 7;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digitChars, offset, len));
	}

	@Test
	public void testParseLong_validInputs() {
		char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		int offset = 0, len = 10;
		long expectedResult = 1234567890L;
		long num = NumberInput.parseLong(digitChars, offset, len);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseLong_edgeCases() {
		char[] digitChars = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
		int offset = 0, len = 11;
		long maxLongValue = Long.MAX_VALUE;
		long num = NumberInput.parseLong(digitChars, offset, len);
		assertEquals(maxLongValue, num);

		digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
		len = 11;
		long num2 = NumberInput.parseLong(digitChars, offset, len);
		assertEquals(123456789101L, num2);
	}

	@Test
	public void testParseLong_invalidInputs() {
		char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a'};
		int offset = 0, len = 10;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digitChars, offset, len));

		digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
		len = 12;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digitChars, offset, len));
	}

	@Test
	public void testParseLong_inRange() {
		char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		boolean negative = false;
		assertTrue(NumberInput.inLongRange(digitChars, 0, 9, negative));

		String numberStr = "123456789";
		assertTrue(NumberInput.inLongRange(numberStr, negative));
	}

	@Test
	public void testParseLong_notInRange() {
		char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		boolean negative = false;
		assertFalse(NumberInput.inLongRange(digitChars, 0, 9, negative));

		String numberStr = "12345678900";
		assertFalse(NumberInput.inLongRange(numberStr, negative));
	}

	@Test
	public void testParseLong_negative_inRange() {
		char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		boolean negative = true;
		assertTrue(NumberInput.inLongRange(digitChars, 1, 9, negative));

		String numberStr = "-123456789";
		assertTrue(NumberInput.inLongRange(numberStr, negative));
	}

	@Test
	public void testParseLong_negative_notInRange() {		
		char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		boolean negative = true;
		assertFalse(NumberInput.inLongRange(digitChars, 1, 9, negative));

		String numberStr = "-12345678900";
		assertFalse(NumberInput.inLongRange(numberStr, negative));
	}

	@Test
	public void testParseAsInt_validInputs() {
		String input = "12345";
		int defaultValue = 0;
		int expectedResult = 12345;
		int num = NumberInput.parseAsInt(input, defaultValue);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseAsInt_edgeCases() {
		String input = "2147483647";
		int defaultValue = 0;
		int expectedResult = Integer.MAX_VALUE;
		int num = NumberInput.parseAsInt(input, defaultValue);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseAsInt_invalidInputs() {
		String input = "abcd";
		int defaultValue = 0;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseAsInt(input, defaultValue));
	}

	@Test
	public void testParseAsLong_validInputs() {
		String input = "12345678901234567890";
		long defaultValue = 0L;
		long expectedResult = 12345678901234567890L;
		long num = NumberInput.parseAsLong(input, defaultValue);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseAsLong_edgeCases() {
		String input = "9223372036854775807";
		long defaultValue = 0L;
		long expectedResult = Long.MAX_VALUE;
		long num = NumberInput.parseAsLong(input, defaultValue);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseAsLong_invalidInputs() {
		String input = "abcde";
		long defaultValue = 0L;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseAsLong(input, defaultValue));
	}

	@Test
	public void testParseAsDouble_validInputs() {
		String input = "3.14159265";
		double defaultValue = 0.0;
		double expectedResult = 3.14159265;
		double num = NumberInput.parseAsDouble(input, defaultValue);
		assertEquals(expectedResult, num, 0.0001);
	}

	@Test
	public void testParseAsDouble_edgeCases() {
		String input = "1.79769313486231E308";
		double defaultValue = 0.0;
		double expectedResult = NumberInput.parseDouble(input);
		double num = NumberInput.parseAsDouble(input, defaultValue);
		assertEquals(expectedResult, num, 0.0);
	}

	@Test
	public void testParseAsDouble_invalidInputs() {
		String input = "abcd";
		double defaultValue = 0.0;
		assertThrows(NumberFormatException.class, () -> NumberInput.parseAsDouble(input, defaultValue));
	}

	@Test
	public void testParseBigDecimal_validInputs() {
		String input = "12345678901234567890.123456";
		BigDecimal expectedResult = new BigDecimal("12345678901234567890.123456");
		BigDecimal num = NumberInput.parseBigDecimal(input);
		assertEquals(expectedResult, num);
	}

	@Test
	public void testParseBigDecimal_edgeCases() {
		char[] buffer = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
		int offset = 0, len = 23;
		String expectedResultStr = "-12345678912345678912345678901234567890123456789012345678901";
		BigDecimal expectedResult = new BigDecimal(expectedResultStr);
		BigDecimal num = NumberInput.parseBigDecimal(buffer, offset, len);
		assertEquals(expectedResult, num);
	}
}