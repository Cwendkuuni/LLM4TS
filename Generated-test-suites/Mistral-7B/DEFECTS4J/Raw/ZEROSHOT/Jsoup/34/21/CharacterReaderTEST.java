package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader characterReader = new CharacterReader("");

 Assert.assertTrue(characterReader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 @Test
 public void testCharacterReaderSingleCharacter() {
 CharacterReader characterReader = new CharacterReader("hello");

 characterReader.consume();
 Assert.assertEquals('h', characterReader.current());
 Assert.assertEquals(4, characterReader.pos());
 }

 @Test
 public void testCharacterReaderMultipleCharacters() {
 CharacterReader characterReader = new CharacterReader("hello");

 characterReader.consume();
 characterReader.consume();

 Assert.assertEquals('l', characterReader.current());
 Assert.assertEquals(5, characterReader.pos());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader characterReader = new CharacterReader("hello");

 characterReader.consume();
 characterReader.consume();
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();

 Assert.assertEquals('l', characterReader.current());
 Assert.assertEquals(5, characterReader.pos());
 }

 @Test
 public void testCharacterReaderNextIndexOfChar() {
 CharacterReader characterReader = new CharacterReader("hello");

 Assert.assertEquals(1, characterReader.nextIndexOf('l'));
 Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfString() {
 CharacterReader characterReader = new CharacterReader("hello world");

 Assert.assertEquals(5, characterReader.nextIndexOf(" world"));
 Assert.assertEquals(-1, characterReader.nextIndexOf("xxx"));
 }

 @Test
 public void testCharacterReaderConsumeToChar() {
 CharacterReader characterReader = new CharacterReader("hello");

 String consumed = characterReader.consumeTo('l');
 Assert.assertEquals("he", consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderConsumeToCharNegativeIndex() {
 CharacterReader characterReader = new CharacterReader("hello");

 characterReader.consumeTo(-1);
 }

 @Test
 public void testCharacterReaderConsumeToString() {
 CharacterReader characterReader = new CharacterReader("hello");

 String consumed = characterReader.consumeTo("ll");
 Assert.assertEquals("hell", consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderConsumeToStringNegativeIndex() {
 CharacterReader characterReader = new CharacterReader("hello");

 characterReader.consumeTo("-1");
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader characterReader = new CharacterReader("12345hello12345");

 String consumed = characterReader.consumeToAny(new char[]{'h', 'e', 'l', 'l'});
 Assert.assertEquals("12345", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader characterReader = new CharacterReader("hello");

 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("hello", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader characterReader = new CharacterReader("hello");

 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("he", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader characterReader = new CharacterReader("hello12345");

 String consumed = characterReader.consumeLetterThenDigitSequence();
 Assert.assertEquals("he12345", consumed);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader characterReader = new CharacterReader("&amp;#x1f44d;");

 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("#x1f44d", consumed);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader characterReader = new CharacterReader("12345");

 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("12345", consumed);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader characterReader = new CharacterReader("hello");

 Assert.assertTrue(characterReader.matches('l'));
 Assert.assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testCharacterReaderMatchesString() {
 CharacterReader characterReader = new CharacterReader("hello world");

 Assert.assertTrue(characterReader.matches("ll"));
 Assert.assertFalse(characterReader.matches("xx"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCaseString() {
 CharacterReader characterReader = new CharacterReader("Hello World");

 Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("XX"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader characterReader = new CharacterReader("hello");

 Assert.assertTrue(characterReader.matchesAny(new char[]{'h', 'e'}));
 Assert.assertFalse(characterReader.matchesAny(new char[]{'z', 'z'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader characterReader = new CharacterReader("hello");

 Assert.assertTrue(characterReader.matchesLetter());
 Assert.assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader characterReader = new CharacterReader("12345");

 Assert.assertFalse(characterReader.matchesLetter());
 Assert.assertTrue(characterReader.matchesDigit());
 }
}