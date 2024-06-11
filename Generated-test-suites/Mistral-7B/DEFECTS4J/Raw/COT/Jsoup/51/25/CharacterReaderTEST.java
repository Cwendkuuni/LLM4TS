package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test cases for CharacterReader class
 */
public class CharacterReaderTest {

 private static final String TEST_INPUT = "This is a test string with special characters: <>&\"'/\\\\";

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 Validate.notNull(reader);
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 int pos = reader.pos();
 assertEquals(0, pos);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 boolean empty = reader.isEmpty();
 assertFalse(empty);
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char current = reader.current();
 assertEquals('T', current);
 reader.consume();
 current = reader.current();
 assertEquals('h', current);
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 reader.consume();
 reader.consume();
 char current = reader.current();
 assertEquals('i', current);
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 reader.consume();
 reader.consume();
 reader.unconsume();
 char current = reader.current();
 assertEquals('i', current);
 reader.advance();
 reader.unconsume();
 current = reader.current();
 assertEquals('i', current);
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 reader.advance();
 char current = reader.current();
 assertEquals('i', current);
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 char current = reader.current();
 assertEquals('i', current);
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String consumed = reader.consumeAsString();
 assertEquals("i", consumed);
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 int offset = reader.nextIndexOf('i');
 assertEquals(2, offset);
 }

 @Test
 public void testNextIndexOfChar_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 int offset = reader.nextIndexOf('k');
 assertEquals(-1, offset);
 }

 @Test
 public void testNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 CharSequence seq = "is";
 int offset = reader.nextIndexOf(seq);
 assertEquals(1, offset);
 }

 @Test
 public void testNextIndexOfCharSequence_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 CharSequence seq = "kis";
 int offset = reader.nextIndexOf(seq);
 assertEquals(-1, offset);
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char target = 'i';
 String consumed = reader.consumeTo(target);
 assertEquals("is", consumed);
 }

 @Test
 public void testConsumeToChar_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char target = 'k';
 String consumed = reader.consumeTo(target);
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String target = "test";
 String consumed = reader.consumeTo(target);
 assertEquals("test string", consumed);
 }

 @Test
 public void testConsumeToString_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String target = "kis";
 String consumed = reader.consumeTo(target);
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeToAnyCharArray() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char[] chars = new char[]{'i', 's'};
 String consumed = reader.consumeToAny(chars);
 assertEquals("is", consumed);
 }

 @Test
 public void testConsumeToAnyCharArray_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char[] chars = new char[]{'k', 's'};
 String consumed = reader.consumeToAny(chars);
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeToAnySortedCharArray() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char[] chars = new char[]{'s', 'i'};
 String consumed = reader.consumeToAnySorted(chars);
 assertEquals("is", consumed);
 }

 @Test
 public void testConsumeToAnySortedCharArray_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char[] chars = new char[]{'k', 's'};
 String consumed = reader.consumeToAnySorted(chars);
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String consumed = reader.consumeData();
 assertEquals("This is a test string with special characters: <>&\"'/\\\\", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String consumed = reader.consumeTagName();
 assertEquals("<", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String consumed = reader.consumeToEnd();
 assertEquals("<>&\"'/\\\\", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("is this a test");
 String consumed = reader.consumeLetterSequence();
 assertEquals("is", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("is this a test 123");
 String consumed = reader.consumeLetterThenDigitSequence();
 assertEquals("is this a test 123", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0xF00D");
 String consumed = reader.consumeHexSequence();
 assertEquals("0xF00D", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("123456");
 String consumed = reader.consumeDigitSequence();
 assertEquals("123456", consumed);
 }

 @Test
 public void testMatchesChar() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 boolean matches = reader.matches('i');
 assertTrue(matches);
 reader.consume();
 matches = reader.matches('i');
 assertFalse(matches);
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String seq = "is";
 boolean matches = reader.matches(seq);
 assertTrue(matches);
 reader.consume(2);
 matches = reader.matches(seq);
 assertFalse(matches);
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String seq = "IS";
 boolean matches = reader.matchesIgnoreCase(seq);
 assertTrue(matches);
 reader.consume(1);
 matches = reader.matchesIgnoreCase(seq);
 assertFalse(matches);
 }

 @Test
 public void testMatchesAnyChar() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char[] seq = new char[]{'i', 's'};
 boolean matches = reader.matchesAny(seq);
 assertTrue(matches);
 reader.consume(2);
 matches = reader.matchesAny(seq);
 assertFalse(matches);
 }

 @Test
 public void testMatchesAnySortedCharArray() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 char[] seq = new char[]{'s', 'i'};
 boolean matches = reader.matchesAnySorted(seq);
 assertTrue(matches);
 reader.consume('s');
 reader.consume('i');
 matches = reader.matchesAnySorted(seq);
 assertFalse(matches);
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 boolean matches = reader.matchesLetter();
 assertTrue(matches);
 reader.consume();
 matches = reader.matchesLetter();
 assertFalse(matches);
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 boolean matches = reader.matchesDigit();
 assertFalse(matches);
 reader.consume();
 matches = reader.matchesDigit();
 assertFalse(matches);
 reader.consume('0');
 matches = reader.matchesDigit();
 assertTrue(matches);
 }

 @Test(expected = AssertionError.class)
 public void testMatchConsumeString_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String seq = "kis";
 reader.matchConsume(seq);
 }

 @Test(expected = AssertionError.class)
 public void testMatchConsumeIgnoreCaseString_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String seq = "KIS";
 reader.matchConsumeIgnoreCase(seq);
 }

 @Test(expected = AssertionError.class)
 public void testContainsIgnoreCaseString_NotPresent() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String seq = "kis";
 reader.containsIgnoreCase(seq);
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader(TEST_INPUT);
 String toString = reader.toString();
 assertEquals(TEST_INPUT, toString);
 }
}