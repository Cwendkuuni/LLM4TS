package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World");
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
        assertEquals('H', characterReader.consume());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(7, characterReader.nextIndexOf('o'));
    }

    @Test
    public void testNextIndexOfWithCharNotPresent() {
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(7, characterReader.nextIndexOf("orl"));
    }

    @Test
    public void testNextIndexOfWithSequenceNotPresent() {
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hell", characterReader.consumeTo('o'));
    }

    @Test
    public void testConsumeToWithCharNotPresent() {
        assertEquals("Hello World", characterReader.consumeTo('z'));
    }

    @Test
    public void testConsumeToWithSequence() {
        assertEquals("Hello W", characterReader.consumeTo("orl"));
    }

    @Test
    public void testConsumeToWithSequenceNotPresent() {
        assertEquals("Hello World", characterReader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny(' ', '-'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader = new CharacterReader("Hello1");
        assertEquals("Hello1", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("a1b2c3");
        assertEquals("a1b2c3", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("12345");
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("Hell"));
        assertFalse(characterReader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("goodbye"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'e', 'l'));
        assertFalse(characterReader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertEquals(' ', characterReader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertEquals(' ', characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("WORLD"));
        assertFalse(characterReader.containsIgnoreCase("WORLD123"));
    }

    @Override
    public String toString() {
        return characterReader.toString();
    }
}