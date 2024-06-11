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
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSeq() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("es", reader.consumeTo('t'));
        assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSeq() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("es", reader.consumeTo("t"));
        assertEquals("", reader.consumeTo("ex"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeToAny('x'));
        assertEquals("te", reader.consumeToAny('s'));
        assertEquals("", reader.consumeToAny('t'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeHexSequence());
        assertEquals("123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesSeq() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("te"));
        assertFalse(reader.matches("ex"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TE"));
        assertFalse(reader.matchesIgnoreCase("EX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('x'));
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
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("ex"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TE"));
        assertFalse(reader.matchConsumeIgnoreCase("EX"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TE"));
        assertFalse(reader.containsIgnoreCase("EX"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}