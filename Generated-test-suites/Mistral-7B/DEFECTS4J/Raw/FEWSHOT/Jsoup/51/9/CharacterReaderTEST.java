package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcde", reader.consumeTo('f').trim());
        assertEquals("", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertEquals("abc", reader.consumeTo('c').trim());
        assertEquals("abcd", reader.consumeTo('d').trim());
        assertEquals("abcdabcd", reader.consumeTo(CharacterReader.EOF).trim());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertEquals("cd", reader.consumeToAny('c', 'd').trim());
        assertEquals("", reader.consumeToAny('a', 'z').trim());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertEquals("cd", reader.consumeToAnySorted('c', 'd').trim());
        assertEquals("abcd", reader.consumeToAnySorted('a', 'd').trim());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&lt;!DOCTYPE html&gt;");
        assertEquals("&lt;!DOCTYPE html", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<html><head></head>");
        assertEquals("html", reader.consumeTagName().trim());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("xy"));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchesAny('a', 'e'));
        assertTrue(reader.matchesAny('a', 'f'));
        assertFalse(reader.matchesAny('x', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchesAnySorted('a', 'e'));
        assertTrue(reader.matchesAnySorted('e', 'e'));
        assertFalse(reader.matchesAnySorted('x', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
}