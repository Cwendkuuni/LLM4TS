package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("foo-bar");
        assertEquals("oo", reader.consumeTo('o'));
        assertEquals("bar", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("foo-bar");
        assertEquals("oo", reader.consumeTo("o"));
        assertEquals("bar", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToStringIgnoreCase() {
        CharacterReader reader = new CharacterReader("FOO-BAR");
        assertTrue(reader.containsIgnoreCase("oo"));
        assertEquals("oo", reader.consumeTo("o"));
        assertEquals("BAR", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcd45efg");
        assertEquals("bc", reader.consumeToAny('b', 'c'));
        assertEquals("efg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abcdef");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcde");
        reader.mark();
        assertEquals('a', reader.consume());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
}