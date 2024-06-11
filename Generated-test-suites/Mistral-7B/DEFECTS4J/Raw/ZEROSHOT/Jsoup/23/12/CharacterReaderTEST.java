package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', '\n', 'i', 's', ' ', 'a', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', '\n', 'r', 'e', 'a', 'd', 'e', 'r', ' ', 'm', 'y', ' ', 't', 'e', 's', 't', '\n'};
 String inputString = new String(testInput);
 reader = new CharacterReader(inputString);
 }

 @Test
 public void testConstructor() {
 assertNotNull(reader);
 assertEquals(inputString, reader.input);
 assertEquals(0, reader.pos);
 assertEquals(0, reader.mark);
 }

 @Test
 public void testPos() {
 assertSame(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
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
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("t", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("is ", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToString() {
 assertEquals("a ", reader.consumeTo(" "));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("a ", reader.consumeToAny(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("readermy test\n", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("character", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("readermy", reader.consumeHexSequence());
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
 assertTrue(reader.matches("is "));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 assertTrue(reader.matchesIgnoreCase("is "));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'r'));
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
 assertEquals("character", reader.matchConsume("character"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("character", reader.matchConsumeIgnoreCase("Character"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("test"));
 }

 @Test
 public void testToString() {
 assertEquals("readermy test\n", reader.toString());
 }
}