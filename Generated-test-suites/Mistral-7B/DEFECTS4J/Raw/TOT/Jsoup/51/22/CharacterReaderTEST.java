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
 CharacterReader reader = new CharacterReader("abcde");
 String consumed = reader.consumeTo('c');
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String consumed = reader.consumeTo("abcd");
 Assert.assertEquals("abcdabcdabcd", reader.toString());
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeAnyCharacter() {
 CharacterReader reader = new CharacterReader("123abcdef");
 String consumed = reader.consumeToAny('1', '2', '3');
 Assert.assertEquals("123", consumed);
 }

 @Test
 public void testConsumeAnyCharacterSorted() {
 CharacterReader reader = new CharacterReader("zabcd1234");
 String consumed = reader.consumeToAnySorted('1', '2', '3');
 Assert.assertEquals("123", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&lt;html&gt;");
 String consumed = reader.consumeData();
 Assert.assertEquals("&lt;html", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("&lt;title&gt;My Title&lt;/title&gt;");
 String consumed = reader.consumeTagName();
 Assert.assertEquals("title", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("abcdef", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcd", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("ab12cd34ef");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("ab12", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&x0A&x61&x62&x63");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("&x0A&x61&x62&x63", reader.toString());
 Assert.assertEquals("0A616263", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", consumed);
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matches('e'));
 Assert.assertFalse(reader.matches('f'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 Assert.assertTrue(reader.matchesIgnoreCase("ef"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("1234567890");
 Assert.assertTrue(reader.matchesAny('2', '5'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("zabcdef1234");
 Assert.assertTrue(reader.matchesAnySorted('1', '2', '3'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");
 Assert.assertTrue(reader.matchesDigit());
 }
}