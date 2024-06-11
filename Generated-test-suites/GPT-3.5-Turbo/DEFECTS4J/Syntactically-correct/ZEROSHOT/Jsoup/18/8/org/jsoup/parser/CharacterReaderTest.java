
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello World");
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
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
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
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('H', reader.advance());
        assertEquals('e', reader.advance());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello ", reader.consumeTo('W'));
        assertEquals("orld", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello ", reader.consumeToAny('W', 'o'));
        assertEquals("W", reader.consumeToAny('o'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals(" ", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("12AB3F");
        assertEquals("12AB", reader.consumeHexSequence());
        assertEquals("3F", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("12", reader.consumeDigitSequence());
        assertEquals("3", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches('W'));
        assertFalse(reader.matches("World"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("HeLLo"));
        assertFalse(reader.matchesIgnoreCase("World"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('W', 'o'));
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
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("World"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("HeLLo"));
        assertFalse(reader.matchConsumeIgnoreCase("World"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("hello"));
        assertFalse(reader.containsIgnoreCase("world"));
    }
}
