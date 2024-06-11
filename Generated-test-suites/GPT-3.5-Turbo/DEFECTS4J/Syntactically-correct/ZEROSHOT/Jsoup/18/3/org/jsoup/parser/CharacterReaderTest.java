package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test string");
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
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test ", characterReader.consumeTo(' '));
        assertEquals("string", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny(' '));
        assertEquals("", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals(" string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
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
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('a'));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T'));
        assertFalse(characterReader.matchesAny('a'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(characterReader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        assertEquals("Test string", characterReader.toString());
    }
}