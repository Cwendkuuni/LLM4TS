package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

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
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcd");
 String consumed = reader.consumeTo('d');
 Assert.assertEquals("bc", consumed);
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 String consumed = reader.consumeTo("ab");
 Assert.assertEquals("cdcd", consumed);
 }

 @Test
 public void testConsumeAny() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 String consumed = reader.consumeToAny('A', 'B', 'C');
 Assert.assertTrue(consumed.isEmpty());
 consumed = reader.consumeToAnySorted(new char[]{'B', 'C'});
 Assert.assertEquals("CD", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<null");
 String consumed = reader.consumeData();
 Assert.assertEquals("&<null", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>My Title</title>");
 String consumed = reader.consumeTagName();
 Assert.assertEquals("title", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeTo('e');
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd1234efghijk");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd1234efghijk");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abcd1234", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&x0F&x23&x7F&x01");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("xF27F", consumed);
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
 public void testMatchesSequence() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matches("cd"));
 Assert.assertFalse(reader.matches("ab"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesIgnoreCase("cd"));
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("ab"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 Assert.assertTrue(reader.matchesAny('A', 'B', 'C'));
 Assert.assertFalse(reader.matchesAny('X'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'B', 'C'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'A', 'X'}));
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
 public void testMatchConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 String consumed = reader.consumeTo("ab");
 Assert.assertTrue(reader.matchConsume("ab"));
 }

 @Test
 public void testMatchConsumeIgnoreCaseSequence() {
 CharacterReader reader = new CharacterReader("ABCDabcd");
 String consumed = reader.consumeTo("ab");
 Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>...</html>");
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
 }
}