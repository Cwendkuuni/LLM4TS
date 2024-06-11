package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.consumeToEnd());
 }

 @Test
 public void testSimpleCharacterConsumption() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals('a', reader.current());
 assertEquals("a", reader.consumeAsString());
 reader.advance();
 assertEquals('b', reader.current());
 reader.consume(); // consume 'b'
 reader.unconsume(); // unconsume 'b'
 assertEquals('b', reader.current());
 reader.consume(); // consume 'b'
 assertEquals('c', reader.current());
 }

 @Test
 public void testCharacterSequenceConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals("ab", reader.consumeTo('c'));
 assertEquals("def", reader.consumeToEnd());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeBeyondEndOfInput() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consumeTo('d');
 }

 @Test
 public void testStringSequenceConsumption() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 assertEquals("abcd", reader.consumeTo("abcd"));
 assertEquals("", reader.consumeTo("efgh"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNullSequence() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeTo(null);
 }

 @Test
 public void testConsumeToAnyCharacter() {
 CharacterReader reader = new CharacterReader("abcdefg");
 assertEquals("bcdefg", reader.consumeToAny('b', 'f', 'g'));
 }

 @Test
 public void testConsumeToAnySortedCharacter() {
 CharacterReader reader = new CharacterReader("hijklmnopqrstuvwxyz");
 assertEquals("ijklmnopqrstuvwxyz", reader.consumeToAnySorted('i', 'j', 'k'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToAnyNullCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeToAny(null);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<abc");
 assertEquals("&<", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<tag name=\"abc\">");
 assertEquals("<tag", reader.consumeTagName());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals("abcdef", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijk");
 assertEquals("abcdefg", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&hxea&hxb");
 assertEquals("&hxea&hxb", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertEquals("1234567890", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('d'));
 assertFalse(reader.matches('e'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches("d"));
 assertFalse(reader.matches("e"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesIgnoreCase("d"));
 assertFalse(reader.matchesIgnoreCase("e"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertTrue(reader.matchesAny('a', 'b', 'e', 'f'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("hijklmnopqrstuvwxyz");
 assertTrue(reader.matchesAnySorted('i', 'j', 'k'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("12345");
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }
}