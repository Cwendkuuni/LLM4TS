
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        assertEquals('T', characterReader.consume());
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.consume());
        characterReader.advance();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        assertEquals('s', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("This", characterReader.consumeAsString());
        assertEquals(" ", characterReader.consumeAsString());
        assertEquals("is", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This", characterReader.consumeTo(' '));
        assertEquals(" ", characterReader.consumeTo('i'));
        assertEquals("is", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' '));
        assertEquals(" ", characterReader.consumeToAny('i'));
        assertEquals("is", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
        assertEquals("is", characterReader.consumeLetterSequence());
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
        characterReader.consume();
        assertFalse(characterReader.matches('A'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("tHIS"));
        characterReader.consume();
        assertFalse(characterReader.matchesIgnoreCase("tHAT"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'A'));
        characterReader.consume();
        assertFalse(characterReader.matchesAny('T', 'A'));
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
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("That"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("tHIS"));
        assertFalse(characterReader.matchConsumeIgnoreCase("tHAT"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("a"));
        assertFalse(characterReader.containsIgnoreCase("b"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string", characterReader.toString());
    }
}
