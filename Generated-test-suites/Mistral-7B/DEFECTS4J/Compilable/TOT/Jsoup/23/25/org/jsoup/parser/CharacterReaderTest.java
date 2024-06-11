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
 // empty reader should report isEmpty as true and current as EOF
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testSimpleConsume() {
 CharacterReader reader = new CharacterReader("hello world");
 // consume should advance position and return current character
 reader.advance();
 reader.advance();
 reader.consume();
 Assert.assertEquals('l', reader.current());
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("hello world");
 // consume string should consume given sequence and advance position
 reader.consumeTo("ll");
 Assert.assertEquals("ll", reader.consumeAsString());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("hello world");
 // consume to end should consume remaining string and set position to end
 String expected = reader.consumeToEnd();
 reader.advance();
 Assert.assertEquals(expected, reader.consumeAsString());
 }

 @Test
 public void testConsumeToAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("hxeef world");
 // consume to any character should consume given character and advance position
 reader.consumeToAny('e');
 Assert.assertEquals("h", reader.consumeAsString());
 }

 @Test
 public void testConsumeToAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("hxeef world");
 // consume to any characters should consume given sequence and advance position
 reader.consumeToAny('e', 'f');
 Assert.assertEquals("he", reader.consumeAsString());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 CharacterReader reader = new CharacterReader("hXeef world");
 // consume to ignore case should consume given sequence and advance position
 reader.consumeTo("EL");
 Assert.assertEquals("Xe", reader.consumeAsString());
 }

 @Test(expected = AssertionError.class)
 public void testConsumePastEndOfString() {
 CharacterReader reader = new CharacterReader("hello world");
 // consume past end of string should throw AssertionError
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 }

 @Test(expected = AssertionError.class)
 public void testConsumeInvalidSequence() {
 CharacterReader reader = new CharacterReader("hxeef world");
 // consume invalid sequence should throw AssertionError
 reader.consumeTo("XX");
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("hxeef world");
 // mark position and rewind to mark
 reader.mark();
 reader.advance();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('h', reader.current());
 }
}