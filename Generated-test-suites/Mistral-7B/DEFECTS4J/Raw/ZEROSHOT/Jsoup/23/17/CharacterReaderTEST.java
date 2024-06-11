package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import org.junit.Test;

/**
 * CharacterReaderTest class
 *
 * @author Johnson
 */
public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] text = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'd', ' ', 't', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'g', 'o', 'o', 'd', ' ', 'f', 'o', 'r', ' ', 't', 'e', 's', 't'};
 char[] sequence = {'h', 'e', 'l', 'l'};
 char[] sequenceLetter = {'h', 'e', 'l', 'l', 'o'};
 char[] sequenceHex = {'48', '65', '6c', '6c', '6c'};
 char[] sequenceDigit = {'3'};
 char c = 'l';
 char cHex = '4' + (char) 0x10; // '4' + 16
 String seq = "hello";
 String seqLetter = "hello";
 String seqIgnoreCase = "HELLO";
 String seqLetterSequence = "hello";
 String seqHexSequence = "hello";
 String seqDigitSequence = "3";
 String seqIgnoreCase2 = "this is a good test";
 String seqAny = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'd'};
 String input = new String(text);

 characterReader = new CharacterReader(input);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.advance();
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 characterReader.advance();
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals(characterReader.current(), characterReader.consume());
 characterReader.consume();
 assertNotEquals(characterReader.current(), c);
 }

 @Test
 public void testUnconsume() {
 characterReader.advance();
 characterReader.unconsume();
 assertEquals(characterReader.current(), c);
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 characterReader.advance();
 assertEquals(characterReader.current(), 'w');
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals(characterReader.current(), c);
 }

 @Test
 public void testConsumeAsString() {
 assertEquals(characterReader.consumeAsString(), Character.toString(c));
 }

 @Test
 public void testConsumeTo() {
 String consume = characterReader.consumeTo(c);
 assertEquals(characterReader.consumeAsString(), consume);
 }

 @Test
 public void testConsumeToString() {
 String consumeLetterSequence = characterReader.consumeLetterSequence();
 assertEquals(seqLetterSequence, consumeLetterSequence);
 characterReader.advance();
 consumeLetterSequence = characterReader.consumeLetterSequence();
 assertEquals(seqLetter, consumeLetterSequence);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumeHexSequence = characterReader.consumeHexSequence();
 assertEquals(seqHexSequence, consumeHexSequence);
 characterReader.advance();
 consumeHexSequence = characterReader.consumeHexSequence();
 assertEquals(seqHex, consumeHexSequence);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumeDigitSequence = characterReader.consumeDigitSequence();
 assertEquals(seqDigitSequence, consumeDigitSequence);
 characterReader.advance();
 consumeDigitSequence = characterReader.consumeDigitSequence();
 assertEquals(Character.toString(c), consumeDigitSequence);
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches(c));
 characterReader.advance();
 assertFalse(characterReader.matches(c));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches(seq));
 characterReader.consumeLetterSequence();
 assertFalse(characterReader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 assertTrue(characterReader.matchesIgnoreCase(seqIgnoreCase));
 characterReader.consumeLetterSequence();
 assertTrue(characterReader.matchesIgnoreCase(seqIgnoreCase2));
 }

 @Test
 public void testMatchesAny() {
 Character[] seqAnyChars = new Character[seqAny.length];
 System.arraycopy(seqAny, 0, seqAnyChars, 0, seqAny.length);
 assertTrue(characterReader.matchesAny(seqAnyChars));
 characterReader.advance();
 assertFalse(characterReader.matchesAny(seqAnyChars));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 characterReader.advance();
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 characterReader.advance();
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 assertTrue(characterReader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 assertTrue(characterReader.matchConsumeIgnoreCase(seqIgnoreCase));
 characterReader.consumeLetterSequence();
 assertTrue(characterReader.matchConsumeIgnoreCase(seqIgnoreCase2));
 }

 @Test
 public void testContainsIgnoreCaseString() {
 assertTrue(characterReader.containsIgnoreCase(seqIgnoreCase));
 characterReader.consumeLetterSequence();
 assertTrue(characterReader.containsIgnoreCase(seqIgnoreCase2));
 }

 @Test
 public void testToString() {
 assertEquals(Character.toString(c), characterReader.toString());
 characterReader.advance();
 assertEquals("", characterReader.toString());
 }
}