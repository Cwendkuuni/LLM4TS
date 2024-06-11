package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader cr = new CharacterReader("");
        assertTrue(cr.isEmpty());

        cr = new CharacterReader("abcdef");
        assertFalse(cr.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals(0, cr.pos());

        cr.advance();
        assertEquals(1, cr.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals('a', cr.current());

        cr.advance();
        assertEquals('b', cr.current());
    }

    @Test
    public void testConsume() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals('a', cr.current());
        assertEquals('b', cr.consume());
        assertEquals('c', cr.current());

        cr.advance();
        assertEquals('e', cr.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals('a', cr.current());
        cr.consume();
        assertEquals('b', cr.current());
        cr.unconsume();
        assertEquals('a', cr.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals('a', cr.current());
        cr.advance();
        assertEquals('b', cr.current());

        cr.advance(3);
        assertEquals('e', cr.current());
    }

    @Test
    public void testMark() {
        CharacterReader cr = new CharacterReader("abcdef");
        cr.mark();
        assertEquals('a', cr.current());
        cr.advance();
        assertEquals('b', cr.current());

        cr.rewindToMark();
        assertEquals('a', cr.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals("ab", cr.consumeAsString());

        cr.advance();
        assertEquals("cd", cr.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader cr = new CharacterReader("abcdefghijkl");
        assertEquals("ef", cr.consumeTo('f'));
        assertEquals("hijkl", cr.consumeToEnd());

        cr.rewindToMark();
        assertEquals("ab", cr.consumeTo('b'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader cr = new CharacterReader("abc123def");
        assertEquals("123", cr.consumeToAny('1', '3'));
        assertEquals("def", cr.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertEquals("def", cr.consumeToEnd());
        assertEquals("", cr.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader cr = new CharacterReader("abc123def");
        assertEquals("abc", cr.consumeLetterSequence());
        assertEquals("", cr.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader cr = new CharacterReader("abc123def");
        assertEquals("12", cr.consumeHexSequence());
        assertEquals("", cr.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader cr = new CharacterReader("abc123def");
        assertEquals("123", cr.consumeDigitSequence());
        assertEquals("", cr.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertTrue(cr.matches('e'));
        assertFalse(cr.matches('g'));

        cr.advance();
        assertTrue(cr.matches('f'));
        assertFalse(cr.matches('g'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertTrue(cr.matchesIgnoreCase("e"));
        assertFalse(cr.matchesIgnoreCase("g"));

        cr.advance();
        assertTrue(cr.matchesIgnoreCase("f"));
        assertFalse(cr.matchesIgnoreCase("g"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertTrue(cr.matchesAny('a', 'e'));
        assertFalse(cr.matchesAny('g'));

        cr.advance();
        assertTrue(cr.matchesAny('b', 'f'));
        assertFalse(cr.matchesAny('g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertTrue(cr.matchesLetter());
        assertFalse(cr.matchesLetter());

        cr.advance();
        assertTrue(cr.matchesLetter());
        assertFalse(cr.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertFalse(cr.matchesDigit());
        assertFalse(cr.matchesDigit());

        cr.advance();
        assertFalse(cr.matchesDigit());
        assertTrue(cr.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertTrue(cr.matchConsume("ef"));
        assertFalse(cr.matchConsume("xy"));

        cr.advance();
        assertTrue(cr.matchConsume("fd"));
        assertFalse(cr.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader cr = new CharacterReader("abcdef");
        assertTrue(cr.matchConsumeIgnoreCase("ef"));
        assertFalse(cr.matchConsumeIgnoreCase("XY"));

        cr.advance();
        assertTrue(cr.matchConsumeIgnoreCase("fd"));
        assertFalse(cr.matchConsumeIgnoreCase("XY"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader cr = new CharacterReader(" <html><head><title>Test</title></head><body>");
        assertTrue(cr.containsIgnoreCase("<title>"));
        assertTrue(cr.containsIgnoreCase("<body>"));
        assertFalse(cr.containsIgnoreCase("<head>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        CharacterReader cr = new CharacterReader("");
        cr.consume();
    }
}