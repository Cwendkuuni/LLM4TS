package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @SuppressWarnings("static-access")
 @Before
 public void setUp() {
 char[] input = "abcdef123ABCDEF".toCharArray();
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 reader.consume();
 // Test EOF
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "ab";
 String actual = reader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfNegativeChar() {
 reader.nextIndexOf('z');
 }

 @Test
 public void testNextIndexOfChar() {
 int index = reader.nextIndexOf('d');
 Assert.assertEquals(3, index);
 }

 @Test
 public void testNextIndexOfCharSequence() {
 String seq = "ef";
 int index = reader.nextIndexOf(seq);
 Assert.assertEquals(4, index);
 }

 @Test
 public void testConsumeToChar() {
 char target = 'd';
 String expected = "cd";
 String actual = reader.consumeTo(target);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToString() {
 String target = "ef";
 String expected = "cdef";
 String actual = reader.consumeTo(target);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 char[] targets = {'1', '2'};
 String expected = "1";
 String actual = reader.consumeToAny(targets);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "def123ABCDEF";
 String actual = reader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abc";
 String actual = reader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String expected = "ab1";
 String actual = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "abcdef";
 String actual = reader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String actual = reader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 char target = 'd';
 Assert.assertTrue(reader.matches(target));
 reader.consume();
 Assert.assertFalse(reader.matches(target));
 }

 @Test
 public void testMatchesString() {
 String target = "ef";
 Assert.assertTrue(reader.matches(target));
 reader.consumeTo(target);
 Assert.assertFalse(reader.matches(target));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String target = "ef";
 Assert.assertTrue(reader.matchesIgnoreCase(target));
 }

 @Test
 public void testMatchesAny() {
 char[] targets = {'1', '2'};
 Assert.assertTrue(reader.matchesAny(targets));
 reader.consume();
 Assert.assertFalse(reader.matchesAny(targets));
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
 reader.consumeLetterSequence();
 reader.consumeDigitSequence();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 String target = "ef";
 reader.matchConsume(target);
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String target = "EF";
 reader.matchConsumeIgnoreCase(target);
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testContainsIgnoreCaseString() {
 String target = "ef";
 Assert.assertTrue(reader.containsIgnoreCase(target));
 }

 @Test
 public void testToString() {
 String expected = "def";
 String actual = reader.toString();
 Assert.assertEquals(expected, actual);
 }
}