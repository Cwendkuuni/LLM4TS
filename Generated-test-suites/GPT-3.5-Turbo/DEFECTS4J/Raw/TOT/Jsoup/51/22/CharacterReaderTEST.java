package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello, World!");
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
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals('H', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
        assertEquals(",", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(7, reader.nextIndexOf(','));
        assertEquals(-1, reader.nextIndexOf('?'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(0, reader.nextIndexOf("H"));
        assertEquals(-1, reader.nextIndexOf("W"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", reader.consumeTo(','));
        assertEquals(", ", reader.consumeTo('.'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", reader.consumeToAny('!', '?'));
        assertEquals("Hello, W", reader.consumeToAny(':', 'o', 'r', 'l', 'd', '!'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("", reader.consumeToAnySorted('!', '?'));
        assertEquals(" H", reader.consumeToAnySorted(':', 'o', 'r', 'l', 'd', '!'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Hello, W", reader.consumeData());
        assertEquals("orld!", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Hello,", reader.consumeTagName());
        assertEquals("orld!", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello, World!", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("e", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("H", reader.consumeLetterThenDigitSequence());
        reader.advance();
        reader.advance();
        assertEquals("ll", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        reader.advance();
        assertEquals("6f", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        reader.advance();
        assertEquals("l", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('W'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("Hello,"));
        assertFalse(reader.matches("World"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("hELlO,"));
        assertFalse(reader.matchesIgnoreCase("WORLD"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'W'));
        assertFalse(reader.matchesAny('!', '?'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('H', 'W'));
        assertFalse(reader.matchesAnySorted('!', '?'));
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
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("World"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("hElLo"));
        assertFalse(reader.matchConsumeIgnoreCase("woRld"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("HELL"));
        assertFalse(reader.containsIgnoreCase("low"));
    }

    @Test
    public void testToString() {
        assertEquals("Hello, World!", reader.toString());
        reader.consume();
        assertEquals("ello, World!", reader.toString());
    }
}