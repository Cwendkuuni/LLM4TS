package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReader_singleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_multipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeAsString());
        assertEquals('b', reader.current());
        assertEquals("ab", reader.consumeAsString());
        assertEquals(2, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("d", reader.consumeToEnd().charAt(0));
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("xyz123xyz");
        assertEquals("123", reader.consumeToAny('1', '3'));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('g'));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("CD"));
        assertFalse(reader.matchesIgnoreCase("G"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("abc123xyz");
        assertTrue(reader.matchesAny('a', 'c', '1', '2', '3'));
        assertTrue(reader.matchesAny('x', 'z'));
        assertFalse(reader.matchesAny('d'));
    }

    @Test
    public void testCharacterReader_markAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
    }
}