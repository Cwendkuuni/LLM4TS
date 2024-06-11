package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] inputChars = new char[]{'a', 'b', 'c', 'd', 'e'};
 String input = new String(inputChars);
 this.characterReader = new CharacterReader(input);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consume();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 characterReader.advance();
 characterReader.advance();
 Assert.assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 characterReader.consume();
 Assert.assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('a', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String result = characterReader.consumeAsString();
 Assert.assertEquals("ab", result);
 }

 @Test
 public void testNextIndexOf() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(1, characterReader.nextIndexOf('c'));
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(-1, characterReader.nextIndexOf('c'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String charSequence = "cd";
 Assert.assertEquals(2, characterReader.nextIndexOf(charSequence));
 }

 @Test(expected = AssertionError.class)
 public void testNextIndexOfWithNonExistentCharSequence() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String charSequence = "ef";
 Assert.assertEquals(-1, characterReader.nextIndexOf(charSequence));
 }

 @Test
 public void testConsumeTo() {
 String expected = "bc";
 String result = characterReader.consumeTo('c');
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "ade";
 String result = characterReader.consumeToEnd();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeToAny() {
 characterReader.consume();
 characterReader.consume();
 String result = characterReader.consumeToAny('d');
 Assert.assertEquals("bc", result);
 }

 @Test
 public void testConsumeToAnySorted() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String result = characterReader.consumeToAnySorted(new char[]{'d', 'c'});
 Assert.assertEquals("bcd", result);
 }

 @Test
 public void testConsumeData() {
 String expected = "ab&";
 String result = characterReader.consumeData();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeTagName() {
 String expected = "ab\tc";
 String result = characterReader.consumeTagName();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeToAnyWithEmptyInput() {
 String result = characterReader.consumeToAny(new char[0]);
 Assert.assertEquals("", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abc";
 String result = characterReader.consumeLetterSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String expected = "ab12cd";
 String result = characterReader.consumeLetterThenDigitSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "ab123e";
 String result = characterReader.consumeHexSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String result = characterReader.consumeDigitSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('b'));
 characterReader.consume();
 Assert.assertFalse(characterReader.matches('b'));
 }

 @Test
 public void testMatchesWithString() {
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.matches("ab"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader.consume();
 characterReader.consume();
 String seq = "Ab";
 Assert.assertTrue(characterReader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 characterReader.consume();
 characterReader.consume();
 char[] chars = new char[]{'c', 'b'};
 Assert.assertTrue(characterReader.matchesAny(chars));
 }

 @Test
 public void testMatchesAnySorted() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 char[] chars = new char[]{'d', 'c'};
 Assert.assertTrue(characterReader.matchesAnySorted(chars));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 characterReader.consume();
 characterReader.consume();
 String expected = "bc";
 Assert.assertTrue(characterReader.matchConsume("bc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 characterReader.consume();
 characterReader.consume();
 String expected = "bc";
 Assert.assertTrue(characterReader.matchConsumeIgnoreCase("BC"));
 }

 @Test
 public void testContainsIgnoreCase() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String seq = "cd";
 Assert.assertTrue(characterReader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 String expected = "de";
 String result = characterReader.toString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCacheString() {
 characterReader.consume();
 characterReader.consume();
 int start = 0;
 int count = 2;
 String result = characterReader.cacheString(start, count);
 String expected = "ab";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testRangeEquals() {
 characterReader.consume();
 characterReader.consume();
 char[] inputChars = new char[]{'a', 'b'};
 String cached = new String(inputChars);
 int start = 0;
 int count = 2;
 Assert.assertTrue(characterReader.rangeEquals(start, count, cached));
 }
}