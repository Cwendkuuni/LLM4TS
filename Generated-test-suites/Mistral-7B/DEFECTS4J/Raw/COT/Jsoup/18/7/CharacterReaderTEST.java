package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 char[] input = new char[]{'t', 'e', 's', 't', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', ':', ' ', '1', '2', '3'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
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
 Assert.assertEquals('t', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 Assert.assertEquals('t', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 reader.consumeToEnd();
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnconsumeFromEmpty() {
 reader.unconsume();
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = new String(new char[]{'t', 'e', 's'});
 String actual = reader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = new String(new char[]{'r', 'e', 'a', 'd'});
 String actual = reader.consumeTo('d');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = new String(new char[]{'r', 'e', 'a', 'd', ':', ' ', '1', '2', '3'});
 String actual = reader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 String expected = new String(new char[]{'a', 'r', 'e', 'a'});
 String actual = reader.consumeToAny('a', 'r', 'e');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = new String(new char[]{'t', 'e', 's', 't'});
 String actual = reader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = new String(new char[]{'1', '2'});
 String actual = reader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = new String(new char[]{'1', '2', '3'});
 String actual = reader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("tesT"));
 Assert.assertFalse(reader.matchesIgnoreCase("abcd"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('r', 'e', 'a'));
 Assert.assertFalse(reader.matchesAny('a', 'b', 'c'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 reader.consumeLetterSequence();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 reader.consumeDigitSequence();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatcheConsume() {
 String expected = new String(new char[]{'r', 'e', 'a', 'd'});
 reader.mark();
 reader.matchConsume(expected);
 Assert.assertEquals(expected.length(), reader.pos() - mark);
 }

 @Test
 public void testMatcheConsumeIgnoreCase() {
 String expected = new String(new char[]{'r', 'e', 'a', 'd'});
 reader.mark();
 reader.matchConsumeIgnoreCase(expected);
 Assert.assertEquals(expected.length(), reader.pos() - mark);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("read"));
 Assert.assertFalse(reader.containsIgnoreCase("title"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithMarkedPosition() {
 reader.mark();
 reader.rewindToMark();
 reader.consumeToEnd();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNegativePosition() {
 reader.consumeTo(-1);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConsumeToSequenceWithMarkedPosition() {
 char[] sequence = new char[3];
 reader.mark();
 reader.consumeToSequence(sequence);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 new CharacterReader(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSequence() {
 reader.consumeToAny(null);
 }
}