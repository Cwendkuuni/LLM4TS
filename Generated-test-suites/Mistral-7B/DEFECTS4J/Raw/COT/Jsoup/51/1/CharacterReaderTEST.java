package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>\t\n\r\f / >".toCharArray();
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 assertNotNull(characterReader);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.consume();
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 characterReader.consume();
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 assertFalse(characterReader.isEmpty());
 assertEquals(characterReader.current(), characterReader.input[characterReader.pos()]);
 }

 @Test
 public void testConsume() {
 assertFalse(characterReader.isEmpty());
 characterReader.consume();
 assertEquals(characterReader.current(), characterReader.input[characterReader.pos()-1]);
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 assertEquals(characterReader.current(), characterReader.input[characterReader.pos()]);
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals(characterReader.pos(), characterReader.pos()+1);
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals(characterReader.pos(), characterReader.mark);
 }

 @Test
 public void testConsumeAsString() {
 characterReader.consume();
 assertEquals(characterReader.consumeAsString(), String.valueOf(characterReader.current()));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfCharNegativeIndex() {
 characterReader.nextIndexOf((char) -1);
 }

 @Test
 public void testNextIndexOfChar() {
 char c = 'a';
 int index = characterReader.nextIndexOf(c);
 assertEquals(index, characterReader.pos() - characterReader.nextIndexOf(c) - 1);
 }

 @Test
 public void testNextIndexOfCharSequenceEmpty() {
 assertFalse(characterReader.nextIndexOf("") > -1);
 }

 @Test
 public void testNextIndexOfCharSequence() {
 String seq = "abc";
 int index = characterReader.nextIndexOf(seq);
 if (index > -1) {
 assertEquals("abc", characterReader.consumeTo(seq.charAt(0)));
 assertEquals(seq, characterReader.consumeTo(seq));
 assertEquals(index + seq.length(), characterReader.pos());
 } else {
 assertEquals("", characterReader.consumeTo(seq));
 }
 }

 @Test
 public void testConsumeToChar() {
 characterReader.consumeLetterSequence();
 characterReader.consume();
 assertEquals("b", characterReader.consumeTo('b'));
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToCharOutOfBounds() {
 characterReader.consumeTo('z');
 }

 @Test
 public void testConsumeToAny() {
 char[] chars = new char[]{'a', 'b'};
 assertEquals("ab", characterReader.consumeToAny(chars));
 }

 @Test
 public void testConsumeToAnySorted() {
 char[] chars = new char[]{'b', 'a'};
 assertEquals("a", characterReader.consumeToAnySorted(chars));
 }

 @Test
 public void testConsumeData() {
 assertEquals("&<", characterReader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("/title", characterReader.consumeTagName());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abc", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("abcd", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("12345", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 char c = 'a';
 assertTrue(characterReader.matches(c));
 characterReader.consume();
 assertFalse(characterReader.matches(c));
 }

 @Test
 public void testMatchesSequence() {
 String seq = "abc";
 assertTrue(characterReader.matches(seq));
 characterReader.consumeTo(seq);
 assertFalse(characterReader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "abC";
 assertTrue(characterReader.matchesIgnoreCase(seq));
 characterReader.consumeTo("aBC");
 assertFalse(characterReader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] chars = new char[]{'a', 'b'};
 assertTrue(characterReader.matchesAny(chars));
 characterReader.consumeTo("abcd");
 assertFalse(characterReader.matchesAny(chars));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] chars = new char[]{'b', 'a'};
 assertTrue(characterReader.matchesAnySorted(chars));
 characterReader.consumeTo("bcd");
 assertFalse(characterReader.matchesAnySorted(chars));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 characterReader.consumeLetter();
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 characterReader.consumeDigit();
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsumeSequence() {
 String seq = "abc";
 assertTrue(characterReader.matchConsume(seq));
 characterReader.consumeTo(seq);
 assertFalse(characterReader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCaseSequence() {
 String seq = "abC";
 assertTrue(characterReader.matchConsumeIgnoreCase(seq));
 characterReader.consumeTo("aBC");
 assertFalse(characterReader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCaseSequence() {
 String seq = "</title>";
 assertTrue(characterReader.containsIgnoreCase(seq));
 characterReader.consumeToEnd();
 assertFalse(characterReader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 assertEquals("", characterReader.toString());
 characterReader.consumeLetterSequence();
 assertEquals("abc", characterReader.toString());
 }
}