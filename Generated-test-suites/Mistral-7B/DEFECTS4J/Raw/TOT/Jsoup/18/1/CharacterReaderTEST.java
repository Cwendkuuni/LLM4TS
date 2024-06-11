package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());

 CharacterReader reader1 = new CharacterReader("abc");
 assertFalse(reader1.isEmpty());
 }

 // Test for pos and current methods
 @Test
 public void testPosAndCurrent() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals('a', reader.current());
 assertEquals(0, reader.pos());

 reader.consume();
 assertEquals('b', reader.current());
 assertEquals(1, reader.pos());
 }

 // Test for consume method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 assertEquals('b', reader.current());
 assertEquals(1, reader.pos());

 reader.consume();
 assertEquals('c', reader.current());
 assertEquals(2, reader.pos());
 }

 // Test for unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 reader.unconsume();
 assertEquals('a', reader.current());
 assertEquals(0, reader.pos());
 }

 // Test for advance method
 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("abc");
 reader.advance();
 assertEquals('b', reader.current());
 assertEquals(1, reader.pos());
 }

 // Test for mark and rewindToMark methods
 @Test
 public void testMarkAndRewindToMark() {
 CharacterReader reader = new CharacterReader("abc");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 assertEquals(0, reader.pos());
 }

 // Test for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals("bc", reader.consumeAsString());
 assertEquals('c', reader.current());
 assertEquals(3, reader.pos());
 }

 // Test for consumeTo method with single character
 @Test
 public void testConsumeToSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("bc", reader.consumeTo('c'));
 assertEquals('d', reader.current());
 assertEquals(4, reader.pos());
 }

 // Test for consumeTo method with multiple characters
 @Test
 public void testConsumeToMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("bcde", reader.consumeTo("de"));
 assertEquals('e', reader.current());
 assertEquals(7, reader.pos());
 }

 // Test for consumeTo method with end of string
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals("abc", reader.consumeToEnd());
 assertEquals(EOF, reader.current());
 assertEquals(3, reader.pos());
 }

 // Test for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd123Efghij");
 assertEquals("bcde", reader.consumeLetterSequence());
 assertEquals('i', reader.current());
 assertEquals(10, reader.pos());
 }

 // Test for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("1234abcdE5f");
 assertEquals("34abcdE", reader.consumeHexSequence());
 assertEquals('5', reader.current());
 assertEquals(11, reader.pos());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345abcd");
 assertEquals("5", reader.consumeDigitSequence());
 assertEquals('a', reader.current());
 assertEquals(6, reader.pos());
 }

 // Test for matches method
 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('d'));
 assertFalse(reader.matches('e'));
 }

 // Test for matchesIgnoreCase method
 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesIgnoreCase("d"));
 assertFalse(reader.matchesIgnoreCase("E"));
 }

 // Test for matchesAny method
 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
 assertFalse(reader.matchesAny('e'));
 }

 // Test for containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("abc<title>Def</title>efghijk");
 assertTrue(reader.containsIgnoreCase("<title>"));
 assertTrue(reader.containsIgnoreCase("DEF"));
 assertFalse(reader.containsIgnoreCase("<style>"));
 }

 // Test for toString method
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals("bc", reader.toString());
 }
}