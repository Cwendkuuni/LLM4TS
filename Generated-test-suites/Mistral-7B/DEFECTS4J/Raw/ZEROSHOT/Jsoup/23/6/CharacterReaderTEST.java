package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 'st', ' ', 'i', 'n', 'p', 'u', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r', '.', ' '};
 reader = new CharacterReader(new String(testInput));
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
 char expected = 't';
 char actual;

 actual = reader.current();
 Assert.assertEquals(expected, actual);
 reader.consume();
 expected = 'e';
 actual = reader.current();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsume() {
 char expected;
 char actual;

 expected = 't';
 actual = reader.consume();
 Assert.assertEquals(expected, actual);
 expected = 'e';
 actual = reader.consume();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 char expected = 't';
 char actual = reader.current();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAdvance() {
 reader.advance();
 char expected = 'i';
 char actual = reader.current();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMark() {
 int mark = reader.pos();
 reader.mark();
 int newMark = reader.pos();
 Assert.assertEquals(mark, newMark);
 reader.rewindToMark();
 char expected = 't';
 char actual = reader.current();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeAsString() {
 String expected = "st ";
 String actual = reader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "in";
 String actual = reader.consumeTo('p');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToString() {
 String expected = "Ch a r c t e r ";
 String actual = reader.consumeTo(' ');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 char[] sequence = {'C', 'h', 'a'};
 String expected = "Ch a";
 String actual = reader.consumeToAny(sequence);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "Reader .";
 String actual = reader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "Character";
 String actual = reader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "Reader";
 String actual = reader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "1";
 String actual = reader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 char expected = 'C';
 char actual = reader.current();
 Assert.assertTrue(reader.matches(expected));
 actual = reader.consume();
 Assert.assertFalse(reader.matches(expected));
 }

 @Test
 public void testMatchesString() {
 String expected = "Reader";
 String actual = reader.consumeToEnd();
 Assert.assertTrue(reader.matches(expected));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String expected = "reader";
 boolean actual = reader.matchesIgnoreCase(expected);
 Assert.assertTrue(actual);
 }

 @Test
 public void testMatchesAny() {
 char[] sequence = {'C', 'h', 'a'};
 char expected = 'a';
 char actual = reader.current();
 Assert.assertTrue(reader.matchesAny(sequence));
 actual = reader.consume();
 Assert.assertFalse(reader.matchesAny(sequence));
 }

 @Test
 public void testMatchesLetter() {
 char expected = 'h';
 boolean actual = reader.matchesLetter();
 Assert.assertTrue(actual);
 }

 @Test
 public void testMatchesDigit() {
 char expected = '1';
 boolean actual = reader.matchesDigit();
 Assert.assertFalse(actual);
 }

 @Test
 public void testMatchConsume() {
 String expected = "Reader.";
 String actual = reader.consumeToEnd();
 Assert.assertTrue(reader.matchConsume(expected));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "reader.";
 Assert.assertTrue(reader.containsIgnoreCase(expected));
 }

 @Test
 public void testToString() {
 String expected = "Reader .";
 String actual = reader.toString();
 Assert.assertEquals(expected, actual);
 }
}