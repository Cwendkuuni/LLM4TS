package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = "abcd123eF45G".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstruct() {
 CharacterReader reader = new CharacterReader("");
 assertNull(reader.input);
 CharacterReader reader2 = new CharacterReader("abcd");
 assertNotNull(reader2.input);
 assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, reader2.input);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 assertEquals('b', reader.current());
 reader.consume();
 assertEquals('c', reader.current());
 reader.consume();
 assertEquals('d', reader.current());
 reader.consume();
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("", reader.consumeAsString());
 reader.consume();
 assertEquals("a", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(-1, reader.nextIndexOf('z'));
 reader.consumeLetterSequence();
 assertNotEquals(-1, reader.nextIndexOf('d'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 assertEquals(-1, reader.nextIndexOf("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertEquals(4, reader.nextIndexOf("d1"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfWithEndOfInput() {
 reader.consumeToEnd();
 assertEquals(-1, reader.nextIndexOf('d'));
 }

 @Test
 public void testConsumeTo() {
 assertEquals("", reader.consumeTo('d'));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertEquals("d1", reader.consumeTo('1'));
 }

 @Test
 public void testConsumeToWithInvalidCharSequence() {
 assertEquals("", reader.consumeTo("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertEquals("d1", reader.consumeTo("d1"));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("", reader.consumeToAny('z'));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertEquals("1", reader.consumeToAny('1'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("", reader.consumeToEnd());
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeDigitSequence();
 assertEquals("d123", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("", reader.consumeLetterSequence());
 assertEquals("abcd", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("", reader.consumeLetterThenDigitSequence());
 assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("", reader.consumeHexSequence());
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeHexSequence();
 assertEquals("f4", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("", reader.consumeDigitSequence());
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeDigitSequence();
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertFalse(reader.matches('z'));
 reader.consumeLetterSequence();
 assertTrue(reader.matches('d'));
 }

 @Test
 public void testMatchesWithString() {
 assertFalse(reader.matches("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.matches("d1"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertFalse(reader.matchesIgnoreCase("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.matchesIgnoreCase("D1"));
 }

 @Test
 public void testMatchesAny() {
 assertFalse(reader.matchesAny('z'));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.matchesAny('d'));
 }

 @Test
 public void testMatchesLetter() {
 assertFalse(reader.matchesLetter());
 reader.consumeLetterSequence();
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertFalse(reader.matchConsume("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.matchConsume("d1"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertFalse(reader.matchConsumeIgnoreCase("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.matchConsumeIgnoreCase("d1"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertFalse(reader.containsIgnoreCase("xyz"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 assertTrue(reader.containsIgnoreCase("D1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsIgnoreCaseWithEmptyInput() {
 CharacterReader emptyReader = new CharacterReader("");
 assertFalse(emptyReader.containsIgnoreCase("xyz"));
 }
}