package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertTrue(reader.isEmpty());
 assertEquals(-1, reader.nextIndexOf('a'));
 }

 @Test
 public void testCharacterReaderWithData() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
 assertFalse(reader.isEmpty());
 assertEquals(0, reader.nextIndexOf('a'));
 assertEquals("a", reader.consumeTo('a'));
 assertEquals("bcd", reader.consumeTo("bc"));
 assertEquals("def", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderWithDelimiters() {
 CharacterReader reader = new CharacterReader(new StringReader("abc<def>"));
 assertEquals(5, reader.nextIndexOf('<'));
 assertEquals("abc", reader.consumeTo(' '));
 assertEquals("def", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderWithError() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 try {
 reader.consume();
 fail("Should throw UncheckedIOException");
 } catch (UncheckedIOException e) {
 // expected
 }
 }

 @Test
 public void testCharacterReaderAdvance() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.advance();
 assertEquals('b', reader.current());
 reader.advance();
 assertEquals('c', reader.current());
 reader.advance();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("abcabcabc"));
 assertEquals(4, reader.nextIndexOf('b', 1));
 assertEquals(3, reader.nextIndexOf('a', 4));
 }

 @Test
 public void testCharacterReaderNextIndexOfWithString() {
 CharacterReader reader = new CharacterReader(new StringReader("abcabcabc"));
 assertEquals(3, reader.nextIndexOf("ba"));
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("helloWorld"));
 assertEquals("hello", reader.consumeLetterSequence());
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("hello12345"));
 assertEquals("hello12345", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("0x1a2b3c"));
 assertEquals("1a2b3c", reader.consumeHexSequence());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("123456789"));
 assertEquals("123456789", reader.consumeDigitSequence());
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("helloWorld"));
 assertTrue(reader.matches('l'));
 assertTrue(reader.matches("Or"));
 assertFalse(reader.matches('!'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("helloWorld"));
 assertTrue(reader.matchesIgnoreCase("or"));
 assertFalse(reader.matchesIgnoreCase("!"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("helloWorld"));
 assertTrue(reader.matchesAny('l', 'r', 'o'));
 assertFalse(reader.matchesAny('!'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("helloWorld"));
 assertTrue(reader.matchesAnySorted(new char[]{'l', 'r', 'o'}));
 assertFalse(reader.matchesAnySorted(new char[]{'!', 'l'}));
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><body><p></p></body></html>"));
 assertEquals("<html><body><p>", reader.consumeTagName());
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&amp;<>&lt;>&gt;"));
 assertEquals("&amp;<>&lt;&gt;", reader.consumeData());
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><body><p></p></body></html>"));
 assertEquals("<html><body><p></p></body></html>", reader.consumeToEnd());
 }
}