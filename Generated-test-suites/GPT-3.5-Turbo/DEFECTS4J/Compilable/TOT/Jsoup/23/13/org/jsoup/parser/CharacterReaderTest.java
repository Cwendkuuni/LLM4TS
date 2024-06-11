package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string.");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        
        characterReader.advance();
        assertEquals(1, characterReader.pos());

        characterReader.consume();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());

        characterReader.advance();
        assertFalse(characterReader.isEmpty());

        characterReader.consume();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());

        characterReader.consume();
        assertEquals('h', characterReader.current());

        characterReader.consume();
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());

        characterReader.consume();
        characterReader.consume();
        assertEquals('s', characterReader.current());
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.consume();

        assertEquals('h', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());

        characterReader.consume();
        assertEquals("h", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("This ", characterReader.consumeTo(' '));

        characterReader.consume();
        assertEquals("is", characterReader.consumeTo(' '));

        characterReader.consume();
        assertEquals("a ", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("This i", characterReader.consumeTo("s"));

        characterReader.consume();
        assertEquals("s a te", characterReader.consumeTo("st"));

        characterReader.consume();
        assertEquals(" t", characterReader.consumeTo("r"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' ', '.'));

        characterReader.consume();
        assertEquals(" is", characterReader.consumeToAny(' ', '.'));

        characterReader.consume();
        assertEquals(" a", characterReader.consumeToAny(' ', '.'));

        characterReader.consume();
        assertEquals(" t", characterReader.consumeToAny(' ', '.'));

        characterReader.consume();
        assertEquals("s", characterReader.consumeToAny(' ', '.'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", characterReader.consumeToEnd());

        setUp();
        characterReader.advance();
        assertEquals("his is a test string.", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());

        characterReader.consume();
        assertEquals("is", characterReader.consumeLetterSequence());

        characterReader.consume();
        assertEquals("a", characterReader.consumeLetterSequence());

        characterReader.consume();
        assertEquals("test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("1234567890ABCDFabcdf");

        assertEquals("1234567890ABCDF", characterReader.consumeHexSequence());

        characterReader.consume();
        assertEquals("abcdf", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("1234567890");

        assertEquals("1234567890", characterReader.consumeDigitSequence());

        characterReader.consume();
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));

        characterReader.consume();
        assertTrue(characterReader.matches('h'));

        characterReader.consume();
        assertTrue(characterReader.matches('i'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("This"));

        characterReader.consume();
        assertTrue(characterReader.matches("is"));

        characterReader.consume();
        assertTrue(characterReader.matches("a"));

        characterReader.consume();
        assertTrue(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("tHis"));

        characterReader.consume();
        assertTrue(characterReader.matchesIgnoreCase("oIS"));

        characterReader.consume();
        assertTrue(characterReader.matchesIgnoreCase("f"));

        characterReader.consume();
        assertTrue(characterReader.matchesIgnoreCase("STRING"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'h'));

        characterReader.consume();
        assertTrue(characterReader.matchesAny('i', 's'));

        characterReader.consume();
        assertTrue(characterReader.matchesAny('a'));

        characterReader.consume();
        assertTrue(characterReader.matchesAny('t'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());

        characterReader.consume();
        assertTrue(characterReader.matchesLetter());

        characterReader.consume();
        assertTrue(characterReader.matchesLetter());

        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());

        characterReader.consume();
        assertFalse(characterReader.matchesDigit());

        characterReader.consume();
        assertFalse(characterReader.matchesDigit());

        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));

        characterReader.consume();
        assertTrue(characterReader.matchConsume("is"));

        characterReader.consume();
        assertTrue(characterReader.matchConsume("a"));

        characterReader.consume();
        assertTrue(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("tHis"));

        characterReader.consume();
        assertTrue(characterReader.matchConsumeIgnoreCase("oIS"));

        characterReader.consume();
        assertTrue(characterReader.matchConsumeIgnoreCase("f"));

        characterReader.consume();
        assertTrue(characterReader.matchConsumeIgnoreCase("STRING"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("abc"));

        characterReader = new CharacterReader("This is a TEST string.");
        assertTrue(characterReader.containsIgnoreCase("TEst"));
    }
}