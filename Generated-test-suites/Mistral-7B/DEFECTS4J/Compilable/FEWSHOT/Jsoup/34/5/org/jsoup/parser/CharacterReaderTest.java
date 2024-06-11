package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a').trim());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals(-1, reader.nextIndexOf('b'));
        assertEquals("", reader.consumeTo('b').trim());
        assertEquals("a", reader.consumeToEnd());
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
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals("a", reader.consumeTo('b').trim());
        assertEquals("abc", reader.consumeToEnd());
        assertEquals("b", reader.consumeToAny('b').trim());
        assertEquals("c", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testCharacterReaderHexSequence() {
        CharacterReader reader = new CharacterReader("0x1234");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('0', reader.current());
        assertEquals('0', reader.consume());
        assertEquals('x', reader.current());
        assertEquals('x', reader.consume());
        assertEquals('1', reader.current());
        assertEquals('2', reader.consume());
        assertEquals('3', reader.consume());
        assertEquals('4', reader.consume());
        assertEquals(5, reader.pos());
        assertEquals("0x1234", reader.consumeToEnd());
        assertEquals("34", reader.consumeHexSequence().trim());
    }

    @Test
    public void testCharacterReaderDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('1', reader.current());
        assertEquals('1', reader.consume());
        assertEquals('2', reader.current());
        assertEquals('2', reader.consume());
        assertEquals('3', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals("123", reader.consumeToEnd());
        assertEquals("3", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeLetterSequence().trim());
        assertEquals('b', reader.current());
        assertEquals("b", reader.consumeLetterSequence().trim());
        assertEquals('c', reader.current());
        assertEquals("c", reader.consumeLetterSequence().trim());
        assertEquals('d', reader.current());
        assertEquals("d", reader.consumeLetterSequence().trim());
        assertEquals(5, reader.pos());
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('f'));
        assertFalse(reader.matches('g'));
        assertFalse(reader.matches('h'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("d"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertTrue(reader.matchesIgnoreCase("f"));
        assertFalse(reader.matchesIgnoreCase("g"));
        assertFalse(reader.matchesIgnoreCase("h"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd', 'e', 'f'));
        assertFalse(reader.matchesAny('g', 'h'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head></html>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("head"));
        assertTrue(reader.containsIgnoreCase("html"));
        assertFalse(reader.containsIgnoreCase("foot"));
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
    }
}