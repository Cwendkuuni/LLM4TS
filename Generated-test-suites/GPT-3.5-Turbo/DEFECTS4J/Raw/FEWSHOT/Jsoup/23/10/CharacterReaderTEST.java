package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setup() {
        // create a new CharacterReader object for each test case
        reader = new CharacterReader("This is a test string");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
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
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('h', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("h", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This is a ", reader.consumeTo('t'));
        assertEquals("test s", reader.consumeTo('r'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This is a ", reader.consumeToAny('t', 'i', 's'));
        assertEquals(" te", reader.consumeToAny('s', 't'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertTrue(reader.matchesLetter());
        assertEquals("This", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertFalse(reader.matchesDigit());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertFalse(reader.matchesDigit());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("tH"));
        assertFalse(reader.matchesIgnoreCase("Th"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'h', 'i', 's'));
        assertFalse(reader.matchesAny('t', 'i', 's'));
    }

    @Test
    public void testMatchConsume() {
        assertFalse(reader.matchConsume("this"));
        assertTrue(reader.matchConsume("Thi"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("this"));
        assertTrue(reader.matchConsumeIgnoreCase("tHIs"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("test"));
        assertTrue(reader.containsIgnoreCase("string"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string", reader.toString());
        reader.consume();
        assertEquals("his is a test string", reader.toString());
    }
}