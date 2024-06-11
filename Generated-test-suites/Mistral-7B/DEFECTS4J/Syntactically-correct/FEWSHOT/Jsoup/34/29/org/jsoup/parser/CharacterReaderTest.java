package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderIsInitialized() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(3, reader.length());
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumesCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals("c", reader.consumeAsString());
        assertEquals(0, reader.pos());
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("ab"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("ab"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("ab"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf('f'));
        assertEquals(-1, reader.nextIndexOf('g'));
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(5, reader.nextIndexOf("ef"));
        assertEquals(-1, reader.nextIndexOf("gh"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("ef", reader.consumeTo("ef"));
        assertEquals("", reader.consumeTo('g'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToString("bc"));
        assertEquals("ef", reader.consumeToString("ef"));
        assertEquals("", reader.consumeToString("g"));
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("123", reader.consumeToAny('1', '2', '3'));
        assertEquals("", reader.consumeToAny('4'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.containsIgnoreCase("a"));
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertFalse(reader.containsIgnoreCase("b"));
    }
}