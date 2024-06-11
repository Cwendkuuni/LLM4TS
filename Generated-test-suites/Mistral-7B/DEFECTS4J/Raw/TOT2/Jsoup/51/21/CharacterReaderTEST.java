package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 // Test for pos() method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals(0, reader.pos());
 }

 // Test for consume method with normal character
 @Test
 public void testConsumeNormalChar() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals(1, reader.pos());
 }

 // Test for consume method with EOF
 @Test
 public void testConsumeEndOfFile() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals('f', reader.consume());
 Assert.assertEquals(5, reader.pos());
 Assert.assertEquals(EOF, reader.consume());
 Assert.assertEquals(5, reader.pos());
 }

 // Test for unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consume();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('b', reader.current());
 }

 // Test for nextIndexOf method with single character
 @Test
 public void testNextIndexOfSingleChar() {
 CharacterReader reader = new CharacterReader("aaabbcdeeff");
 Assert.assertEquals(1, reader.nextIndexOf('a'));
 }

 // Test for nextIndexOf method with multiple occurrences
 @Test
 public void testNextIndexOfMultipleOccurrences() {
 CharacterReader reader = new CharacterReader("aaabbcdeeff");
 Assert.assertEquals(1, reader.nextIndexOf('a'));
 Assert.assertEquals(4, reader.nextIndexOf('a'));
 Assert.assertEquals(7, reader.nextIndexOf('a'));
 }

 // Test for nextIndexOf method with non-existent character
 @Test (expected = AssertionError.class)
 public void testNextIndexOfNonExistentChar() {
 CharacterReader reader = new CharacterReader("aaabbcdeeff");
 reader.nextIndexOf('z');
 }

 // Test for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeAsString();
 Assert.assertEquals("abc", consumed);
 Assert.assertEquals(3, reader.pos());
 }

 // Test for consumeTo method with single character
 @Test
 public void testConsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo('e');
 Assert.assertEquals("abcde", consumed);
 Assert.assertEquals(5, reader.pos());
 }

 // Test for consumeTo method with multiple characters
 @Test
 public void testConsumeToMultipleChars() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo("def");
 Assert.assertEquals("abcdef", consumed);
 Assert.assertEquals(6, reader.pos());
 }

 // Test for consumeTo method with non-existent sequence
 @Test (expected = AssertionError.class)
 public void testConsumeToNonExistentSeq() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeTo("ghi");
 }

 // Test for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abc123defG");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abc", consumed);
 Assert.assertEquals(3, reader.pos());
 }

 // Test for consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123defG");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abc123", consumed);
 Assert.assertEquals(6, reader.pos());
 }

 // Test for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a2b3c");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("1a2b3c", consumed);
 Assert.assertEquals(3, reader.pos());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345678");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("12345678", consumed);
 Assert.assertEquals(8, reader.pos());
 }

 // Test for matches method with single character
 @Test
 public void testMatchesSingleChar() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matches('e'));
 }

 // Test for matches method with multi-character string
 @Test
 public void testMatchesMultiCharString() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matches("ef"));
 }

 // Test for matchesIgnoreCase method with single character
 @Test
 public void testMatchesIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("abcDEF");
 Assert.assertTrue(reader.matchesIgnoreCase("e"));
 }

 // Test for matchesIgnoreCase method with multi-character string
 @Test
 public void testMatchesIgnoreCaseMultiCharString() {
 CharacterReader reader = new CharacterReader(" ABCDEFabcdef");
 Assert.assertTrue(reader.matchesIgnoreCase("EF"));
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 String consumed = reader.consumeTagName();
 Assert.assertEquals("html", consumed);
 Assert.assertEquals(6, reader.pos());
 }

 // Test for consumeData method
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&amp;<>&lt;<!DOCTYPE html>");
 String consumed = reader.consumeData();
 Assert.assertEquals("&amp;&lt;<!DOCTYPE html>", consumed);
 Assert.assertEquals(11, reader.pos());
 }

 // Test for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("abcdef", consumed);
 Assert.assertEquals(6, reader.pos());
 }
}