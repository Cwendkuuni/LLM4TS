package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructor() {
 CharactercReader reader = new CharacterReader("testString");
 assertNotNull(reader);
 }

 @Test
 public void testPos() {
 CharactercReader reader = new CharacterReader("testString");
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 CharactercReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharactercReader reader = new CharacterReader("testString");
 reader.consume();
 assertEquals(reader.current(), 't');
 }

 @Test
 public void testConsume() {
 CharactercReader reader = new CharacterReader("testString");
 reader.consume();
 reader.consume();
 assertEquals(reader.current(), 's');
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeyondEnd() {
 CharactercReader reader = new CharacterReader("testString");
 reader.consume();
 reader.consume();
 reader.consume(); // consume one more than the length of the string
 }

 @Test
 public void testUnconsume() {
 CharactercReader reader = new CharacterReader("testString");
 reader.consume();
 reader.unconsume();
 assertEquals(reader.current(), 't');
 }

 @Test
 public void testAdvance() {
 CharactercReader reader = new CharacterReader("testString");
 reader.advance();
 assertEquals(reader.pos(), 1);
 }

 @Test
 public void testMark() {
 CharactercReader reader = new CharacterReader("testString");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals(reader.current(), 't');
 }

 @Test
 public void testConsumeAsString() {
 CharactercReader reader = new CharacterReader("testString");
 assertEquals(reader.consumeAsString(), "");
 reader.consume();
 reader.consume();
 assertEquals(reader.consumeAsString(), "st");
 }

 @Test
 public void testConsumeTo() {
 CharactercReader reader = new CharacterReader("testStringTest");
 assertEquals(reader.consumeTo('t'), "est");
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(reader.consumeTo('e'), "stest");
 }

 @Test
 public void testConsumeToStringSeq() {
 CharactercReader reader = new CharacterReader("testStringTest");
 assertEquals(reader.consumeTo("est"), "test");
 }

 @Test
 public void testConsumeToAny() {
 CharactercReader reader = new CharacterReader("abcdAbfg");
 assertEquals(reader.consumeToAny('a', 'b'), "ab");
 assertEquals(reader.consumeToAny('c', 'f', 'g'), "dAbfg");
 }

 @Test
 public void testConsumeToEnd() {
 CharactercReader reader = new CharacterReader("testString");
 assertEquals(reader.consumeToEnd(), "testString");
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(reader.consumeToEnd(), "string");
 }

 @Test
 public void testConsumeLetterSequence() {
 CharactercReader reader = new CharacterReader("testString");
 assertEquals(reader.consumeLetterSequence(), "");
 reader.consume();
 reader.consume();
 assertEquals(reader.consumeLetterSequence(), "st");
 }

 @Test
 public void testConsumeHexSequence() {
 CharactercReader reader = new CharacterReader("test%String");
 assertEquals(reader.consumeHexSequence(), "%String");
 }

 @Test
 public void testConsumeDigitSequence() {
 CharactercReader reader = new CharacterReader("test123String");
 assertEquals(reader.consumeDigitSequence(), "123");
 }

 @Test
 public void testMatches() {
 CharactercReader reader = new CharacterReader("testString");
 assertTrue(reader.matches('t'));
 assertTrue(reader.matches("es"));
 assertFalse(reader.matches('g'));
 assertFalse(reader.matches("\n"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharactercReader reader = new CharacterReader("testString");
 assertTrue(reader.matchesIgnoreCase("es"));
 assertFalse(reader.matchesIgnoreCase("G"));
 }

 @Test
 public void testMatchesAny() {
 CharactercReader reader = new CharacterReader("testString");
 assertTrue(reader.matchesAny('t', 's'));
 assertFalse(reader.matchesAny('g'));
 }

 @Test
 public void testMatchesLetter() {
 CharactercReader reader = new CharacterReader("testString");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('6'));
 }

 @Test
 public void testMatchesDigit() {
 CharactercReader reader = new CharacterReader("testString");
 assertFalse(reader.matchesDigit());
 assertFalse(reader.matchesDigit('2'));
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharactercReader reader = new CharacterReader("testStringTest");
 assertTrue(reader.matchConsume("est"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharactercReader reader = new CharacterReader("testStringTest");
 assertTrue(reader.matchConsumeIgnoreCase("EST"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharactercReader reader = new CharacterReader("test<title>String<title>Test</title>");
 assertTrue(reader.containsIgnoreCase("/title>"));
 assertTrue(reader.containsIgnoreCase("/title"));
 assertFalse(reader.containsIgnoreCase("/head"));
 }

 @Test
 public void testToString() {
 CharactercReader reader = new CharacterReader("testString");
 assertEquals(reader.toString(), "testString");
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(reader.toString(), "string");
 }
}