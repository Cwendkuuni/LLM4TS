package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test constructor
 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertNotNull(reader);
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.isEmpty());
 reader.advance();
 Assert.assertTrue(reader.isEmpty());
 reader.rewindToMark();
 Assert.assertFalse(reader.isEmpty());
 }

 // Test pos and current methods
 @Test
 public void testPosAndCurrent() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals('T', reader.current());
 Assert.assertEquals(0, reader.pos());
 reader.advance();
 Assert.assertEquals('h', reader.current());
 Assert.assertEquals(1, reader.pos());
 reader.unconsume();
 Assert.assertEquals('h', reader.current());
 Assert.assertEquals(1, reader.pos());
 }

 // Test consume method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals('T', reader.consume());
 Assert.assertEquals('h', reader.current());
 }

 // Test consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test", reader.consumeAsString());
 }

 // Test consumeTo method with single character
 @Test
 public void testConsumeToSingleCharacter() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test st", reader.consumeTo('r'));
 }

 // Test consumeTo method with multiple characters
 @Test
 public void testConsumeToMultipleCharacters() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test strin", reader.consumeTo("ing"));
 }

 // Test consumeTo method with invalid character
 @Test(expected = AssertionError.class)
 public void testConsumeToInvalidCharacter() {
 CharacterReader reader = new CharacterReader("This is a test string");
 reader.consumeTo('X');
 }

 // Test consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("string", reader.consumeToEnd());
 }

 // Test consumeToAny method with single character
 @Test
 public void testConsumeToAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test st", reader.consumeToAny('t'));
 }

 // Test consumeToAny method with multiple characters
 @Test
 public void testConsumeToAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("is a test string", reader.consumeToAny('i', 'g'));
 }

 // Test matches method
 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('x'));
 }

 // Test matches method with multiple characters
 @Test
 public void testMatchesMultipleCharacters() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matches("is"));
 Assert.assertFalse(reader.matches("foo"));
 }

 // Test matchesIgnoreCase method
 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesIgnoreCase("is"));
 Assert.assertTrue(reader.matchesIgnoreCase("is a"));
 Assert.assertFalse(reader.matchesIgnoreCase("foo"));
 }

 // Test matchesAny method
 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesAny('t', 's'));
 Assert.assertFalse(reader.matchesAny('x'));
 }

 // Test matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchesLetter());
 reader.advance();
 Assert.assertTrue(reader.matchesLetter());
 reader.advance();
 Assert.assertFalse(reader.matchesLetter());
 }

 // Test matchesDigit method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertFalse(reader.matchesDigit());
 reader.advance();
 Assert.assertFalse(reader.matchesDigit());
 reader.advance();
 Assert.assertTrue(reader.matchesDigit());
 reader.advance();
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test matchConsume method
 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchConsume("is"));
 Assert.assertEquals("a test string", reader.toString());
 }

 // Test matchConsumeIgnoreCase method
 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertTrue(reader.matchConsumeIgnoreCase("is"));
 Assert.assertTrue(reader.matchConsumeIgnoreCase("IS"));
 Assert.assertEquals(" a test string", reader.toString());
 }

 // Test containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("This is a <title>test string</title>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
 Assert.assertFalse(reader.containsIgnoreCase("foo"));
 }

 // Test toString method
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("This is a test string");
 Assert.assertEquals("string", reader.consumeToEnd().trim());
 Assert.assertEquals("a test", reader.toString());
 }
}