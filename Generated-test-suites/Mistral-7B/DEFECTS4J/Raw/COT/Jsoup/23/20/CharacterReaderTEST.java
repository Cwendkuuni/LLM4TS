package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 this.reader = new CharacterReader("This is a test for CharacterReader class.");
 }

 // Testing isEmpty method
 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 // Testing pos(), current(), consume(), and unconsume() methods
 @Test
 public void testCurrentPositionMethods() {
 assertEquals('T', reader.current());
 reader.consume();
 assertEquals('h', reader.current());
 reader.unconsume();
 assertEquals('h', reader.current());
 reader.consume();
 assertEquals('i', reader.current());
 }

 // Testing advance() and mark() methods
 @Test
 public void testMarkAndAdvanceMethods() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('h', reader.current());
 reader.consume();
 assertEquals('i', reader.current());
 }

 // Testing consumeAsString() method
 @Test
 public void testConsumeAsString() {
 String expected = "is a test for CharacterReader class.";
 String actual = reader.consumeAsString();
 assertEquals(expected, actual);
 }

 // Testing consumeTo() method with single character
 @Test
 public void testConsumeToSingleCharacter() {
 String expected = "a test for CharacterReader class.";
 String actual = reader.consumeTo(' ');
 assertEquals(expected, reader.input().substring(0, reader.pos()));
 }

 // Testing consumeTo() method with multiple characters
 @Test
 public void testConsumeToMultipleCharacters() {
 String expected = "a test for CharacterReader class.";
 String actual = reader.consumeTo("is");
 assertEquals(expected, reader.input().substring(0, reader.pos()));
 }

 // Testing consumeToAny() method with single character
 @Test
 public void testConsumeToAnySingleCharacter() {
 String expected = "is a test for CharacterReader class.";
 reader.consume('i');
 String actual = reader.consumeToAny('s');
 assertEquals(expected.substring(reader.pos()), actual);
 }

 // Testing consumeToAny() method with multiple characters
 @Test
 public void testConsumeToAnyMultipleCharacters() {
 String expected = "is a test for CharacterReader class.";
 reader.consume('i');
 String actual = reader.consumeToAny('s', 't');
 assertEquals(expected.substring(reader.pos()), actual);
 }

 // Testing consumeToEnd() method
 @Test
 public void testConsumeToEnd() {
 String expected = "is a test for CharacterReader class.";
 String actual = reader.consumeToEnd();
 assertEquals(expected, reader.input());
 }

 // Testing consumeLetterSequence() method
 @Test
 public void testConsumeLetterSequence() {
 String expected = "test for CharacterReader class.";
 String actual = reader.consumeLetterSequence();
 assertEquals(expected, actual);
 }

 // Testing consumeHexSequence() method with valid hex sequence
 @Test
 public void testConsumeHexSequenceValid() {
 String expected = "0x686563616d65";
 String actual = reader.consumeHexSequence();
 assertEquals(expected, actual);
 }

 // Testing consumeHexSequence() method with invalid character
 @Test(expected = AssertionError.class)
 public void testConsumeHexSequenceInvalid() {
 reader.consume('x');
 reader.consume('6');
 reader.consume('8');
 reader.consume('I'); // This should cause an AssertionError
 String actual = reader.consumeHexSequence();
 }

 // Testing consumeDigitSequence() method with valid digit sequence
 @Test
 public void testConsumeDigitSequenceValid() {
 String expected = "123";
 String actual = reader.consumeDigitSequence();
 assertEquals(expected, actual);
 }

 // Testing consumeDigitSequence() method with invalid character
 @Test(expected = AssertionError.class)
 public void testConsumeDigitSequenceInvalid() {
 reader.consume('1');
 reader.consume('2');
 reader.consume('x'); // This should cause an AssertionError
 String actual = reader.consumeDigitSequence();
 }

 // Testing matches() method with single character
 @Test
 public void testMatchesSingleCharacter() {
 assertTrue(reader.matches('t'));
 reader.consume();
 assertFalse(reader.matches('t'));
 }

 // Testing matches() method with multiple characters
 @Test
 public void testMatchesMultipleCharacters() {
 assertTrue(reader.matches("is"));
 reader.advance();
 reader.advance();
 reader.consume();
 assertFalse(reader.matches("is"));
 }

 // Testing matchesIgnoreCase() method with single character
 @Test
 public void testMatchesIgnoreCaseSingleCharacter() {
 assertTrue(reader.matchesIgnoreCase("T"));
 reader.consume();
 assertFalse(reader.matchesIgnoreCase("T"));
 }

 // Testing matchesIgnoreCase() method with multiple characters
 @Test
 public void testMatchesIgnoreCaseMultipleCharacters() {
 assertTrue(reader.matchesIgnoreCase("is"));
 reader.advance();
 reader.advance();
 reader.consume();
 assertFalse(reader.matchesIgnoreCase("is"));
 }

 // Testing matchesAny() method with single character
 @Test
 public void testMatchesAnySingleCharacter() {
 assertTrue(reader.matchesAny('s'));
 reader.consume();
 assertFalse(reader.matchesAny('s'));
 }

 // Testing matchesAny() method with multiple characters
 @Test
 public void testMatchesAnyMultipleCharacters() {
 assertTrue(reader.matchesAny('s', 't'));
 reader.consume();
 reader.consume();
 assertFalse(reader.matchesAny('s', 't'));
 }

 // Testing matchesLetter() method
 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 reader.consume();
 assertFalse(reader.matchesLetter());
 }

 // Testing matchesDigit() method
 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 reader.consume();
 assertFalse(reader.matchesDigit());
 }

 // Testing matchConsume() method with single character
 @Test
 public void testMatchConsumeSingleCharacter() {
 String expected = "for CharacterReader class.";
 String actual = reader.matchConsume("t");
 assertEquals(expected, reader.input().substring(0, reader.pos()));
 }

 // Testing matchConsume() method with multiple characters
 @Test
 public void testMatchConsumeMultipleCharacters() {
 String expected = "for CharacterReader class.";
 String actual = reader.matchConsume("is");
 assertEquals(expected, reader.input().substring(0, reader.pos()));
 }

 // Testing matchConsumeIgnoreCase() method with single character
 @Test
 public void testMatchConsumeIgnoreCaseSingleCharacter() {
 String expected = "for CharacterReader class.";
 String actual = reader.matchConsumeIgnoreCase("T");
 assertEquals(expected, reader.input().substring(0, reader.pos()));
 }

 // Testing matchConsumeIgnoreCase() method with multiple characters
 @Test
 public void testMatchConsumeIgnoreCaseMultipleCharacters() {
 String expected = "for CharacterReader class.";
 String actual = reader.matchConsumeIgnoreCase("is");
 assertEquals(expected, reader.input().substring(0, reader.pos()));
 }

 // Testing containsIgnoreCase() method with single character
 @Test
 public void testContainsIgnoreCaseSingleCharacter() {
 assertTrue(reader.containsIgnoreCase("i"));
 reader.consume();
 assertFalse(reader.containsIgnoreCase("i"));
 }

 // Testing containsIgnoreCase() method with multiple characters
 @Test
 public void testContainsIgnoreCaseMultipleCharacters() {
 assertTrue(reader.containsIgnoreCase("is"));
 reader.advance();
 reader.advance();
 reader.consume();
 assertFalse(reader.containsIgnoreCase("is"));
 }
}