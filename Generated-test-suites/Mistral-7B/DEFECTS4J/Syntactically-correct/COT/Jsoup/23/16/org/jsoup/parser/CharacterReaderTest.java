package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

 private static final String TEST_STRING = "This is a test string with special characters: !@#$%^&*()_+-={[}]]|\\;:'\",.<>/?";

 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals("This is a test string with special characters: !@#$%^&*()_+-={[}]]|\\;:'\",.<>/?", reader.input);
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(TEST_STRING.charAt(0), reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 assertEquals(TEST_STRING.charAt(0), reader.current());
 assertEquals(1, reader.pos());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEnd() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.unconsume();
 assertEquals(TEST_STRING.charAt(0), reader.current());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.advance();
 assertEquals(TEST_STRING.charAt(1), reader.current());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals(TEST_STRING.charAt(0), reader.current());
 assertEquals(0, reader.pos());
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals("", reader.consumeAsString());
 reader.consume("ab");
 assertEquals("ab", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals("", reader.consumeTo('a'));
 reader.consume("abc");
 reader.consumeTo('c');
 assertEquals("bc", reader.consumeAsString());
 }

 @Test
 public void testConsumeToWithEnd() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consumeToEnd();
 assertEquals(TEST_STRING, reader.consumeAsString());
 }

 @Test
 public void testConsumeToWithSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume("ab");
 reader.consumeTo("cd");
 assertEquals("", reader.consumeAsString());
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consumeToAny('a', 'b');
 assertEquals("", reader.consumeAsString());
 reader.consume("abcd");
 reader.consumeToAny('d', 'e');
 assertEquals("cd", reader.consumeAsString());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals("", reader.consumeLetterSequence());
 reader.consume("abcd");
 assertEquals("cd", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume("0x12");
 assertEquals("12", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume("1234");
 assertEquals("4", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matches('T'));
 assertFalse(reader.matches('Z'));
 reader.consume("This is a test string");
 assertTrue(reader.matches('s'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matches("is"));
 assertFalse(reader.matches("is not"));
 reader.consume("This is a test string");
 assertTrue(reader.matches("a test string"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesIgnoreCase("is"));
 assertFalse(reader.matchesIgnoreCase("is not"));
 reader.consume("This is a test string");
 assertTrue(reader.matchesIgnoreCase("a test string"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume("This is a test string");
 assertTrue(reader.matchesAny('a', 'e'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 reader.consume("This is a test string");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertFalse(reader.matchesDigit());
 reader.consume("12345");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume("The quick brown fox jumps over the lazy dog");
 assertTrue(reader.matchConsume("the"));
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume("The quick brown fox jumps over the lazy dog");
 assertTrue(reader.matchConsumeIgnoreCase("THE"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.containsIgnoreCase("lazy dog"));
 }

}