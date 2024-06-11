package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', ' '};
 characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consumeToEnd();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consumeToEnd();
 characterReader.consume();
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.consume();
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String result = characterReader.consumeAsString();
 Assert.assertEquals("is", result);
 characterReader.consume();
 result = characterReader.consumeAsString();
 Assert.assertEquals("a", result);
 }

 @Test
 public void testConsumeTo() {
 String result = characterReader.consumeTo('a');
 Assert.assertEquals("is a", result);
 characterReader.consume();
 result = characterReader.consumeTo('c');
 Assert.assertEquals("is a t C", result);
 }

 @Test
 public void testConsumeToEnd() {
 String result = characterReader.consumeToEnd();
 Assert.assertEquals("test CharacterReader", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = characterReader.consumeLetterSequence();
 Assert.assertEquals("is", result);
 characterReader.consume();
 result = characterReader.consumeLetterSequence();
 Assert.assertEquals("a", result);
 }

 @Test
 public void testConsumeHexSequence() {
 String result = characterReader.consumeHexSequence();
 Assert.assertEquals("Ch", result);
 characterReader.consume();
 result = characterReader.consumeHexSequence();
 Assert.assertEquals("ar", result);
 characterReader.consume();
 result = characterReader.consumeHexSequence();
 Assert.assertEquals("ct", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = characterReader.consumeDigitSequence();
 Assert.assertEquals("1", result);
 characterReader.consume();
 result = characterReader.consumeDigitSequence();
 Assert.assertEquals("2", result);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('t'));
 characterReader.consume();
 Assert.assertFalse(characterReader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(characterReader.matches("is"));
 characterReader.consumeTo(' ');
 characterReader.consume();
 Assert.assertFalse(characterReader.matches("is"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("is"));
 characterReader.consumeTo(' ');
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesIgnoreCase("is"));
 }

 @Test
 public void testMatchesAny() {
 characterReader.consumeTo(' ');
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesAny('a', 's'));
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesAny('t', 'c'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String result = characterReader.matchConsume("is");
 Assert.assertEquals("is", result);
 characterReader.consume();
 result = characterReader.matchConsume("a");
 Assert.assertEquals("a", result);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String result = characterReader.matchConsumeIgnoreCase("is");
 Assert.assertEquals("is", result);
 characterReader.consumeTo(' ');
 characterReader.consume();
 result = characterReader.matchConsumeIgnoreCase("is");
 Assert.assertEquals("", result);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("is"));
 characterReader.consumeTo(' ');
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.containsIgnoreCase("is"));
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 Assert.assertFalse(characterReader.containsIgnoreCase("is"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 new CharacterReader(null);
 }

}