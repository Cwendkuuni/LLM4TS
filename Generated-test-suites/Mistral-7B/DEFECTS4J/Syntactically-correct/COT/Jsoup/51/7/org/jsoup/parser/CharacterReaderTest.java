package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import java.util.Arrays;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>\t\n\r\f / >".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testIsNotEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testConsumeString() {
 assertEquals("a", reader.consumeAsString());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testNextIndexOfCharacter() {
 assertEquals(6, reader.nextIndexOf('e'));
 }

 @Test
 public void testNextIndexOfCharSequence() {
 CharSequence seq = "abc";
 assertEquals(3, reader.nextIndexOf(seq));
 }

 @Test
 public void testConsumeToCharacter() {
 assertEquals("abc", reader.consumeTo('e'));
 assertEquals(6, reader.pos());
 }

 @Test
 public void testConsumeToCharSequence() {
 CharSequence seq = "abc";
 assertEquals("abc", reader.consumeTo(seq));
 assertEquals(6, reader.pos());
 }

 @Test
 public void testConsumeToAny() {
 char[] chars = {'a', 'b'};
 assertEquals("ab", reader.consumeToAny(chars));
 }

 @Test
 public void testConsumeToAnySorted() {
 char[] chars = {'z', 'a'};
 assertEquals("a", reader.consumeToAnySorted(chars));
 }

 @Test
 public void testConsumeData() {
 assertEquals("&<", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("/title", reader.consumeTagName());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("/title>\n<style>", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcdef", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("abc123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("abcd12ef", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123456", reader.consumeDigitSequence());
 }

 @Test
 public void testMatchesCharacter() {
 assertTrue(reader.matches('a'));
 }

 @Test
 public void testMatchesCharSequence() {
 CharSequence seq = "abc";
 assertTrue(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharSequence seq = "AbC";
 assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] chars = {'a', 'b'};
 assertTrue(reader.matchesAny(chars));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] chars = {'z', 'a'};
 assertTrue(reader.matchesAnySorted(chars));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharSequence seq = "abc";
 reader.matchConsume(seq);
 assertEquals(3, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharSequence seq = "AbC";
 reader.matchConsumeIgnoreCase(seq);
 assertEquals(3, reader.pos());
 }

 @Test(expected = IllegalStateException.class)
 public void testMarkAndRewindToMarkWithEmptyInput() {
 reader.mark();
 reader.rewindToMark();
 }

 @Test
 public void testMarkAndRewindToMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testStringCache() {
 // Check that cacheString is caching strings correctly
 String cachedString = reader.cacheString(0, 10);
 assertEquals("abcdefghij", cachedString);
 String sameString = reader.cacheString(0, 10);
 assertEquals(cachedString, sameString);
 // Check that cacheString is able to handle collisions
 String collidingString1 = reader.cacheString(11, 10);
 assertEquals("abcdefghij", collidingString1);
 String collidingString2 = reader.cacheString(0, 10);
 assertNotEquals(collidingString1, collidingString2);
 }
}