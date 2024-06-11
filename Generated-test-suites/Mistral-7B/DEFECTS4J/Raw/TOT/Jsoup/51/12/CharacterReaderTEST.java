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
 public void testConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals('a', reader.consume());
 }

 @Test
 public void testConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals("ab", reader.consume(2));
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abc");
 String result = reader.consumeToEnd();
 Assert.assertEquals("abc", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("abca");
 Assert.assertEquals(1, reader.nextIndexOf('a'));
 }

 @Test
 public void testNextIndexOfMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abca");
 Assert.assertEquals(3, reader.nextIndexOf("bc"));
 }

 @Test
 public void testNextIndexOfEmptySequence() {
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertEquals(-1, reader.nextIndexOf(""));
 }

 @Test
 public void testConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("abca");
 Assert.assertEquals("ab", reader.consumeTo('b'));
 }

 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals("cd", reader.consumeTo("de").substring(0, 2));
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcd&efg");
 Assert.assertEquals("de&", reader.consumeToAny('&'));
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("abcd&efg");
 Assert.assertEquals("dfg", reader.consumeToAnySorted(new char[]{'d', 'f'}));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<abc");
 Assert.assertEquals("&<", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>Test</title>");
 Assert.assertEquals("<title>", reader.consumeTagName());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertEquals("abc", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("ab123cd");
 Assert.assertEquals("ab123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&\u0061\u0062");
 Assert.assertEquals("&\\u0061\\u0062", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("123");
 Assert.assertEquals("123", reader.consumeDigitSequence());
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
 Assert.assertTrue(reader.matchesAny('a', 'b'));
 Assert.assertTrue(reader.matchesAny('b', 'a'));
 Assert.assertFalse(reader.matchesAny('e'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'c', 'e'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('1'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("123");
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testCacheString() {
 CharacterReader reader = new CharacterReader("abcd");
 String result = reader.cacheString(0, 3);
 Assert.assertEquals("abc", result);
 }

 @Test
 public void testCacheStringCollision() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 String result1 = reader.cacheString(0, 3);
 String result2 = reader.cacheString(3, 3);
 Assert.assertEquals("abc", result1);
 Assert.assertEquals("abc", result2);
 }
}