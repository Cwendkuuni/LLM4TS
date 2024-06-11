package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.consume());
 }

 @Test
 public void testConsumeEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals('e', reader.consumeToEnd().charAt(0));
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader("abcabc");
 String consumed = reader.consumeTo('c');
 Assert.assertEquals("bc", consumed);
 }

 @Test
 public void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("abcdabcabc");
 String consumed = reader.consumeTo("abc");
 Assert.assertEquals("abcdabca", reader.toString());
 Assert.assertEquals("abc", consumed);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String consumed = reader.consumeToAny('a', 'c');
 Assert.assertEquals("abd", consumed);
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String consumed = reader.consumeToAnySorted(new char[]{'b', 'd'});
 Assert.assertEquals("ac", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>Test</title>");
 String consumed = reader.consumeTagName();
 Assert.assertEquals("title", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&amp; <tag name=\"foo\" />");
 String consumed = reader.consumeData();
 Assert.assertEquals("&amp;", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMark() {
 CharacterReader reader = new CharacterReader("abc");
 reader.mark();
 reader.rewindToMark();
 reader.mark();
 reader.rewindToMark();
 reader.rewindToMark(); // should throw
 }
}