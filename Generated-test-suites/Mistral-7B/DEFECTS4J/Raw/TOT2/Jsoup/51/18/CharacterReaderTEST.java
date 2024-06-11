package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("a");
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 }

 @Test
 public void testReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.current());
 assertEquals('c', reader.consume());
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testReaderMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcde");
 reader.mark();
 reader.consume(); // consume 'a'
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testReaderConsumeTo() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals("bcdef", reader.consumeTo('e'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReaderConsumeToNegativeOffset() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeTo(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReaderConsumeToBeyondEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeTo(CharacterReader.EOF);
 }

 @Test
 public void testReaderConsumeToString() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals("abcdef", reader.consumeToEnd());
 }

 @Test
 public void testReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdEFG");
 assertEquals("dEFG", reader.consumeLetterSequence());
 }

 @Test
 public void testReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd1234");
 assertEquals("234", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcd%5Fghij");
 assertEquals("ghij", reader.consumeHexSequence());
 }

 @Test
 public void testReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd1234");
 assertEquals("4", reader.consumeDigitSequence());
 }

 @Test
 public void testReaderMatches() {
 CharacterReader reader = new CharacterReader("abcd1234");
 assertTrue(reader.matches('d'));
 assertTrue(reader.matches("cd"));
 assertFalse(reader.matches('w'));
 }

 @Test
 public void testReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd1234");
 assertTrue(reader.matchesIgnoreCase("CD"));
 assertFalse(reader.matchesIgnoreCase("w"));
 }

 @Test
 public void testReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd1234");
 assertTrue(reader.matchesAny('d'));
 assertTrue(reader.matchesAny('c'));
 assertFalse(reader.matchesAny('w'));
 }

 @Test
 public void testReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcd1234");
 assertTrue(reader.matchesAnySorted(Arrays.asList('c', 'd').toArray(new char[0])));
 assertFalse(reader.matchesAnySorted(Arrays.asList('w').toArray(new char[0])));
 }

 @Test
 public void testReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd<title>efg</title>hij");
 assertTrue(reader.containsIgnoreCase("/title"));
 assertTrue(reader.containsIgnoreCase("TITLE"));
 assertFalse(reader.containsIgnoreCase("/foo"));
 }

}