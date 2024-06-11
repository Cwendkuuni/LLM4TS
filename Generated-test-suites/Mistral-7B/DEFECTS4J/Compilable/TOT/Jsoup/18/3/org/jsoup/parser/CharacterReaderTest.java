package org.jsoup.parser;
import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 // Test empty reader
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testBasicReading() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Test basic reading
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 reader.advance();
 Assert.assertEquals('b', reader.current());
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 // Test consuming a sequence of characters
 String seq = reader.consumeAsString();
 Assert.assertEquals("ab", seq);
 reader.advance();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Test consuming to end
 String end = reader.consumeToEnd();
 Assert.assertEquals("def", end);
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Test consuming to a specific character
 String expected = "abcd";
 String actual = reader.consumeTo('e');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Test consuming to a specific string
 String expected = "abc";
 String actual = reader.consumeTo("cd");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 // Test consuming to any of a set of characters
 String expected = "abcd";
 String actual = reader.consumeToAny('a', 'b', 'c', 'd');
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Test exception when consuming past end
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 }
}