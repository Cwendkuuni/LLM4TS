
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a sample string");
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
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('h', characterReader.consume());
        characterReader.unconsume();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.current());
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
        assertEquals('h', characterReader.current());
        characterReader.mark();
        characterReader.advance();
        assertEquals('i', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("h", characterReader.consumeAsString());
        assertEquals("i", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(2, characterReader.nextIndexOf('i'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_sequence() {
        assertEquals(2, characterReader.nextIndexOf("is"));
        assertEquals(-1, characterReader.nextIndexOf("not"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
        assertEquals("is", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeTo_sequence() {
        assertEquals("This ", characterReader.consumeTo("is"));
        assertEquals("is", characterReader.consumeTo("sample"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("T", characterReader.consumeToAny('i', 's'));
        assertEquals("h", characterReader.consumeToAny('i', 's'));
        assertEquals("i", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals(" ", characterReader.consumeToAnySorted(' ', 'i', 's'));
        assertEquals("T", characterReader.consumeToAnySorted('i', 's'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("This", characterReader.consumeData());
        characterReader.advance();
        assertEquals("i", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
        characterReader.advance();
        assertEquals("i", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a sample string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
        characterReader.advance();
        assertEquals("s", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        characterReader.advance();
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.advance();
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testMatches_sequence() {
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches("Zoo"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("THIS"));
        assertFalse(characterReader.matchesIgnoreCase("Zoo"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'Z'));
        assertFalse(characterReader.matchesAny('A', 'B'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 'i', 's'));
        assertFalse(characterReader.matchesAnySorted('A', 'B'));
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
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("Zoo"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("THIS"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Zoo"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("IS A"));
        assertFalse(characterReader.containsIgnoreCase("Zoo"));
    }
}
