package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("example");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());

        characterReader.consume();

        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('e', characterReader.consume());
        assertEquals('x', characterReader.consume());
        assertEquals('a', characterReader.consume());
        assertEquals('m', characterReader.consume());
        assertEquals('p', characterReader.consume());
        assertEquals('l', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();

        assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();

        assertEquals('x', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.advance();

        assertEquals('x', characterReader.current());

        characterReader.rewindToMark();

        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("exam", characterReader.consumeTo('m'));
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("example", characterReader.consumeToAny('z', 'k'));
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("example", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("example", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("example", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("example", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('e'));
        assertFalse(characterReader.matches('x'));
        assertTrue(characterReader.matches("example"));
        assertFalse(characterReader.matches("nope"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("ExAmPle"));
        assertFalse(characterReader.matchesIgnoreCase("nope"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('e', 'x'));
        assertFalse(characterReader.matchesAny('n', 'o'));
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
        assertTrue(characterReader.matchConsume("ex"));
        assertFalse(characterReader.matchConsume("no"));
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Ex"));
        assertFalse(characterReader.matchConsumeIgnoreCase("No"));
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("mpl"));
        assertFalse(characterReader.containsIgnoreCase("No"));
    }

    @Test
    public void testToString() {
        assertEquals("example", characterReader.toString());
    }
}
