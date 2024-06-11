package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals("A", reader.toString());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals("abc", reader.toString());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals("ab", reader.toString());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals("c", reader.toString());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
        reader.mark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals("bc", reader.toString());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(26, reader.nextIndexOf('Z'));
        assertEquals(-1, reader.nextIndexOf('X'));
        assertEquals(-1, reader.nextIndexOf('Z', 5));
        assertEquals(22, reader.nextIndexOf('M', 5));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals("a", reader.consumeTo('a').toString());
        assertEquals("bc", reader.consumeTo('c').toString());
        assertEquals("defg", reader.consumeTo('g').toString());
        assertEquals("hijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeToAny('a', 'z', 'A', 'Z').toString());
        assertEquals("", reader.consumeToEnd().toString());
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals("ab", reader.consumeTo("ab").toString());
        assertEquals("cdefg", reader.consumeTo("defg").toString());
        assertEquals("hijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeTo("xyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toString());
        assertEquals("", reader.consumeTo("xyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("xy"));
        assertFalse(reader.matchesIgnoreCase("XY"));
        assertFalse(reader.matchesAny(new char[]{'x', 'y'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderConsumeMatches() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertTrue(reader.matchConsume('a'));
        assertEquals("bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.toString());
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cdefgihijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.toString());
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertEquals("defgihijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.toString());
        assertFalse(reader.matchConsume('x'));
        assertEquals("defgihijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>My Title</title></head><body>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
        assertFalse(reader.containsIgnoreCase("/BODY"));
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }
}