package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 // Test isEmpty method
 Assert.assertTrue(reader.isEmpty());
 // Test current and consume methods
 Assert.assertEquals(-1, reader.current());
 Assert.assertEquals(-1, reader.consume());
 }

 @Test
 public void testReaderWithValidInput() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 // Test pos and isEmpty methods
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 // Test current and consume methods
 Assert.assertEquals('H', reader.current());
 Assert.assertEquals('H', reader.consume());
 Assert.assertEquals('e', reader.current());
 // Test unconsume method
 reader.unconsume();
 Assert.assertEquals('H', reader.current());
 // Test advance method
 reader.advance();
 Assert.assertEquals('e', reader.current());
 // Test mark and rewindToMark methods
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 // Test consumeAsString and consumeTo methods
 String expectedString = reader.consumeAsString();
 Assert.assertEquals("Hello", expectedString);
 Assert.assertEquals("l", reader.consumeTo('l'));
 // Test consumeToEnd method
 String expectedEndString = reader.consumeToEnd();
 Assert.assertEquals("World", expectedEndString);
 }

 @Test
 public void testReaderWithInvalidInput() {
 CharacterReader reader = new CharacterReader("HelloWorld<script>alert('XSS')</script>");
 // Test matches and matchConsume methods with valid characters
 Assert.assertTrue(reader.matches('H'));
 Assert.assertTrue(reader.matchesIgnoreCase("ello"));
 Assert.assertTrue(reader.matchesAny(new char[]{'H', 'e'}));
 // Test matches and matchConsume methods with invalid characters
 Assert.assertFalse(reader.matches('<'));
 Assert.assertFalse(reader.matchesIgnoreCase("script"));
 Assert.assertFalse(reader.matchesAny(new char[]{'<', 's'}));
 // Test consumeToEnd method with script tag
 String expectedScriptString = reader.consumeTo("<script");
 Assert.assertEquals("HelloWorld", expectedScriptString);
 // Test consumeToEnd method with invalid character
 String expectedInvalidCharString = reader.consumeTo('Z');
 Assert.assertEquals("HelloWorld<script>alert('XSS')</script>", expectedInvalidCharString);
 }
}