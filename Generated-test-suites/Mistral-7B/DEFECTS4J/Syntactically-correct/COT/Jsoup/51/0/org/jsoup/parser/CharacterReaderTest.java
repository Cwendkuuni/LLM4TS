package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = new char[]{'t', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n', 'g', ' ', 'o', 'f', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', 'Reader'};
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 reader.consume();
 assertEquals('s', reader.current());
 reader.consume(2);
 assertEquals('r', reader.current());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeBeyondEnd() {
 reader.consume(lengthOf("CharacterReaderTest") + 1);
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('s', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('s', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('s', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ts", reader.consumeAsString());
 assertEquals("tring", reader.consumeTo("tring").toString());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(3, reader.nextIndexOf('s'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfWithSequence() {
 String test = "CharacterReader";
 assertEquals(24, reader.nextIndexOf(test));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfWithSequenceBeyondEnd() {
 String test = "JavaTester";
 assertEquals(-1, reader.nextIndexOf(test));
 }

 @Test
 public void testConsumeTo() {
 assertEquals("CharacterReader", reader.consumeTo("CharacterReader").toString());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("Reader", reader.consumeToAny('r', 'e', 'a', 'd', 'e', 'r').toString());
 }

 @Test
 public void testConsumeToAnySorted() {
 assertEquals("Reader", reader.consumeToAnySorted('d', 'e', 'a', 'r').toString());
 }

 @Test
 public void testConsumeData() {
 assertEquals("&", reader.consumeData().charAt(0));
 assertEquals("/", reader.consumeData().charAt(1));
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("/", reader.consumeTagName().charAt(0));
 assertEquals("title", reader.consumeLetterSequence().toString());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("CharacterReader", reader.consumeToEnd().toString());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("title", reader.consumeLetterSequence().toString());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("123", reader.consumeLetterThenDigitSequence().toString());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('s'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesWithSequence() {
 String test = "CharacterReader";
 assertTrue(reader.matches(test));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String test = "characterrReader";
 assertTrue(reader.matchesIgnoreCase(test));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('r', 'e', 'a', 'd', 'e', 'r'));
 assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] test = {'r', 'e', 'a', 'd', 'e', 'r'};
 assertTrue(reader.matchesAnySorted(test));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String test = "title";
 reader.matchConsume(test);
 assertEquals("title", reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String test = "Title";
 reader.matchConsumeIgnoreCase(test);
 assertEquals("title", reader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 String test = "title";
 assertTrue(reader.containsIgnoreCase(test));
 }

 @After
 public void tearDown() {
 reader = null;
 }
}