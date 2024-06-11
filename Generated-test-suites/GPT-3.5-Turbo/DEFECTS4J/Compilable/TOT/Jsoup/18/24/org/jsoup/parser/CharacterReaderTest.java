package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Hello, World!");
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
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('e', characterReader.consume());
        characterReader.advance();
        assertEquals('l', characterReader.current());
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
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", characterReader.consumeTo(','));
        assertEquals(" World!", characterReader.consumeTo(';'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello, ", characterReader.consumeToAny('W', 'o'));
        assertEquals("W", characterReader.consumeToAny('o'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello, World!", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("1234ABC");
        assertEquals("1234ABC", characterReader.consumeHexSequence());
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("1234ABC");
        assertEquals("1234", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("h"));
        assertFalse(characterReader.matchesIgnoreCase("E"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'e', 'l'));
        assertFalse(characterReader.matchesAny('X', 'Y', 'Z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("H"));
        assertFalse(characterReader.matchConsume("e"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("h"));
        assertFalse(characterReader.matchConsumeIgnoreCase("E"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("hello"));
        assertFalse(characterReader.containsIgnoreCase("WORLD"));
    }

    @Test
    public void testToString() {
        assertEquals("Hello, World!", characterReader.toString());
    }

}