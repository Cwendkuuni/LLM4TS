package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals(0, cr.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader cr = new CharacterReader("");
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals('t', cr.current());
    }

    @Test
    public void testConsume() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals('t', cr.consume());
        assertEquals('e', cr.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader cr = new CharacterReader("test");
        cr.consume();
        cr.unconsume();
        assertEquals('t', cr.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader cr = new CharacterReader("test");
        cr.advance();
        assertEquals('e', cr.current());
    }

    @Test
    public void testMark() {
        CharacterReader cr = new CharacterReader("test");
        cr.mark();
        assertEquals(0, cr.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader cr = new CharacterReader("test");
        cr.advance();
        cr.mark();
        cr.advance();
        cr.rewindToMark();
        assertEquals('t', cr.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("t", cr.consumeAsString());
        assertEquals("e", cr.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("", cr.consumeTo('t'));
        assertEquals("t", cr.consumeTo('e'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("tes", cr.consumeToAny('t'));
        assertEquals("t", cr.consumeToAny('e'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("test", cr.consumeToEnd());
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("test", cr.consumeLetterSequence());
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader cr = new CharacterReader("123abc");
        assertEquals("123abc", cr.consumeHexSequence());
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader cr = new CharacterReader("123abc");
        assertEquals("123", cr.consumeDigitSequence());
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matches('t'));
        assertFalse(cr.matches('e'));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchesAny('t'));
        assertFalse(cr.matchesAny('e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchesLetter());
        assertFalse(cr.matchesDigit());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.containsIgnoreCase("te"));
        assertFalse(cr.containsIgnoreCase("ab"));
    }
}
