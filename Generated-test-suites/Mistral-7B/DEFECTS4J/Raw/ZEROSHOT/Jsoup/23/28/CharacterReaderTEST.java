package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] testInput = new char[]{'a', 'b', 'c', 'd', 'e'};
 characterReader = new CharacterReader(new String(testInput));
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
 public void testCurrent() {
 assertEquals('a', characterReader.current());
 characterReader.consume();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertFalse(characterReader.matches('a'));
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
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
 String consumed = characterReader.consumeAsString();
 assertEquals("ab", characterReader.toString());
 assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo('c');
 assertEquals("ab", characterReader.toString());
 assertEquals("bc", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 assertEquals("", characterReader.toString());
 assertEquals("def", consumed);
 }

 @Test
 public void testConsumeToAny() {
 characterReader.advance();
 String consumed = characterReader.consumeToAny('b', 'c');
 assertEquals("bcdef", characterReader.toString());
 assertEquals("c", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String sequence = characterReader.consumeLetterSequence();
 assertEquals("def", characterReader.toString());
 assertEquals("def", sequence);
 }

 @Test
 public void testConsumeHexSequence() {
 String sequence = characterReader.consumeHexSequence();
 assertEquals("", characterReader.toString());
 assertEquals("", sequence);
 }

 @Test
 public void testConsumeDigitSequence() {
 String sequence = characterReader.consumeDigitSequence();
 assertEquals("def", characterReader.toString());
 assertEquals("0123", sequence);
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('a'));
 assertFalse(characterReader.matches('b'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("A"));
 assertFalse(characterReader.matchesIgnoreCase("B"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('b', 'c'));
 assertFalse(characterReader.matchesAny('x', 'y'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(characterReader.matchesDigit());
 assertTrue(characterReader.matchesDigit('1'));
 }

 @Test
 public void testMatchConsume() {
 String sequence = "ab";
 assertTrue(characterReader.matchConsume(sequence));
 assertEquals("cd", characterReader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String sequence = "Ab";
 assertTrue(characterReader.matchConsumeIgnoreCase(sequence));
 assertEquals("cd", characterReader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 String sequence = "ab";
 assertTrue(characterReader.containsIgnoreCase(sequence));
 }

 @Test (expected = AssertionError.class)
 public void testConsumePastEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume(); // should throw an exception
 }

}