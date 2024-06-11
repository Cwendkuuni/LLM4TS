package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest class to test CharacterReader methods.
 */
public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader("test input");
 assertEquals("test input", characterReader.input);
 assertEquals(10, characterReader.length);
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', characterReader.current());
 characterReader.consume();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("", characterReader.consumeAsString());
 characterReader.consume();
 characterReader.consume();
 assertEquals("ab", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo('c');
 assertEquals("a", consumed);
 assertEquals('b', characterReader.current());
 consumed = characterReader.consumeTo('d');
 assertEquals("b", consumed);
 assertEquals('c', characterReader.current());
 consumed = characterReader.consumeToEnd();
 assertEquals("defgihj", consumed);
 }

 @Test
 public void testConsumeToAny() {
 String consumed = characterReader.consumeToAny('b', 'c');
 assertEquals("a", consumed);
 consumed = characterReader.consumeToAny('b');
 assertEquals("ab", consumed);
 consumed = characterReader.consumeToAny('x');
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 assertEquals("defgihj", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 assertEquals("", consumed);
 consumed = characterReader.consumeLetterSequence();
 assertEquals("a", consumed);
 consumed = characterReader.consumeLetterSequence();
 assertEquals("abcd", consumed);
 consumed = characterReader.consumeLetterSequence();
 assertEquals("efgihj", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 assertEquals("", consumed);
 consumed = characterReader.consumeHexSequence();
 assertEquals("a", consumed);
 consumed = characterReader.consumeHexSequence();
 assertEquals("ab", consumed);
 consumed = characterReader.consumeHexSequence();
 assertEquals("def", consumed);
 consumed = characterReader.consumeHexSequence();
 assertEquals("gihj", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 assertEquals("", consumed);
 consumed = characterReader.consumeDigitSequence();
 assertEquals("0", consumed);
 consumed = characterReader.consumeDigitSequence();
 assertEquals("12", consumed);
 consumed = characterReader.consumeDigitSequence();
 assertEquals("345", consumed);
 consumed = characterReader.consumeDigitSequence();
 assertEquals("6789", consumed);
 consumed = characterReader.consumeDigitSequence();
 assertEquals("", consumed);
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('a'));
 assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("ab"));
 assertFalse(characterReader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("Ab"));
 assertFalse(characterReader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('a', 'b'));
 assertFalse(characterReader.matchesAny('x'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String consumed = characterReader.matchConsume("ab");
 assertEquals("ab", consumed);
 consumed = characterReader.matchConsume("cd");
 assertEquals("", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = characterReader.matchConsumeIgnoreCase("Ab");
 assertEquals("ab", consumed);
 consumed = characterReader.matchConsumeIgnoreCase("CD");
 assertEquals("", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("ef"));
 assertTrue(characterReader.containsIgnoreCase("GH"));
 assertFalse(characterReader.containsIgnoreCase("XYZ"));
 }

 @Test
 public void testToString() {
 assertEquals("defgihj", characterReader.toString());
 }
}