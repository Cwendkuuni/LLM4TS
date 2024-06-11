package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertTrue(reader.isEmpty());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testSingleCharacterReader() {
 CharacterReader reader = new CharacterReader(new StringReader("a"));
 assertFalse(reader.isEmpty());
 assertEquals(1, reader.pos());
 assertEquals('a', reader.current());
 }

 @Test
 public void testMultipleCharactersReader() {
 CharacterReader reader = new CharacterReader(new StringReader("abcd"));
 assertFalse(reader.isEmpty());
 assertEquals(4, reader.pos());
 assertEquals('d', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() {
 CharacterReader reader = new CharacterReader(new IOException("Test IO exception"));
 assertFalse(reader.isEmpty()); // should not be empty as constructor should have thrown an exception
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklm"));
 reader.mark();
 reader.consume(3);
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklm"));
 assertEquals("abc", reader.consumeTo('c'));
 assertEquals("defgijklm", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
 assertEquals("abcd", reader.consumeToAny(new char[]{'a', 'b', 'c', 'd'}));
 assertEquals("", reader.consumeToAny(new char[]{'e'}));
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
 assertEquals("abcd", reader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd'}));
 assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&<null"));
 assertEquals("&<null", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
 assertEquals("<title>", reader.consumeTagName());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
 assertEquals("abcdef", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("xyz12345"));
 assertEquals("xyz123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("%#65%#6e%#63%"));
 assertEquals("AbC", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
 assertEquals("12345", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
 assertTrue(reader.matches('e'));
 assertTrue(reader.matches("def"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGH"));
 assertTrue(reader.matchesIgnoreCase("def"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
 assertTrue(reader.matchesAny(new char[]{'a', 'e'}));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
 assertTrue(reader.matchesAnySorted(new char[]{'e', 'a'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
 assertTrue(reader.matchesDigit());
 }
}