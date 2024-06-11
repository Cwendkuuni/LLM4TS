package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertEquals("Hello World!", reader.input);
 Assert.assertEquals(11, reader.length);
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());

 reader = new CharacterReader("Hello World!");
 Assert.assertFalse(reader.isEmpty());
 }

 // Test current and consume methods with normal characters
 @Test
 public void testCurrentAndConsumeNormalChars() {
 CharacterReader reader = new CharacterReader("abcdefg");
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 }

 // Test current and consume methods with EOF
 @Test
 public void testCurrentAndConsumeEOF() {
 CharacterReader reader = new CharacterReader("");
 reader.consume();
 Assert.assertEquals(EOF, reader.current());
 Assert.assertEquals(EOF, reader.consume());
 }

 // Test unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 // Test advance method
 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 // Test mark and rewindToMark methods
 @Test
 public void testMarkRewindToMark() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 // Test consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcdefg");
 Assert.assertEquals("defg", reader.consumeAsString());
 }

 // Test consumeTo method with single character
 @Test
 public void testConsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("abcdefg");
 Assert.assertEquals("efg", reader.consumeTo('g'));
 }

 // Test consumeTo method with multiple characters
 @Test
 public void testConsumeToMultipleChars() {
 CharacterReader reader = new CharacterReader("abcdefg");
 Assert.assertEquals("ef", reader.consumeTo("fg"));
 }

 // Test consumeTo method with EOF
 @Test
 public void testConsumeToEOF() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals("", reader.consumeTo(EOF));
 }

 // Test consumeToAny method
 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 Assert.assertEquals("abcd", reader.consumeToAny('a', 'd'));
 }

 // Test consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals("abcd", reader.consumeToEnd());
 }

 // Test consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World!");
 Assert.assertEquals("World", reader.consumeLetterSequence());
 }

 // Test consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("1a2b3c");
 Assert.assertEquals("3c", reader.consumeHexSequence());
 }

 // Test consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 Assert.assertEquals("5", reader.consumeDigitSequence());
 }

 // Test matches method with single character
 @Test
 public void testMatchesSingleChar() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertTrue(reader.matches('b'));
 Assert.assertFalse(reader.matches('a'));
 }

 // Test matches method with multiple characters
 @Test
 public void testMatchesMultipleChars() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matches("cd"));
 }

 // Test matchesIgnoreCase method with single character
 @Test
 public void testMatchesIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("ABC");
 Assert.assertTrue(reader.matchesIgnoreCase("B"));
 Assert.assertFalse(reader.matchesIgnoreCase("A"));
 }

 // Test matchesIgnoreCase method with multiple characters
 @Test
 public void testMatchesIgnoreCaseMultipleChars() {
 CharacterReader reader = new CharacterReader("AbCdEf");
 Assert.assertTrue(reader.matchesIgnoreCase("ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("ef"));
 }

 // Test matchesAny method with single character
 @Test
 public void testMatchesAnySingleChar() {
 CharacterReader reader = new CharacterReader("ab");
 Assert.assertTrue(reader.matchesAny('a'));
 Assert.assertTrue(reader.matchesAny('b'));
 }

 // Test matchesAny method with multiple characters
 @Test
 public void testMatchesAnyMultipleChars() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
 Assert.assertFalse(reader.matchesAny('e'));
 }

 // Test containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("head"));
 Assert.assertFalse(reader.containsIgnoreCase("body"));
 }
}