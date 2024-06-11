package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\n'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader("hello world\n");
 assertEquals("hello world\n", characterReader.input);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 try {
 new CharacterReader(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
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
 assertEquals('h', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 assertEquals('h', characterReader.current());
 assertEquals(5, characterReader.pos());
 }

 @Test
 public void testUnconsume() {
 characterReader.unconsume();
 assertEquals('h', characterReader.current());
 assertEquals(5, characterReader.pos());
 characterReader.unconsume();
 assertEquals('h', characterReader.current());
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('e', characterReader.current());
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 assertEquals(6, characterReader.pos());
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('e', characterReader.current());
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals(" world\n", characterReader.consumeAsString());
 assertEquals('w', characterReader.current());
 }

 @Test
 public void testConsumeTo() {
 assertEquals(" world\n", characterReader.consumeTo(' '));
 assertFalse(characterReader.matches(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("\n", characterReader.consumeToEnd());
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testConsumeToAny() {
 characterReader.advance();
 characterReader.advance();
 assertEquals("ld", characterReader.consumeToAny('d', 'l'));
 assertEquals('r', characterReader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("ello", characterReader.consumeLetterSequence());
 assertEquals('w', characterReader.current());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("world\n", characterReader.input);
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 assertEquals("ld\n", characterReader.consumeHexSequence());
 assertEquals('\n', characterReader.current());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", characterReader.consumeDigitSequence());
 assertEquals('\n', characterReader.current());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('l'));
 assertFalse(characterReader.matches('o'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("ELLO"));
 }

 @Test
 public void testMatchesAny() {
 characterReader.advance();
 characterReader.advance();
 assertTrue(characterReader.matchesAny('d', 'l'));
 assertEquals('r', characterReader.current());
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("world\n", characterReader.input);
 characterReader.advance();
 characterReader.advance();
 characterReader.matchConsume("ld");
 assertEquals('\n', characterReader.current());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("world\n", characterReader.input);
 characterReader.advance();
 characterReader.advance();
 characterReader.matchConsumeIgnoreCase("ld");
 assertEquals('\n', characterReader.current());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("ld"));
 }

 @Test
 public void testToString() {
 assertEquals("\n", characterReader.toString());
 }
}