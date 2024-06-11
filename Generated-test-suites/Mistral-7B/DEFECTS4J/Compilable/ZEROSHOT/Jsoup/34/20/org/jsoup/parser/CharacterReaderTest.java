package org.jsoup.parser;
import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testCharacterReaderSingleCharacter() {
 CharacterReader reader = new CharacterReader("a");
 assertFalse(reader.isEmpty());
 assertEquals("a", reader.consumeAsString());
 assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 assertFalse(reader.isEmpty());
 assertEquals("abc", reader.consumeToEnd());
 assertEquals('a', reader.current());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.consume());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testCharacterReaderConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testCharacterReaderConsumeSubstring() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("bc", reader.consumeTo("cd").toString());
 }

 @Test
 public void testCharacterReaderConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("ab", reader.consumeLetterSequence().toString());
 assertEquals("ab1", reader.consumeLetterThenDigitSequence().toString());
 assertEquals("0f", reader.consumeHexSequence().toString());
 assertEquals("123", reader.consumeDigitSequence().toString());
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches('e'));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abCD");
 assertTrue(reader.matchesIgnoreCase("ab"));
 assertTrue(reader.matchesIgnoreCase("CD"));
 assertFalse(reader.matchesIgnoreCase("ef"));
 assertFalse(reader.matchesIgnoreCase("abcd"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesAny('a', 'b'));
 assertTrue(reader.matchesAny('a', 'c'));
 assertFalse(reader.matchesAny('e', 'f'));
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("abCdEf");
 assertTrue(reader.containsIgnoreCase("cd"));
 assertTrue(reader.containsIgnoreCase("EF"));
 assertFalse(reader.containsIgnoreCase("gh"));
 }
}