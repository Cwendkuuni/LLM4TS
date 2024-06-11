package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test Input");
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
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals(' ', characterReader.consume());
        assertEquals('I', characterReader.consume());
        assertEquals('n', characterReader.consume());
        assertEquals('p', characterReader.consume());
        assertEquals('u', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('s', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithNoMatch() {
        assertEquals("Test Input", characterReader.consumeTo(','));
    }

    @Test
    public void testConsumeToWithSequence() {
        assertEquals(" Input", characterReader.consumeTo("Test"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny(' ', 'I'));
    }

    @Test
    public void testConsumeToAnyWithNoMatch() {
        assertEquals("Test Input", characterReader.consumeToAny(','));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Te", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertFalse(characterReader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("xyz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("TES"));
        assertTrue(characterReader.containsIgnoreCase("inp"));
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        assertEquals("Test Input", characterReader.toString());
    }
}