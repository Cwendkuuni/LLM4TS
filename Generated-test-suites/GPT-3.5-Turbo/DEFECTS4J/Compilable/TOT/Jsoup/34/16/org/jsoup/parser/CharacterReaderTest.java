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
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(5, characterReader.nextIndexOf('i'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(5, characterReader.nextIndexOf("is"));
        assertEquals(-1, characterReader.nextIndexOf("not"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeTo_String() {
        assertEquals("This ", characterReader.consumeTo("is"));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' ', 'a'));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", characterReader.consumeToEnd());
        assertEquals(22, characterReader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches("that"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
        assertFalse(characterReader.matchesIgnoreCase("that"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'x'));
        assertFalse(characterReader.matchesAny('Z', 'x'));
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
        assertTrue(characterReader.matchConsume("This"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("is"));
        assertFalse(characterReader.containsIgnoreCase("not"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string", characterReader.toString());
    }
}