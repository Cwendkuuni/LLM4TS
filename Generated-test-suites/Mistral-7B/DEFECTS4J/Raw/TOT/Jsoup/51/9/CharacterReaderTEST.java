package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testUnconsume() {
 reader.unconsume();
 assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("a", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(1, reader.nextIndexOf('b'));
 }

 @Test
 public void testNextIndexOfWithSequence() {
 char[] seq = {'b', 'c'};
 assertEquals(1, reader.nextIndexOf(seq));
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ab", reader.consumeTo('c'));
 }

 @Test
 public void testConsumeToWithSequence() {
 assertEquals("ab", reader.consumeTo(new String("ab")));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("cde", reader.consumeToAny(new char[]{'c', 'd', 'e'}));
 }

 @Test
 public void testConsumeToAnySorted() {
 assertEquals("de", reader.consumeToAnySorted(new char[]{'d', 'e'}));
 }

 @Test
 public void testConsumeData() {
 assertEquals("&<", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("tag", reader.consumeTagName());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("abcde", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcd", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("ab12cd", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("abcd12ef", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("12345", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('b'));
 }

 @Test
 public void testMatchesWithSequence() {
 assertTrue(reader.matches(new String("ab")));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase(new String("Ab")));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
 }

 @Test
 public void testMatchesAnySorted() {
 assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
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
 assertEquals("ab", reader.matchConsume(new String("ab")));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("AB", reader.matchConsumeIgnoreCase(new String("Ab")));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase(new String("ab")));
 }
}