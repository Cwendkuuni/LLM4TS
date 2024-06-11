package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setup() {
 char[] testInput = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
 characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader("testString");
 Assert.assertNotNull(characterReader);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.consume();
 Assert.assertEquals(1, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('a', characterReader.consume());
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 Assert.assertEquals('b', characterReader.current());
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
 characterReader.consume();
 result = characterReader.consumeAsString();
 Assert.assertEquals("cd", result);
 }

 @Test
 public void testConsumeTo() {
 String result = characterReader.consumeTo('c');
 Assert.assertEquals("a", result);
 result = characterReader.consumeTo('e');
 Assert.assertEquals("ade", result);
 }

 @Test
 public void testConsumeToEnd() {
 String result = characterReader.consumeToEnd();
 Assert.assertEquals("gheid", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = characterReader.consumeLetterSequence();
 Assert.assertEquals("ade", result);
 }

 @Test
 public void testConsumeHexSequence() {
 String result = characterReader.consumeHexSequence();
 Assert.assertEquals("23", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = characterReader.consumeDigitSequence();
 Assert.assertEquals("123", result);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('a'));
 Assert.assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(characterReader.matches("ab"));
 Assert.assertFalse(characterReader.matches("ef"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("ab"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("ef"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader characterReader1 = new CharacterReader("testString123");
 Assert.assertTrue(characterReader1.matchesAny('1', '2', '3'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 Assert.assertFalse(characterReader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(characterReader.matchesDigit());
 Assert.assertFalse(characterReader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 String result = characterReader.matchConsume("ab");
 Assert.assertEquals("ab", result);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String result = characterReader.matchConsumeIgnoreCase("Ab");
 Assert.assertEquals("ab", result);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("ab"));
 Assert.assertFalse(characterReader.containsIgnoreCase("ef"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("de", characterReader.toString());
 }

}