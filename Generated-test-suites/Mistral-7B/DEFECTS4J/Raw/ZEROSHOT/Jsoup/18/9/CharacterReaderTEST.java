package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

 CharacterReader characterReader;

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
 public void testCurrentAndConsume() {
 assertEquals('a', characterReader.current());
 characterReader.consume();
 assertEquals('b', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume(); // should throw an exception
 }

 @Test
 public void testMarkAndRewind() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "ab";
 String actual = characterReader.consumeAsString();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "bc";
 characterReader.advance();
 String actual = characterReader.consumeTo('c');
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "def";
 String actual = characterReader.consumeToEnd();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abc";
 String actual = characterReader.consumeLetterSequence();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "123";
 String actual = characterReader.consumeHexSequence();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String actual = characterReader.consumeDigitSequence();
 assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('b'));
 assertFalse(characterReader.matches('d'));
 }

 @Test
 public void testMatchesString() {
 String expected = "ab";
 String actual = characterReader.consumeTo('b').consumeTo('a');
 assertTrue(characterReader.matches(expected));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String expected = "Ab";
 assertTrue(characterReader.matchesIgnoreCase(expected));
 }

 @Test
 public void testMatchesAny() {
 char[] expected = new char[]{'a', 'b'};
 char[] actual = characterReader.consumeToAny(expected).toCharArray();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expected = "cd";
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 String actual = characterReader.matchConsume("cd");
 assertEquals(expected, actual);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "CD";
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.advance();
 String actual = characterReader.matchConsumeIgnoreCase("CD");
 assertEquals(expected, actual);
 }

 @Test
 public void testContainsIgnoreCase() {
 String input = "This is a test <title>Test Title</title>";
 CharacterReader reader = new CharacterReader(input);
 assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testToString() {
 assertEquals("def", characterReader.toString());
 }
}