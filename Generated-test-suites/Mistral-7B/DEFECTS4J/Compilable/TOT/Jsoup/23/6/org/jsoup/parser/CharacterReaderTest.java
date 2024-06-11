package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 reader = new CharacterReader("This is a test string for CharacterReader.");
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "This is a test";
 assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "for CharacterReader.";
 assertEquals(expected, reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "12345";
 reader.advance(); // consume 'T'
 assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test(expected = java.lang.IndexOutOfBoundsException.class)
 public void testConsumeBeyondEnd() {
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testMatches() {
 char c = 't';
 assertTrue(reader.matches(c));
 reader.consume();
 assertFalse(reader.matches(c));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "is a test";
 assertTrue(reader.matchesIgnoreCase(seq));
 reader.advance();
 reader.advance(); // consume 'i' and 's'
 assertFalse(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'a', 'e'};
 assertTrue(reader.matchesAny(seq));
 reader.consume();
 assertFalse(reader.matchesAny(seq));
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "<title>";
 assertTrue(reader.containsIgnoreCase(seq));
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 assertFalse(reader.containsIgnoreCase(seq));
 }
}