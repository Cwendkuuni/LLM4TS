package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test for the constructor method
 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader("Test String");
 Assert.assertNotNull(reader);
 }

 // Test for the isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("Test String");
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 // Test for the pos and length methods
 @Test
 public void testPosAndLength() {
 CharacterReader reader = new CharacterReader("Test String");
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals(11, reader.length());
 }

 // Test for the current method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertEquals('T', reader.current());
 reader.consume();
 Assert.assertEquals('S', reader.current());
 }

 // Test for the consume method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("TestString");
 reader.consume();
 reader.consume();
 Assert.assertEquals('S', reader.current());
 }

 // Test for the unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("TestString");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('T', reader.current());
 }

 // Test for the mark and rewindToMark methods
 @Test
 public void testMarkAndRewindToMark() {
 CharacterReader reader = new CharacterReader("TestString");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('T', reader.current());
 }

 // Test for the consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("TestString");
 String str = reader.consumeAsString();
 Assert.assertEquals("Test", str);
 }

 // Test for the nextIndexOf method with a single character
 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 int index = reader.nextIndexOf('t');
 Assert.assertEquals(4, index);
 }

 // Test for the nextIndexOf method with a multiple characters sequence
 @Test
 public void testNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 int index = reader.nextIndexOf("String");
 Assert.assertEquals(7, index);
 }

 // Test for the consumeTo method with a single character
 @Test
 public void testConsumeToSingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 String str = reader.consumeTo('t');
 Assert.assertEquals("Test", str);
 }

 // Test for the consumeTo method with a multiple characters sequence
 @Test
 public void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 String str = reader.consumeTo("String");
 Assert.assertEquals("String", str);
 }

 // Test for the consumeToAny method with a single character
 @Test
 public void testConsumeToAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 String str = reader.consumeToAny('t');
 Assert.assertEquals("Test", str);
 }

 // Test for the consumeToAny method with multiple characters
 @Test
 public void testConsumeToAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 String str = reader.consumeToAny('s', 't');
 Assert.assertEquals("StringTest", str);
 }

 // Test for the consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("TestString");
 String str = reader.consumeToEnd();
 Assert.assertEquals("TestString", str);
 }

 // Test for the consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("TestLetterSequence");
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("TestLetterSequence", str);
 }

 // Test for the consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Test1234LetterSequence");
 String str = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Test1234", str);
 }

 // Test for the consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("TestHexaSequence%");
 String str = reader.consumeHexSequence();
 Assert.assertEquals("TestHexaSequence%", str);
 }

 // Test for the consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("Test12345DigitSequence");
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("12345", str);
 }

 // Test for the matches method with a single character
 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matches('t'));
 }

 // Test for the matches method with a multiple characters sequence
 @Test
 public void testMatchesSequence() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 Assert.assertTrue(reader.matches("String"));
 }

 // Test for the matchesIgnoreCase method with a single character
 @Test
 public void testMatchesIgnoreCaseSingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matchesIgnoreCase("s"));
 }

 // Test for the matchesIgnoreCase method with a multiple characters sequence
 @Test
 public void testMatchesIgnoreCaseSequence() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 Assert.assertTrue(reader.matchesIgnoreCase("string"));
 }

 // Test for the matchesAny method with a single character
 @Test
 public void testMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matchesAny('t'));
 }

 // Test for the matchesAny method with multiple characters
 @Test
 public void testMatchesAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 Assert.assertTrue(reader.matchesAny('s', 't'));
 }

 // Test for the matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("TestLetterSequence");
 Assert.assertTrue(reader.matchesLetter());
 }

 // Test for the matchesDigit method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("Test1234DigitSequence");
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test for the matchConsume method with a single character
 @Test
 public void testMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("TestString");
 Assert.assertTrue(reader.matchConsume('t'));
 }

 // Test for the matchConsume method with a multiple characters sequence
 @Test
 public void testMatchConsumeSequence() {
 CharacterReader reader = new CharacterReader("TestStringTest");
 Assert.assertTrue(reader.matchConsume("String"));
 }

 // Test for the containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 }

 // Test for exception handling when input is null
 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderConstructionWithNullInput() {
 new CharacterReader(null);
 }
}