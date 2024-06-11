package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertNull(reader.consume());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("", reader.consumeAsString());
        assertEquals("", reader.consumeTo(''));
        assertEquals("A", reader.consumeTo('A'));
        assertEquals("", reader.consumeToAny(new char[]{}));
        assertEquals("", reader.consumeToEnd());
        assertEquals("A", reader.consumeLetterSequence());
        assertFalse(reader.matches('B'));
        assertFalse(reader.matches("AB"));
        assertFalse(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesAny(new char[]{'B'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchConsume("AB"));
        assertFalse(reader.matchConsumeIgnoreCase("ab"));
        assertFalse(reader.containsIgnoreCase("Ab"));
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("AB");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("A", reader.consumeAsString());
        assertEquals("AB", reader.consumeTo('B'));
        assertEquals("AB", reader.consumeTo(new String("AB")));
        assertEquals("A", reader.consumeToAny(new char[]{'A'}));
        assertEquals("B", reader.consumeToEnd());
        assertEquals("AB", reader.consumeLetterSequence());
        assertFalse(reader.matches('C'));
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesAny(new char[]{'A', 'B'}));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchConsume("C"));
        assertTrue(reader.matchConsume("A"));
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertFalse(reader.containsIgnoreCase("ab"));
    }

    @Test
    public void testCharacterReaderLongInput() {
        CharacterReader reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        // ...
        assertEquals('Z', reader.consume());
        assertEquals(26, reader.pos());
        assertEquals("", reader.consumeAsString());
        assertEquals("", reader.consumeTo(''));
        assertEquals("", reader.consumeTo(""));
        assertEquals("A", reader.consumeTo('A'));
        assertEquals("AB", reader.consumeTo("AB"));
        assertEquals("", reader.consumeToAny(new char[]{'A', 'B'}));
        assertEquals("", reader.consumeToEnd());
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeLetterSequence());
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesAny(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchConsume("ab"));
        assertTrue(reader.matchConsume("A"));
        assertTrue(reader.matchConsumeIgnoreCase("A"));
        assertFalse(reader.containsIgnoreCase("ab"));
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABC");
        assertFalse(reader.isEmpty());
        reader.mark();
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }
}