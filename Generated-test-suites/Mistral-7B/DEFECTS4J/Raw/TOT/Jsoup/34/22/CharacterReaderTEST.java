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
 public void testReadSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals('a', reader.consume());
 }

 @Test
 public void testReadMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.consume());
 Assert.assertEquals('c', reader.consume());
 Assert.assertEquals('d', reader.consume());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume(); // mark is now pos = 0, input[pos] = 'a'
 reader.rewindToMark(); // pos is now 0
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd123efghIJK45Lmno");
 String sequence = reader.consumeLetterSequence();
 Assert.assertEquals("abcd", sequence);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123efghIJK45Lmno");
 String sequence = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abcd123", sequence);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcd%#e1f0g%#h1i1j1k");
 String sequence = reader.consumeHexSequence();
 Assert.assertEquals("%#e1f0", sequence);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123efghIJK45Lmno");
 String sequence = reader.consumeDigitSequence();
 Assert.assertEquals("12345", sequence);
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcd<title>Hello World</title>efgh");
 String consumed = reader.consumeTo("<title>");
 Assert.assertEquals("abcd<title>", consumed);
 }

 @Test
 public void testConsumeStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd<TITLE>Hello World</title>efgh");
 String consumed = reader.consumeToIgnoreCase("<title>");
 Assert.assertEquals("abcd<title>", consumed);
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("abcd<title>Hello World</title>efgh");
 int index = reader.nextIndexOf('t');
 Assert.assertEquals(5, index);
 }

 @Test
 public void testNextIndexOfString() {
 CharacterReader reader = new CharacterReader("abcd<title>Hello World</title>efgh");
 int index = reader.nextIndexOf("title");
 Assert.assertEquals(5, index);
 }

 @Test
 public void testNextIndexOfStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd<TITLE>Hello World</title>efgh");
 int index = reader.nextIndexOf("title");
 Assert.assertEquals(5, index);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcd<title>Hello World</title>efgh");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("efgh", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEndOfInput() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // IndexOutOfBoundsException here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 CharacterReader reader = new CharacterReader(null); // IllegalArgumentException here
 }
}