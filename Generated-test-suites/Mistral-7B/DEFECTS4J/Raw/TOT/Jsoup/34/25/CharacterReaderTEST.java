package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderWithOneCharacter() {
 CharacterReader reader = new CharacterReader("A");
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('A', reader.current());
 }

 @Test
 public void testReadMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello");
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('H', reader.current());
 Assert.assertEquals('e', reader.consume());
 Assert.assertEquals('l', reader.consume());
 Assert.assertEquals('l', reader.consume());
 Assert.assertEquals('o', reader.consume());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("abcabcabca");
 Assert.assertEquals(3, reader.nextIndexOf('c'));
 Assert.assertEquals(-1, reader.nextIndexOf('x'));
 }

 @Test
 public void testNextIndexOfString() {
 CharacterReader reader = new CharacterReader("abcabcabca");
 Assert.assertEquals(3, reader.nextIndexOf("bc"));
 Assert.assertEquals(-1, reader.nextIndexOf("xy"));
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader("abcabcabca");
 String consumed = reader.consumeTo('c');
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("abcabcabca");
 String consumed = reader.consumeTo("bc");
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("0123456789abc");
 String consumed = reader.consumeToAny('3', '5');
 Assert.assertEquals("12", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcabcabca");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abc", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abc123", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEF0123456789ABCDEF");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("0f", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("123456789012345678901234567890");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEF0123456789ABCDEF");
 Assert.assertTrue(reader.matches('e'));
 Assert.assertTrue(reader.matches("ef"));
 Assert.assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEF0123456789ABCDEF");
 Assert.assertTrue(reader.matchesIgnoreCase("ef"));
 Assert.assertTrue(reader.matchesIgnoreCase("Ef"));
 Assert.assertFalse(reader.matchesIgnoreCase("x"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEF");
 Assert.assertTrue(reader.matchesAny('1', '5'));
 Assert.assertFalse(reader.matchesAny('x'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEF0123456789ABCDEF");
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEF");
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abc");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("HelloWorld", consumed);
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("HelloWorld", consumed);
 Assert.assertEquals("World", reader.toString());
 }
}