package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test String");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader.advance();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.advance();
        assertEquals('s', characterReader.current());
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
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        assertEquals("s", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(2, characterReader.nextIndexOf("st"));
        assertEquals(-1, characterReader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test", characterReader.consumeTo(' '));
        assertEquals("String", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToWithSequence() {
        assertEquals("Test", characterReader.consumeTo(" "));
        assertEquals("String", characterReader.consumeTo(" "));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny(' '));
        assertEquals("S", characterReader.consumeToAny(' '));
        assertEquals("tring", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
        characterReader.advance();
        assertEquals("String", characterReader.consumeLetterThenDigitSequence());
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
        assertFalse(characterReader.matches('S'));
    }

    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("Test1"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertFalse(characterReader.matchesIgnoreCase("test1"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'S'));
        assertFalse(characterReader.matchesAny('S', 'E'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("Test1"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("test1"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("St"));
        assertFalse(characterReader.containsIgnoreCase("tS"));
    }

    @Test
    public void testToString() {
        assertEquals("Test String", characterReader.toString());
    }
}