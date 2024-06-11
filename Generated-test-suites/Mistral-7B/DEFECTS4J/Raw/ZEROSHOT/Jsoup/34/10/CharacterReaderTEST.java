package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(-1, reader.nextIndexOf('b'));
        assertEquals("a", reader.consumeAsString());
        assertEquals('a', reader.current());
        assertEquals(-1, reader.nextIndexOf("ab"));
        assertEquals("a", reader.consumeTo('a').toString());
        assertEquals("a", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals(-1, reader.nextIndexOf('d'));
        assertEquals("a", reader.consumeAsString());
        assertEquals("ab", reader.consumeTo('b').toString());
        assertEquals("abc", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderScanSingleCharacter() {
        CharacterReader reader = new CharacterReader("aaabbbbccccc");
        assertEquals(3, reader.nextIndexOf('a'));
        assertEquals(4, reader.nextIndexOf('b'));
        assertEquals(7, reader.nextIndexOf('c'));
    }

    @Test
    public void testCharacterReaderScanSequence() {
        CharacterReader reader = new CharacterReader("aaabbbbccccc");
        assertEquals("aaa", reader.consumeTo('a').toString());
        assertEquals("bbb", reader.consumeTo('b').toString());
        assertEquals("cccc", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderScanIgnoreCase() {
        CharacterReader reader = new CharacterReader("AAAbbbCCC");
        assertTrue(reader.containsIgnoreCase("aaa"));
        assertTrue(reader.containsIgnoreCase("bbb"));
        assertTrue(reader.containsIgnoreCase("ccc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }
}