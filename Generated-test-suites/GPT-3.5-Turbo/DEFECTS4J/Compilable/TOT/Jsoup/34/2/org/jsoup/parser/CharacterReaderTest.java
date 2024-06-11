package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test input");
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
        for (int i = 0; i < 20; i++) {
            reader.consume();
        }
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
        reader.advance();
        assertEquals('h', reader.current());
        reader.advance();
        assertEquals('i', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals('i', reader.current());
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("h", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(2, reader.nextIndexOf('i'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(2, reader.nextIndexOf("is"));
        assertEquals(-1, reader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Th", reader.consumeTo('i'));
        assertEquals(" is a test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        assertEquals("Th", reader.consumeTo("is"));
        assertEquals(" is a test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Th", reader.consumeToAny('i', 's'));
        assertEquals("s is a test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals(" ", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", reader.consumeLetterThenDigitSequence());
        assertEquals(" ", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", reader.consumeHexSequence());
        assertEquals(" ", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("i", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("This"));
        assertFalse(reader.matches("this"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("This"));
        assertTrue(reader.matchesIgnoreCase("this"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'h', 'e'));
        assertFalse(reader.matchesAny('t', 'H', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("this"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("This"));
        assertTrue(reader.matchConsumeIgnoreCase("this"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("IS"));
        assertFalse(reader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test input", reader.toString());
    }
}