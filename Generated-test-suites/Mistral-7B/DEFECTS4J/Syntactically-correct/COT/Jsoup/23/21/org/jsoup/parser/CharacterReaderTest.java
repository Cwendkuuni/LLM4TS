package org.jsoup.parser.test;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r', ' ', '}', '\n', ' ', '\t'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.current());
 assertEquals('e', reader.consume());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 reader.consume();
 reader.consume(); // consume two characters, leaving one left
 reader.consume(); // should throw exception
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("is a", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("a ", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("C h a r c t e r R e d e r \n \t", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("Character", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("red", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches(" a "));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 assertTrue(reader.matchesIgnoreCase(" A "));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(' ', 'a'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("Character", reader.matchConsume("Character"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("Character", reader.matchConsumeIgnoreCase("Character"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testToString() {
 assertEquals("CharacterRede", reader.toString());
 }
}