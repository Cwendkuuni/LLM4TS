package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkRewind() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny(' ', '!'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals(" World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("World"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("World"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'W'));
        assertFalse(characterReader.matchesAny('e', '!'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("World"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("World"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("Hello"));
        assertFalse(characterReader.containsIgnoreCase("World"));
    }
}