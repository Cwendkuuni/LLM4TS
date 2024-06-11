package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a').trim());
        assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.nextIndexOf('A'));
        assertEquals("A", reader.consumeTo('A').trim());
        assertEquals("A", reader.toString());
        assertEquals('A', reader.current());
        reader.consume();
        assertEquals(EOF, reader.current());
        assertEquals("", reader.consumeTo('A').trim());
        assertEquals("", reader.toString());
    }

    @Test
    public void testConsumeAndNextIndexOf() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals(1, reader.nextIndexOf('f'));
        assertEquals("f", reader.consume().toString());
        assertEquals(2, reader.nextIndexOf('o'));
        assertEquals("o", reader.consume().toString());
        assertEquals(3, reader.nextIndexOf('o'));
        assertEquals("o", reader.consume().toString());
        assertEquals(EOF, reader.consume().toString()[0]);
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals("foo", reader.consumeToEnd());
        assertEquals("f", reader.consumeTo('o').trim());
        assertEquals("oo", reader.consumeTo('o').trim());
        assertEquals("oo", reader.consumeTo('o').trim());
        assertEquals("", reader.consumeTo('x').trim());
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals("foo", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence().trim());
        assertEquals("", reader.consumeLetterThenDigitSequence().trim());
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("foo123");
        assertEquals("foo123", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeLetterSequence().trim());
        assertEquals("", reader.toString().trim());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&nbsp;");
        assertEquals("&nbsp;", reader.consumeHexSequence());
        assertEquals("", reader.consume().toString().trim());
        assertEquals("", reader.toString().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));
        assertTrue(reader.matches("oo"));
        assertFalse(reader.matches("xx"));
        assertTrue(reader.matches("foo123"));
        assertTrue(reader.matches("FOO"));
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchesIgnoreCase("oo"));
        assertTrue(reader.matchesIgnoreCase("FOO"));
        assertFalse(reader.matchesIgnoreCase("XX"));
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("foo");
        assertTrue(reader.matchesAny('f', 'o'));
        assertFalse(reader.matchesAny('x', 'o'));
        assertTrue(reader.matchesAny("fo", "oo"));
        assertFalse(reader.matchesAny("xx", "oo"));
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("foo");
        assertEquals("foo", reader.consumeToEnd());
        assertEquals("f", reader.consume().toString());
        assertEquals("o", reader.consume().toString());
        assertEquals(EOF, reader.consume().toString()[0]);
        assertEquals("", reader.consumeToEnd().trim());
        assertEquals("", reader.toString().trim());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<title>test</title>"));
        assertFalse(reader.containsIgnoreCase("<body>"));
        assertEquals("<html><head><title>Test</title></head><body>", reader.toString());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("foo");
        reader.mark();
        reader.advance();
        reader.consume();
        reader.rewindToMark();
        assertEquals('f', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(EOF, reader.current());
        assertEquals("foo", reader.toString());
    }
}