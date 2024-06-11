package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 assertEquals('A', reader.current());
 }

 @Test
 public void testReaderConsumeCharacter() {
 CharacterReader reader = new CharacterReader("AB");
 assertEquals('A', reader.current());
 reader.consume();
 assertEquals('B', reader.current());
 }

 @Test
 public void testReaderConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("ABC");
 assertEquals('A', reader.current());
 reader.consume();
 assertEquals('B', reader.current());
 reader.consume();
 assertEquals('C', reader.current());
 reader.consume(); // consume past end of input
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 reader.mark();
 assertEquals('A', reader.current());
 reader.consume();
 reader.rewindToMark();
 assertEquals('A', reader.current());
 }

 @Test
 public void testReaderNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("ABABAB");
 int index = reader.nextIndexOf('A');
 assertEquals(2, index);
 }

 @Test
 public void testReaderNextIndexOfMultipleCharacters() {
 CharacterReader reader = new CharacterReader("ABCABCABC");
 int index = reader.nextIndexOf("AB");
 assertEquals(3, index);
 }

 @Test
 public void testReaderNextIndexOfEmptySequence() {
 CharacterReader reader = new CharacterReader("ABC");
 int index = reader.nextIndexOf("");
 assertEquals(-1, index);
 }

 @Test
 public void testReaderConsumeAsString() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertEquals("AB", reader.consumeAsString());
 }

 @Test
 public void testReaderConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertEquals("AB", reader.consumeTo('C').toString());
 }

 @Test
 public void testReaderConsumeToString() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertEquals("DEF", reader.consumeToEnd().toString());
 }

 @Test
 public void testReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
 assertEquals("abcdef", reader.consumeLetterSequence().toString());
 }

 @Test
 public void testReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 assertEquals("abc123", reader.consumeLetterThenDigitSequence().toString());
 }

 @Test
 public void testReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a2b3c");
 assertEquals("1a2b3c", reader.consumeHexSequence().toString());
 }

 @Test
 public void testReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("123456", reader.consumeDigitSequence().toString());
 }

 @Test
 public void testReaderMatches() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertTrue(reader.matches('A'));
 assertTrue(reader.matches("AB"));
 assertFalse(reader.matches('B'));
 }

 @Test
 public void testReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 assertTrue(reader.matchesIgnoreCase("Ab"));
 assertTrue(reader.matchesIgnoreCase("ab"));
 assertFalse(reader.matchesIgnoreCase("C"));
 }

 @Test
 public void testReaderMatchesAnySingleCharacter() { {CharacterReader reader = new CharacterReader("ABCDEF"); assertTrue(reader.matchesAny('A')); assertTrue(reader.matchesAny('B')); assertTrue(reader.matchesAny('C')); assertTrue(reader.matchesAny('D')); assertTrue(reader.matchesAny('E')); assertTrue(reader.matchesAny('F')); } }

 @Test
 public void testReaderMatchesAnyMultipleCharacters() { {CharacterReader reader = new CharacterReader("ABCDEF"); assertTrue(reader.matchesAny(new char[]{'A', 'B'})); assertTrue(reader.matchesAny(new char[]{'B', 'C'})); assertFalse(reader.matchesAny(new char[]{'X', 'Y'})); } }

 @Test
 public void testReaderMatchesAnySorted() { {CharacterReader reader = new CharacterReader("ABCDEF"); assertTrue(reader.matchesAnySorted(new char[]{'A', 'B', 'C', 'D', 'E', 'F'})); assertTrue(reader.matchesAnySorted(new char[]{'F', 'E', 'D', 'C', 'B', 'A'})); assertFalse(reader.matchesAnySorted(new char[]{'X', 'Y'})); } }

 @Test
 public void testReaderToString() { CharacterReader reader = new CharacterReader("ABCDEF"); assertEquals("ABCDEF", reader.toString()); }
}