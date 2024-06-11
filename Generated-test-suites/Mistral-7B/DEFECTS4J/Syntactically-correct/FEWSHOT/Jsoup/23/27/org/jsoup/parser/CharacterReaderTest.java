package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
        assertEquals(-1, reader.pos());
    }

    @Test
    public void testSimpleConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("ef", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('z'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeLetterSequence());
        assertEquals("world", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0xFFFF");
        assertEquals("FF", reader.consumeHexSequence());
        assertEquals("FF", reader.consumeHexSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('e'));
        assertTrue(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCd");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("Cd"));
        assertFalse(reader.matchesIgnoreCase("E"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('g', 'a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("head"));
        assertFalse(reader.containsIgnoreCase("body"));
    }
}