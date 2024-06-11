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
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
        assertEquals('s', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('h', characterReader.current());
        characterReader.advance();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        assertEquals('i', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("h", characterReader.consumeAsString());
        assertEquals("i", characterReader.consumeAsString());
        assertEquals("s", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(2, characterReader.nextIndexOf('i'));
        assertEquals(4, characterReader.nextIndexOf(' '));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(2, characterReader.nextIndexOf("is"));
        assertEquals(1, characterReader.nextIndexOf("his"));
        assertEquals(-1, characterReader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Th", characterReader.consumeTo('i'));
        assertEquals("is ", characterReader.consumeTo('t'));
        assertEquals(" a ", characterReader.consumeTo('s'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("is ", characterReader.consumeTo("test"));
        assertEquals("Th", characterReader.consumeTo("is a"));
        assertEquals(" ", characterReader.consumeTo("string"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This ", characterReader.consumeToAny('i', 's'));
        assertEquals("is ", characterReader.consumeToAny('z', 'a'));
        assertEquals(" a ", characterReader.consumeToAny('z', 'r'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Thi", characterReader.consumeToAnySorted('i', 's'));
        assertEquals("s ", characterReader.consumeToAnySorted('a', 'z'));
        assertEquals("a ", characterReader.consumeToAnySorted('r', 'z'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("This", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Th", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
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
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches('t'));
        assertFalse(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
        assertFalse(characterReader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'h'));
        assertFalse(characterReader.matchesAny('t', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 'a'));
        assertFalse(characterReader.matchesAnySorted('t', 'e'));
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
        assertTrue(characterReader.matchConsume("This"));
        assertFalse(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertFalse(characterReader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("This"));
        assertFalse(characterReader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string", characterReader.toString());
    }
}