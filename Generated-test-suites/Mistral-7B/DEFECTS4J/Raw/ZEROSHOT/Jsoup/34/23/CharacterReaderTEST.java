package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderNonEmpty() {
 CharacterReader reader = new CharacterReader("Hello World!");
 assertFalse(reader.isEmpty());
 assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 CharacterReader reader = new CharacterReader("Hello World!");
 assertEquals('H', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testCharacterReaderConsumePastEnd() {
 CharacterReader reader = new CharacterReader("Hello World!");
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // This should throw an exception
 }

 @Test
 public void testCharacterReaderUnconsume() {
 CharacterReader reader = new CharacterReader("Hello World!");
 reader.consume();
 reader.unconsume();
 assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderAdvance() {
 CharacterReader reader = new CharacterReader("Hello World!");
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderMark() {
 CharacterReader reader = new CharacterReader("Hello World!");
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("d", reader.consumeAsString());
 assertEquals("abc", reader.toString());
 }

 @Test
 public void testCharacterReaderNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdefghijk");
 int index = reader.nextIndexOf('h');
 assertEquals(6, index);
 }

 @Test
 public void testCharacterReaderNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 int index = reader.nextIndexOf("ab");
 assertEquals(4, index);
 }

 @Test
 public void testCharacterReaderNextIndexOfEmptySequence() {
 CharacterReader reader = new CharacterReader("abcd");
 int index = reader.nextIndexOf("");
 assertEquals(-1, index);
 }

 @Test
 public void testCharacterReaderConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertEquals("bc", reader.consumeTo('c'));
 }

 @Test
 public void testCharacterReaderConsumeToString() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertEquals("cd", reader.consumeTo("de"));
 }

 @Test
 public void testCharacterReaderConsumeToAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertEquals("b", reader.consumeToAny('b'));
 }

 @Test
 public void testCharacterReaderConsumeToAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertEquals("bc", reader.consumeToAny(new char[]{'b', 'c'}));
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("d", reader.consumeToEnd().charAt(0));
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdABC123efg");
 assertEquals("cdABC123", reader.consumeLetterSequence());
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcdABC123efg");
 assertEquals("cdABC123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcd%#F0F0F0F0");
 assertEquals("%#F0F0F0F0", reader.consumeHexSequence());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123efg456");
 assertEquals("123456", reader.consumeDigitSequence());
 }

 @Test
 public void testCharacterReaderMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('d'));
 }

 @Test
 public void testCharacterReaderMatchesString() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertTrue(reader.matches("ab"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCaseSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesIgnoreCase('D'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCaseString() {
 CharacterReader reader = new CharacterReader("abcdABC123efg");
 assertTrue(reader.matchesIgnoreCase("ABC"));
 }

 @Test
 public void testCharacterReaderMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesAny('b'));
 assertTrue(reader.matchesAny('d'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("abcd123");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchConsume('d'));
 assertEquals("", reader.toString());
 }

 @Test
 public void testCharacterReaderMatchConsumeString() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertTrue(reader.matchConsume("ab"));
 assertEquals("cd", reader.toString());
 }

 @Test
 public void testCharacterReaderContainsIgnoreCaseString() {
 CharacterReader reader = new CharacterReader("abcdABC123efg");
 assertTrue(reader.containsIgnoreCase("ABC"));
 }
}