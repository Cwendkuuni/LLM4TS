package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("test");
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
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
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
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        assertEquals("s", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("tes", characterReader.consumeTo('t'));
        assertEquals("t", characterReader.consumeTo('e'));
        assertEquals("es", characterReader.consumeTo('s'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("te", characterReader.consumeToAny('e', 's'));
        assertEquals("s", characterReader.consumeToAny('t'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("test", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("test", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("tEsT"));
        assertFalse(characterReader.matchesIgnoreCase("TeSt"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('e', 't'));
        assertFalse(characterReader.matchesAny('s'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("test"));
        assertFalse(characterReader.matchConsume("Test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("tEsT"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("Test"));
    }

    @Test
    public void testToString() {
        assertEquals("test", characterReader.toString());
    }
}