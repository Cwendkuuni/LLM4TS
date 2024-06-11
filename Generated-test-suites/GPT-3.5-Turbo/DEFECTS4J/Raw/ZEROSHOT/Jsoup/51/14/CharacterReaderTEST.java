package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
    }

    @Test
    public void pos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void isEmpty() {
        assertTrue(reader.isEmpty());
    }

    @Test
    public void current() {
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void consume() {
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void unconsume() {
        reader.unconsume();
        assertEquals(-1, reader.pos());
    }

    @Test
    public void advance() {
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void mark() {
        reader.mark();
        assertEquals(0, reader.mark);
    }

    @Test
    public void rewindToMark() {
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void consumeAsString() {
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void nextIndexOf() {
        assertEquals(-1, reader.nextIndexOf('c'));
    }

    @Test
    public void nextIndexOf1() {
        assertEquals(-1, reader.nextIndexOf("test"));
    }

    @Test
    public void consumeTo() {
        assertEquals("", reader.consumeTo('c'));
    }

    @Test
    public void consumeTo1() {
        assertEquals("", reader.consumeTo("test"));
    }

    @Test
    public void consumeToAny() {
        assertEquals("", reader.consumeToAny('a'));
    }

    @Test
    public void consumeToAnySorted() {
        assertEquals("", reader.consumeToAnySorted('a'));
    }

    @Test
    public void consumeData() {
        assertEquals("", reader.consumeData());
    }

    @Test
    public void consumeTagName() {
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void consumeToEnd() {
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void consumeLetterSequence() {
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void consumeLetterThenDigitSequence() {
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void consumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void consumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void matches() {
        assertFalse(reader.matches('c'));
    }

    @Test
    public void matches1() {
        assertFalse(reader.matches("test"));
    }

    @Test
    public void matchesIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("test"));
    }

    @Test
    public void matchesAny() {
        assertFalse(reader.matchesAny('a'));
    }

    @Test
    public void matchesAnySorted() {
        assertFalse(reader.matchesAnySorted('a'));
    }

    @Test
    public void matchesLetter() {
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void matchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void matchConsume() {
        assertFalse(reader.matchConsume("test"));
    }

    @Test
    public void matchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void containsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        assertEquals("", reader.toString());
    }
}