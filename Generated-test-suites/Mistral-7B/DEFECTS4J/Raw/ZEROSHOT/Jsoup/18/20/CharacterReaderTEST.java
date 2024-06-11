package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

CharacterReader characterReader;

@Test
public void testCharacterReaderEmpty() {
CharacterReader emptyCharacterReader = new CharacterReader("");
assertTrue(emptyCharacterReader.isEmpty());
assertEquals(-1, emptyCharacterReader.current());
}

@Test
public void testCharacterReaderWithInput() {
characterReader = new CharacterReader("abcdef");
assertEquals(0, characterReader.pos());
assertEquals('a', characterReader.current());
assertEquals("a", characterReader.consumeAsString());
assertEquals(1, characterReader.pos());
assertEquals('b', characterReader.current());
}

@Test
public void testConsumeToEnd() {
characterReader = new CharacterReader("abcdef");
assertEquals("def", characterReader.consumeToEnd());
assertEquals(6, characterReader.pos());
}

@Test
public void testConsumeToCharacter() {
characterReader = new CharacterReader("abcdef");
assertEquals("def", characterReader.consumeTo('f'));
assertEquals(6, characterReader.pos());
assertEquals('e', characterReader.current());
}

@Test(expected = IllegalArgumentException.class)
public void testConsumeToCharacterNegative() {
characterReader = new CharacterReader("abcdef");
characterReader.consumeTo('a');
}

@Test
public void testConsumeToSequence() {
characterReader = new CharacterReader("abcdef");
assertEquals("def", characterReader.consumeTo("ef"));
assertEquals(6, characterReader.pos());
}

@Test
public void testConsumeToSequenceEmpty() {
characterReader = new CharacterReader("abcdef");
assertEquals("", characterReader.consumeTo(""));
assertEquals(0, characterReader.pos());
}

@Test
public void testConsumeToSequenceNotPresent() {
characterReader = new CharacterReader("abcdef");
assertEquals("def", characterReader.consumeTo("xyz"));
assertEquals(6, characterReader.pos());
}

@Test
public void testConsumeToAny() {
characterReader = new CharacterReader("abcdefg");
assertEquals("defg", characterReader.consumeToAny('d', 'f'));
assertEquals(6, characterReader.pos());
}

@Test
public void testConsumeLetterSequence() {
characterReader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.consumeLetterSequence());
assertEquals(48, characterReader.pos());
}

@Test
public void testConsumeHexSequence() {
characterReader = new CharacterReader("0123456789ABCDEFabcdef");
assertEquals("0123456789ABCDEFabcdef", characterReader.consumeHexSequence());
assertEquals(20, characterReader.pos());
}

@Test
public void testConsumeDigitSequence() {
characterReader = new CharacterReader("0123456789");
assertEquals("0123456789", characterReader.consumeDigitSequence());
assertEquals(10, characterReader.pos());
}

@Test
public void testMatches() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matches('e'));
assertFalse(characterReader.matches('g'));
}

@Test
public void testMatchesString() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matches("ef"));
assertFalse(characterReader.matches("xyz"));
}

@Test
public void testMatchesIgnoreCase() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matchesIgnoreCase("ef"));
assertTrue(characterReader.matchesIgnoreCase("EF"));
assertFalse(characterReader.matchesIgnoreCase("xyz"));
}

@Test
public void testMatchesAny() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matchesAny('e', 'f'));
assertFalse(characterReader.matchesAny('g', 'h'));
}

@Test
public void testMatchesLetter() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matchesLetter());
assertFalse(characterReader.matchesDigit());
}

@Test
public void testMatchesDigit() {
characterReader = new CharacterReader("0123456789");
assertTrue(characterReader.matchesDigit());
assertFalse(characterReader.matchesLetter());
}

@Test
public void testMatchConsume() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matchConsume("ef"));
assertEquals(2, characterReader.pos());
}

@Test
public void testMatchConsumeIgnoreCase() {
characterReader = new CharacterReader("abcdef");
assertTrue(characterReader.matchConsumeIgnoreCase("ef"));
assertTrue(characterReader.matchConsumeIgnoreCase("EF"));
assertEquals(2, characterReader.pos());
}

@Test
public void testContainsIgnoreCase() {
characterReader = new CharacterReader("ab<title>cd<title>efg<title>hijklm");
assertTrue(characterReader.containsIgnoreCase("/title/"));
assertFalse(characterReader.containsIgnoreCase("/head/"));
}

@Test
public void testMarkAndRewind() {
characterReader = new CharacterReader("abcdef");
characterReader.mark();
characterReader.consume();
characterReader.rewindToMark();
assertEquals('a', characterReader.current());
assertEquals(0, characterReader.pos());
}

@Test
public void testAdvance() {
characterReader = new CharacterReader("abcdef");
characterReader.advance();
assertEquals('b', characterReader.current());
assertEquals(1, characterReader.pos());
}

@Test
public void testIsEmpty() {
CharacterReader emptyCharacterReader = new CharacterReader("");
assertTrue(emptyCharacterReader.isEmpty());
CharacterReader nonEmptyCharacterReader = new CharacterReader("abcdef");
assertFalse(nonEmptyCharacterReader.isEmpty());
}
}