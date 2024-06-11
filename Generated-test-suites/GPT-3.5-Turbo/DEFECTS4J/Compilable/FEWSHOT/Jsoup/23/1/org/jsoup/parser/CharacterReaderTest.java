package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.consume();
        assertEquals('e', reader.consume());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('t', reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo('s'));
        assertEquals("st", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeToAny('e', 's'));
        assertEquals("es", reader.consumeToAny('t'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ThisIsATest");
        assertEquals("ThisIsATest", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABCDEF123");
        assertEquals("ABCDEF123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('s'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("T"));
        assertFalse(reader.matchesIgnoreCase("S"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 's'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("t"));
        assertFalse(reader.matchConsume("s"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("T"));
        assertFalse(reader.matchConsumeIgnoreCase("S"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.containsIgnoreCase("a"));
        assertTrue(reader.containsIgnoreCase("T"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}