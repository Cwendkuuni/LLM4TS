package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for CharacterReader class.
 */
public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("test input");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("test ", reader.consumeTo('i'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("test in", reader.consumeTo("pu"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("te", reader.consumeToAny('s', 'i'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatchesChar() {
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("input"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TeSt"));
        assertFalse(reader.matchesIgnoreCase("inPUT"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('i', 's'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consumeToEnd();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("input"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TeSt"));
        assertFalse(reader.matchConsumeIgnoreCase("inPUT"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("TEST"));
        assertFalse(reader.containsIgnoreCase("INPUT"));
    }

    @Test
    public void testToString() {
        assertEquals("test input", reader.toString());
    }
}