package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 reader = new CharacterReader("TestString");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('T', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals('s', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('T', reader.current());
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
 assertEquals('T', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("Test", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("est", reader.consumeTo('s'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("String", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("Test", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("Test", reader.consumeHexSequence()); // Assuming "Test" contains only hex characters or their equivalent ASCII values
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("est"));
 assertFalse(reader.matches("foo"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("est"));
 assertTrue(reader.matchesIgnoreCase("Est"));
 assertFalse(reader.matchesIgnoreCase("foo"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('t', 'e'));
 assertFalse(reader.matchesAny('x', 'e'));
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
 assertTrue(reader.matchConsume("est"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertTrue(reader.matchConsumeIgnoreCase("Est"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("est"));
 }

 @Test
 public void testConsumeNullInput() {
 assertNull(new CharacterReader(null));
 }
}