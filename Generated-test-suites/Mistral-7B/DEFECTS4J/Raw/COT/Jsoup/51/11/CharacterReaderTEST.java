package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 reader.consume();
 Assert.assertEquals('c', reader.current());
 reader.consume();
 Assert.assertEquals('d', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 Assert.assertEquals("a", reader.consumeAsString());
 Assert.assertEquals("bc", reader.consumeAsString(2));
 }

 @Test
 public void testNextIndexOf() {
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 Assert.assertEquals(1, reader.nextIndexOf('b'));
 Assert.assertEquals(5, reader.nextIndexOf('e'));
 }

 @Test
 public void testConsumeTo() {
 String expected = "abcd";
 Assert.assertEquals(expected, reader.consumeTo('d'));
 Assert.assertEquals("ef", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToSorted() {
 String expected = "bcd";
 Assert.assertEquals(expected, reader.consumeToAnySorted('b', 'c', 'd'));
 }

 @Test
 public void testConsumeData() {
 String expected = "ab&";
 Assert.assertEquals(expected, reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 String expected = "abcd";
 Assert.assertEquals(expected, reader.consumeTagName());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToEndWithEmptyInput() {
 reader = new CharacterReader("");
 reader.consumeToEnd();
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abcd";
 Assert.assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String expected = "ab12cd";
 Assert.assertEquals(expected, reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "abcd12ef";
 Assert.assertEquals(expected, reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "12345";
 Assert.assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matches("pq"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("pq"));
 }

 @Test
 public void testMatchesAny() {
 char[] expected = new char[]{'a', 'b', 'c'};
 Assert.assertArrayEquals(expected, reader.matchesAny(new char[]{'a', 'b', 'c'}));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] expected = new char[]{'b', 'c', 'd'};
 Assert.assertArrayEquals(expected, reader.matchesAnySorted(new char[]{'b', 'c', 'd'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 String expected = "abcd";
 reader.matchConsume("abcd");
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String expected = "abcd";
 reader.matchConsumeIgnoreCase("AbCd");
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("cd"));
 Assert.assertFalse(reader.containsIgnoreCase("pq"));
 }

 @Test
 public void testToString() {
 String expected = "abcd";
 Assert.assertEquals(expected, reader.toString());
 }
}