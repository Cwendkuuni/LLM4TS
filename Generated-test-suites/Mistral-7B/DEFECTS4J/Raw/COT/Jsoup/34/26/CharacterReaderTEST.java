package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = {'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test(expected = java.lang.IndexOutOfBoundsException.class)
 public void testConsumeEmptyList() {
 reader.consume();
 reader.consume();
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("tes", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(3, reader.nextIndexOf(' '));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfCharSequence() {
 CharSequence seq = "CharSequence";
 assertEquals(8, reader.nextIndexOf(seq));
 }

 @Test
 public void testConsumeTo() {
 assertEquals("tes", reader.consumeTo(' '));
 assertEquals("Reader", reader.consumeTo("Reader"));
 assertEquals("", reader.consumeTo('z'));
 }

 @Test
 public void testConsumeToString() {
 assertEquals("tes", reader.consumeTo(" "));
 assertEquals("Reader", reader.consumeTo("Reader"));
 assertEquals("", reader.consumeTo('z'));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("tes", reader.consumeToAny(' ', 's'));
 assertEquals("Reader", reader.consumeToAny('R', 'd'));
 assertEquals("", reader.consumeToAny('z'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("tes CharReader", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("tes", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("tes123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("tesA1B2C3", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("tes123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("tes"));
 assertFalse(reader.matches("zoo"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("tes"));
 assertFalse(reader.matchesIgnoreCase("Zoo"));
 }

 @Test
 public void testMatchesAny() {
 assertFalse(reader.matchesAny('z'));
 assertTrue(reader.matchesAny('s', 't'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('3'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("tes", reader.matchConsume("tes"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("tes", reader.matchConsumeIgnoreCase("tes"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("tes"));
 assertFalse(reader.containsIgnoreCase("ZOO"));
 }

 @Test(expected = AssertionError.class)
 public void testToStringNullInput() {
 new CharacterReader(null).toString();
 }
}