package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberInputTest {

	@Test
	public void testParseInt_validSmallPositiveInteger() {
		char[] digitChars = {'1', '2', '3'};
		int expected = 123;
		int actual = NumberInput.parseInt(digitChars, 0, 3);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseInt_validSmallNegativeInteger() {
		char[] digitChars = {'-', '1', '2', '3'};
		int expected = -123;
		int actual = NumberInput.parseInt(digitChars, 0, 4);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseInt_validLargePositiveInteger() {
		char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		long expected = 123_456_789;
		int actual = NumberInput.parseInt(digitChars, 0, 9);
		assertEquals((int)expected, actual);
	}

	@Test
	public void testParseInt_largePositiveInteger_longReturned() {
		char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
		long expected = 123_456_789_001;
		long actual = NumberInput.parseInt(digitChars, 0, 11);
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseInt_invalidInput() {
		char[] digitChars = {'1', '2', '3', 'x'};
		NumberInput.parseInt(digitChars, 0, 3);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseInt_negativeLength() {
		char[] digitChars = {'1'};
		NumberInput.parseInt(digitChars, 0, -1);
	}

	@Test
	public void testParseLong_validSmallPositiveLong() {
		char[] digitChars = {'1', '2', '3'};
		long expected = 123;
		long actual = NumberInput.parseLong(digitChars, 0, 3);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseLong_validSmallNegativeLong() {
		char[] digitChars = {'-', '1', '2', '3'};
		long expected = -123;
		long actual = NumberInput.parseLong(digitChars, 0, 4);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseLong_validLargePositiveLong() {
		char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		BigInteger expected = BigInteger.valueOf(123_456_789_000L);
		BigInteger actual = NumberInput.parseBigDecimal(digitChars).toBigInteger();
		assertEquals(expected, actual);
	}

	@Test
	public void testParseLong_largePositiveLong_longReturned() {
		char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
		BigInteger expected = BigInteger.valueOf(123_456_789_001_234L);
		BigInteger actual = NumberInput.parseBigDecimal(digitChars).toBigInteger();
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLong_invalidInput() {
		char[] digitChars = {'1', '2', '3', 'x'};
		NumberInput.parseLong(digitChars, 0, 3);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLong_negativeLength() {
		char[] digitChars = {'1'};
		NumberInput.parseLong(digitChars, 0, -1);
	}

	@Test
	public void testParseLong_longOutOfRange() {
		char[] digitChars = {'1', '2', '3'};
		assertFalse(NumberInput.inLongRange(digitChars, 0, 3, false));
		assertFalse(NumberInput.inLongRange(digitChars, 0, 3, true));
	}

	@Test		public void testParseLong_longInRange() {
		char[] digitChars = {'1', '2', '3', '4', '5'};
		assertTrue(NumberInput.inLongRange(digitChars, 0, 5, false));
		assertTrue(NumberInput.inLongRange(digitChars, 0, 5, true));
	}

	@Test
	public void testParseLong_StringInput_validSmallPositiveLong() {
		String input = "123";
		long expected = 123;
		long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
		assertEquals(expected, actual);
	}

	@Test
	public void testParseLong_StringInput_validSmallNegativeLong() {
		String input = "-123";
		long expected = -123;
		long actual = NumberInput.parseLong(input.toCharArray(), 0, input.length());
		assertEquals(expected, actual);
	}

	@Test
	public void testParseLong_StringInput_largePositiveLong() {
		String input = "123_456_789_000";
		BigInteger expected = BigInteger.valueOf(123_456_789_000L);
		BigInteger actual = NumberInput.parseBigDecimal(input.toCharArray()).toBigInteger();
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLong_StringInput_invalidInput() {
		String input = "123x";
		NumberInput.parseLong(input.toCharArray(), 0, input.length());
	}

	@Test(expected = NumberFormatException.class)
	public void testParseLong_StringInput_negativeLength() {
		String input = "123";
		NumberInput.parseLong(input.toCharArray(), 0, -1);
	}

	@Test
	public void testParseLong_StringInput_longOutOfRange() {
		String input = "123456789012345678901234567890123456789012345678901234567890123";
		assertFalse(NumberInput.inLongRange(input.toCharArray(), 0, input.length(), false));
		assertFalse(NumberInput.inLongRange(input.toCharArray(), 0, input.length(), true));
	}

	@Test		public void testParseLong_StringInput_longInRange() {
		String input = "1234567890123";
		assertTrue(NumberInput.inLongRange(input.toCharArray(), 0, input.length(), false));
		assertTrue(NumberInput.inLongRange(input.toCharArray(), 0, input.length(), true));
	}

	@Test
	public void testParseAsInt_validInput() {
		String input = "123";
		int expected = 123;
		int actual = NumberInput.parseAsInt(input, 0);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseAsInt_defaultValue() {
		String input = "x";
		int expected = 0;
		int actual = NumberInput.parseAsInt(input, 0);
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsInt_invalidInput() {
		String input = "123x";
		NumberInput.parseAsInt(input.toCharArray(), 0);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsInt_negativeLength() {
		String input = "123";
		NumberInput.parseAsInt(input.toCharArray(), -1);
	}

	@Test
	public void testParseAsLong_validInput() {
		String input = "123_456_789_000";
		BigInteger expected = BigInteger.valueOf(123_456_789_000L);
		BigInteger actual = NumberInput.parseBigDecimal(input.toCharArray()).toBigInteger();
		assertEquals(expected, actual);
	}

	@Test
	public void testParseAsLong_defaultValue() {
		String input = "x";
		long expected = 0L;
		long actual = NumberInput.parseAsLong("x", 0L);
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsLong_invalidInput() {
		String input = "123x";
		NumberInput.parseAsLong(input.toCharArray(), 0L);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsLong_negativeLength() {
		String input = "123";
		NumberInput.parseAsLong(input.toCharArray(), -1L);
	}

	@Test
	public void testParseAsDouble_validInput() {
		String input = "123.45";
		double expected = 123.45;
		double actual = NumberInput.parseDouble(input);
		assertEquals(expected, actual, 0.00001);
	}

	@Test
	public void testParseAsDouble_defaultValue() {
		String input = "x";
		double expected = 0.0;
		double actual = NumberInput.parseAsDouble("x", 0.0);
		assertEquals(expected, actual, 0.00001);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsDouble_invalidInput() {
		String input = "123x";
		NumberInput.parseAsDouble(input.toCharArray(), 0.0);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseAsDouble_negativeLength() {
		String input = "123.45";
		NumberInput.parseAsDouble(input.toCharArray(), -1.0);
	}

	@Test
	public void testParseBigDecimal_validInput() {
		String input = "123.45";
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal actual = NumberInput.parseBigDecimal(input);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseBigDecimal_defaultValue() {
		String input = "x";
		BigDecimal expected = BigDecimal.ZERO;
		BigDecimal actual = NumberInput.parseBigDecimal("x");
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseBigDecimal_invalidInput() {
		String input = "123x";
		NumberInput.parseBigDecimal(input.toCharArray());
	}

	@Test
	public void testParseBigDecimal_charArray() {
		char[] input = {'1', '2', '3', '.', '4', '5'};
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal actual = NumberInput.parseBigDecimal(input);
		assertEquals(expected, actual);
	}

	@Test
	public void testParseBigDecimal_charArray_offset() {
		char[] input = {'1', '2', '3', '.', '4', '5'};
		BigDecimal expected = new BigDecimal("123.45");
		BigDecimal actual = NumberInput.parseBigDecimal(input, 0, 6);
		assertEquals(expected, actual);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseBigDecimal_charArray_outOfRange() {
		char[] input = {'1', '2', '3', '.', '4', '5'};
		NumberInput.parseBigDecimal(input, 7, 1);
	}
}