package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest class to test the functionality of CharacterReader class.
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");
 // Test isEmpty method
 Assert.assertTrue(reader.isEmpty());
 // Test current(), consume(), and pos() methods with empty reader
 Assert.assertEquals(-1, reader.current());
 Assert.assertEquals(-1, reader.consume());
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testCharacterReaderWithValidInput() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 // Test pos(), isEmpty(), and current() methods with valid reader
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('H', reader.current());
 // Test consume() method
 Assert.assertEquals('H', reader.consume());
 Assert.assertEquals(1, reader.pos());
 // Test consumeAsString() and consumeTo() methods
 Assert.assertEquals("H", reader.consumeAsString());
 Assert.assertEquals("ello", reader.consumeTo('l'));
 // Test consumeToEnd() method
 Assert.assertEquals("World", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderWithMixedInput() {
 CharacterReader reader = new CharacterReader("123<abc>456<def>789");
 // Test consumeLetterSequence() method
 Assert.assertEquals("abc", reader.consumeLetterSequence());
 // Test consumeHexSequence() and consumeDigitSequence() methods
 Assert.assertEquals("def", reader.consumeHexSequence());
 Assert.assertEquals("89", reader.consumeDigitSequence());
 // Test matches() and matchesAny() methods
 Assert.assertTrue(reader.matches('<'));
 Assert.assertTrue(reader.matches("def"));
 Assert.assertTrue(reader.matchesAny(new char[]{'1', '2'}));
 // Test matchesIgnoreCase() method
 Assert.assertTrue(reader.matchesIgnoreCase("def"));
 // Test containsIgnoreCase() method
 Assert.assertTrue(reader.containsIgnoreCase("<ABC>"));
 }

 @Test(expected = NullPointerException.class)
 public void testCharacterReaderWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 // This test should throw NullPointerException
 }

 // Add more test cases as necessary

}