package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_1() {
 char[] digits = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digits, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_2() {
 char[] digits = {'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digits, 0, 6);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_3() {
 char[] digits = {'1', '2', '3', '4', '5', 'a'};
 NumberInput.parseInt(digits, 0, 6);
 }

 @Test
 public void testParseInt_4() {
 String number = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(number);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_5() {
 String number = "12345a";
 NumberInput.parseInt(number);
 }

 @Test
 public void testParseLong_1() {
 char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digits, 0, digits.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_2() {
 char[] digits = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(digits, 0, digits.length+1);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_3() {
 char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a'};
 NumberInput.parseLong(digits, 0, digits.length+1);
 }

 @Test
 public void testParseLong_4() {
 String number = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(number);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_5() {
 String number = "1234567890a";
 NumberInput.parseLong(number);
 }

 @Test
 public void testParseLong_6() {
 String number = "12345e6";
 long expected = 12345000000L;
 long actual = NumberInput.parseLong(number);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_7() {
 char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'e'};
 NumberInput.parseLong(digits, 0, digits.length+1);
 }

 @Test
 public void testInLongRange_1() {
 char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean actual = NumberInput.inLongRange(digits, 0, digits.length, false);
 assertTrue(actual);
 }

 @Test
 public void testInLongRange_2() {
 char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '1'};
 boolean actual = NumberInput.inLongRange(digits, 0, digits.length, false);
 assertFalse(actual);
 }

 @Test
 public void testInLongRange_3() {
 String number = "12345678901";
 boolean actual = NumberInput.inLongRange(number, false);
 assertFalse(actual);
 }

 @Test
 public void testInLongRange_4() {
 String number = "1234567890";
 boolean actual = NumberInput.inLongRange(number, false);
 assertTrue(actual);
 }

 @Test
 public void testParseAsInt_1() {
 String number = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt("12345", 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_2() {
 String number = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseAsInt("12345", -12345);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_3() {
 String number = "12345a";
 int expected = 0;
 int actual = NumberInput.parseAsInt("12345a", 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_4() {
 String number = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(number, 12345);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_5() {
 String number = "12345x";
 int expected = 0;
 int actual = NumberInput.parseAsInt(number, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_1() {
 String number = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong("1234567890", 1234567890L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_2() {
 String number = "-1234567890";
 long expected = -1234567890L;
 long actual = NumberInput.parseAsLong("1234567890", -1234567890L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_3() {
 String number = "1234567890a";
 long expected = 0;
 long actual = NumberInput.parseAsLong("1234567890a", 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_4() {
 String number = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong(number, 1234567890L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_5() {
 String number = "12345e6";
 long expected = 12345000000L;
 long actual = NumberInput.parseAsLong("12345e6", 12345000000L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_6() {
 String number = "12345e10";
 long expected = Long.MAX_VALUE;
 long actual = NumberInput.parseAsLong("12345e10", Long.MAX_VALUE);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_7() {
 String number = "12345e-10";
 long expected = 0;
 long actual = NumberInput.parseAsLong("12345e-10", 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_1() {
 String number = "12345";
 BigDecimal expected = new BigDecimal("12345");
 BigDecimal actual = NumberInput.parseBigDecimal("12345");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_2() {
 String number = "-12345";
 BigDecimal expected = new BigDecimal("-12345");
 BigDecimal actual = NumberInput.parseBigDecimal("-12345");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_3() {
 String number = "12345.123";
 BigDecimal expected = new BigDecimal("12345.123");
 BigDecimal actual = NumberInput.parseBigDecimal("12345.123");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_4() {
 String number = "12345E123";
 BigDecimal expected = new BigDecimal("12345000000000000");
 BigDecimal actual = NumberInput.parseBigDecimal("12345E123");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_5() {
 char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
 int offset = 10;
 int len = 1;
 BigDecimal expected = new BigDecimal("1.3");
 BigDecimal actual = NumberInput.parseBigDecimal(new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}, offset, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_6() {
 String number = "12345a";
 NumberInput.parseBigDecimal("12345a");
 }
}