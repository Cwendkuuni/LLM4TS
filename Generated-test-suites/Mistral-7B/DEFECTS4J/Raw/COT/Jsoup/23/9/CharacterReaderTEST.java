package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 'n', 'p', 'u', 't', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', '\n'};
 String testString = new String(testInput);
 reader = new CharacterReader(testString);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 // consume first two characters
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 char current = reader.current();
 Assert.assertEquals('t', current);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // consume 6 characters, causing empty state
 reader.consume(); // should throw exception
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume(); // consume first two characters
 reader.unconsume();
 reader.unconsume(); // move back to the start
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
 String consumed = reader.consumeAsString();
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('n');
 Assert.assertEquals("p", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("character\nreader", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("12345", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("is"));
 Assert.assertFalse(reader.matches("in"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("is"));
 Assert.assertTrue(reader.matchesIgnoreCase("Is"));
 Assert.assertFalse(reader.matchesIgnoreCase("in"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny(new char[]{'t', 'e'}));
 Assert.assertFalse(reader.matchesAny(new char[]{'a', 'b'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter()); // consume a character
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit()); // consume a character
 }

 @Test
 public void testMatchConsume() {
 String consumed = reader.matchConsume("is");
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase("is");
 Assert.assertEquals("is", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("is"));
 Assert.assertTrue(reader.containsIgnoreCase("IS"));
 Assert.assertFalse(reader.containsIgnoreCase("in"));
 }

}