package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals("bc", reader.toString());
    }

    @Test
    public void testConsumeMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.consume(2));
        assertEquals("", reader.toString());
    }

    @Test
    public void testConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
        assertEquals("", reader.toString());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeTo("ef"));
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals("cdcd", reader.consumeToAny('c', 'd'));
        assertEquals("ab", reader.toString());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader("abababab");
        assertEquals(1, reader.nextIndexOf("ab"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
        assertEquals("ababab", reader.toString());
    }

    @Test
    public void testMatchesChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('x'));
        assertEquals("abcabc", reader.toString());
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abababab");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("xyz"));
        assertEquals("ababab", reader.toString());
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbABaBaBAb");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
        assertEquals("AbABaBaBAb", reader.toString());
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matchesAny('a', 'd'));
        assertFalse(reader.matchesAny('x', 'z'));
        assertEquals("abcdabcd", reader.toString());
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
        assertEquals("abcdabcd", reader.toString());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
        assertEquals("abcdabcd", reader.toString());
    }
}