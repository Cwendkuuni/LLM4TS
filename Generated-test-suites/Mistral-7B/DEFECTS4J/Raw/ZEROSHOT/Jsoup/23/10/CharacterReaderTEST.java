package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

import org.junit.Test;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        assertEquals('A', reader.current());
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("AB", reader.consumeAsString());
        assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BC", reader.consumeTo('C'));
        assertEquals('D', reader.current());
        assertEquals("EF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BC", reader.consumeTo("C"));
        assertEquals('D', reader.current());
        assertEquals("EF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("123ABCdeF45");
        assertEquals("23ABC", reader.consumeToAny('2', 'A', 'C'));
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ab123cdEfG45h");
        assertEquals("abcdEfG", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertEquals("cd%4E", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('z', 'a', 'b'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cd%4EfG45h", reader.toString());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertEquals("cd%4EfG45h", reader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ab123cd%4EfG45h");
        assertTrue(reader.containsIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("ZX"));
    }
}