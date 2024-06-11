package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = {'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 assertNotNull(characterReader);
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertNull(characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeforeEmpty() {
 characterReader.consume();
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 assertEquals('t', characterReader.current());
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testConsumeMultiple() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 assertEquals('s', characterReader.current());
 assertEquals(3, characterReader.pos());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('s', characterReader.current());
 assertEquals(3, characterReader.pos());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('s', characterReader.current());
 assertEquals(1, characterReader.pos() - characterReader.mark());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('s', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("tes", characterReader.consumeAsString());
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(2, characterReader.nextIndexOf(' '));
 assertEquals(-1, characterReader.nextIndexOf('x'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 CharacterReader characterReaderWithSeq = new CharacterReader("test CharacterReader");
 assertEquals(6, characterReaderWithSeq.nextIndexOf("CharacterReader"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextIndexOfWithEmptyCharSequence() {
 new CharacterReader("test").nextIndexOf("");
 }

 @Test
 public void testConsumeTo() {
 assertEquals("te", characterReader.consumeTo(' '));
 assertEquals(5, characterReader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("test CharacterReader", characterReader.consumeToEnd());
 assertEquals(23, characterReader.pos());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("tes", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("test1", characterReader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("test%20CharacterReader", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('t'));
 assertFalse(characterReader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("tes"));
 assertFalse(characterReader.matches("tester"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("tes"));
 assertFalse(characterReader.matchesIgnoreCase("TESTER"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('t', 's'));
 assertFalse(characterReader.matchesAny('x', 't'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(characterReader.matchesDigit());
 assertTrue(characterReader.matchesDigit('3'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("tes", characterReader.matchConsume("tes"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("tes", characterReader.matchConsumeIgnoreCase("tes"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("CharacterReader"));
 }

 @Test
 public void testToString() {
 assertEquals("test CharacterReader", characterReader.toString());
 }
}