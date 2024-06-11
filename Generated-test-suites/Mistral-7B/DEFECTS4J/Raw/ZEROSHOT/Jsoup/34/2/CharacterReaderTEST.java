package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CharacterReaderTest class to test all methods in CharacterReader class
 */
public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.consume();
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 characterReader.advance();
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testUnconsume() {
 characterReader.advance();
 characterReader.unconsume();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 characterReader.advance();
 assertEquals('c', characterReader.current());
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
 assertEquals("a", characterReader.consumeAsString());
 characterReader.consume();
 assertEquals("b", characterReader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(1, characterReader.nextIndexOf('b'));
 assertEquals(-1, characterReader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfWithSequence() {
 char[] sequence = new char[]{'b', 'c'};
 assertEquals(2, characterReader.nextIndexOf(new String(sequence)));
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ab", characterReader.consumeTo('b'));
 assertEquals("de", characterReader.consumeTo('e'));
 assertEquals("", characterReader.consumeTo('z'));
 }

 @Test
 public void testConsumeToWithSequence() {
 assertEquals("ab", characterReader.consumeTo(new String("ab")));
 assertEquals("de", characterReader.consumeTo(new String("de")));
 assertEquals("", characterReader.consumeTo(new String("xyz")));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("bc", characterReader.consumeToAny('b', 'c'));
 assertEquals("", characterReader.consumeToAny('x', 'z'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("de", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("ab", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("ab1", characterReader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("31", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('b'));
 assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatchesWithSequence() {
 assertTrue(characterReader.matches(new String("ab")));
 assertFalse(characterReader.matches(new String("xyz")));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase(new String("Ab")));
 assertFalse(characterReader.matchesIgnoreCase(new String("xyz")));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('b', 'c'));
 assertFalse(characterReader.matchesAny('x', 'z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("ab", characterReader.consumeTo('b'));
 assertTrue(characterReader.matchConsume(new String("ab")));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("ab", characterReader.consumeTo('b'));
 assertTrue(characterReader.matchConsumeIgnoreCase(new String("Ab")));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase(new String("ab")));
 assertFalse(characterReader.containsIgnoreCase(new String("xyz")));
 }
}