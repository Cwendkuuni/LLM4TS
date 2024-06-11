package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "This is a test";
        reader = new CharacterReader(input);
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
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('h', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This is a ", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("This", reader.consumeTo("is"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", reader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
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
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('S'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(reader.matches("This"));
        assertFalse(reader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("this"));
        assertFalse(reader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T'));
        assertFalse(reader.matchesAny('S'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("Test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("this"));
        assertFalse(reader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("this"));
        assertFalse(reader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test", reader.toString());
    }
    
}