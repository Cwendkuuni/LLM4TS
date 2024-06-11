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
 public void testSingleCharacterReader() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals('a', reader.current());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 // the constructor should throw an IllegalArgumentException
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consume(); reader.consume(); reader.consume(); reader.consume(); reader.consume();
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark(); reader.consume(); reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeSubstring() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeTo("bc");
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeTo("cd");
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeCharacterArray() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeToAny(new char[]{'d', 'e'});
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeCharacterArraySorted() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeToAnySorted(new char[]{'a', 'b'});
 Assert.assertEquals("", consumed);
 consumed = reader.consumeToAnySorted(new char[]{'d', 'c'});
 Assert.assertEquals("cd", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<foo");
 String consumed = reader.consumeData();
 Assert.assertEquals("&<", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title></title>");
 String consumed = reader.consumeTagName();
 Assert.assertEquals("title", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("<title></title>");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("<title></title>", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abcd1234", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&x0F&x0A");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("xF, A", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matches('d'));
 Assert.assertFalse(reader.matches('e'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matches("d"));
 Assert.assertFalse(reader.matches("e"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesIgnoreCase("d"));
 Assert.assertTrue(reader.matchesIgnoreCase("D"));
 Assert.assertFalse(reader.matchesIgnoreCase("e"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesAny('d', 'e'));
 Assert.assertFalse(reader.matchesAny('f'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'c'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'f'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234");
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeTo("cd");
 Assert.assertEquals("abcd", consumed);
 Assert.assertTrue(reader.matchConsume("cd"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeTo("CD");
 Assert.assertEquals("abcd", consumed);
 Assert.assertTrue(reader.matchConsumeIgnoreCase("cd"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
 Assert.assertFalse(reader.containsIgnoreCase("header"));
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals("abcd", reader.toString());
 }
}