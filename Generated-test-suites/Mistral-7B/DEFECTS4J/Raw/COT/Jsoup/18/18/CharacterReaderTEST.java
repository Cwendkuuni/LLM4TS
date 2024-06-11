package org.jsoup.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.CharacterReader;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

 private static final String TEST_STRING = "abcdef123ABC 12345<tag />";

 @Test
 public void testCharacterReaderCreation() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertNotNull(reader);
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 int pos = reader.pos();
 assertEquals(2, pos);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 reader = new CharacterReader(TEST_STRING);
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 char current = reader.current();
 assertEquals('c', current);
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 reader.consume();
 char consumed = reader.consume();
 assertEquals('d', consumed);
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 reader.consume();
 reader.unconsume();
 char current = reader.current();
 assertEquals('c', current);
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 reader.advance();
 int pos = reader.pos();
 assertEquals(3, pos);
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 char current = reader.current();
 assertEquals('c', current);
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeAsString();
 assertEquals("def", consumed);
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeTo('f');
 assertEquals("def", consumed);
 consumer = reader.consumeTo('g');
 assertEquals("", consumer);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeToEnd();
 assertEquals("abcdef123ABC 12345<tag />", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeLetterSequence();
 assertEquals("abc", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeHexSequence();
 assertEquals("123", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeDigitSequence();
 assertEquals("12345", consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToWithNegativeOffset() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consumeTo(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToWithNullSeq() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consumeTo(null);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matches('d'));
 assertFalse(reader.matches('g'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matches("def"));
 assertFalse(reader.matches("abc"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesIgnoreCase("Def"));
 assertFalse(reader.matchesIgnoreCase("ABC"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesAny('d', 'e'));
 assertFalse(reader.matchesAny('g', 'e'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchConsume("def"));
 assertFalse(reader.matchConsume("abc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchConsumeIgnoreCase("Def"));
 assertFalse(reader.matchConsumeIgnoreCase("ABC"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.containsIgnoreCase("tag"));
 assertFalse(reader.containsIgnoreCase("TAG"));
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals("def123ABC 12345<tag />", reader.toString());
 }

}