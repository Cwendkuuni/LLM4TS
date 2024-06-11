package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

/**
 * CharacterReader Test Cases
 */
public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 CharacterReader emptyReader = new CharacterReader("");
 assertTrue(emptyReader.isEmpty());

 characterReader.unconsume();
 characterReader.unconsume();
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 characterReader.advance();
 characterReader.advance();
 assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', characterReader.current());
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.advance();
 characterReader.advance();
 characterReader.unconsume();
 characterReader.unconsume();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ab", characterReader.consumeAsString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfNegativeChar() {
 characterReader.nextIndexOf((char) -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfEmptySequence() {
 characterReader.nextIndexOf("");
 }

 @Test
 public void testNextIndexOfSingleChar() {
 assertEquals(0, characterReader.nextIndexOf('a'));
 }

 @Test
 public void testNextIndexOfMultipleCharsInSequence() {
 assertEquals(1, characterReader.nextIndexOf("ba"));
 }

 @Test
 public void testNextIndexOfMultipleCharsInSequence_multipleOccurrences() {
 assertEquals(6, characterReader.nextIndexOf("AB"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfCharNotInSequence() {
 characterReader.nextIndexOf('z');
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ab", characterReader.consumeTo('b'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcde", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("ab1cd2ef3g", characterReader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("a1b2c3d4e5f", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123456789", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('a'));
 assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("ab"));
 assertFalse(characterReader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 assertTrue(characterReader.matchesIgnoreCase("Ab"));
 assertFalse(characterReader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('a', 'b'));
 assertFalse(characterReader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("ab", characterReader.consumeTo('b'));
 assertTrue(characterReader.matchConsume("ab"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("ab", characterReader.consumeTo('b'));
 assertTrue(characterReader.matchConsumeIgnoreCase("Ab"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("ab"));
 assertTrue(characterReader.containsIgnoreCase("AB"));
 assertFalse(characterReader.containsIgnoreCase("xyz"));
 }
}