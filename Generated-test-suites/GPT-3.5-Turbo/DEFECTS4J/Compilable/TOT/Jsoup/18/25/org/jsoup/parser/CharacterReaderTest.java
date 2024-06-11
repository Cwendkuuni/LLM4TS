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
        characterReader = new CharacterReader("This is a test string.");
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
        assertEquals("This", characterReader.consumeAsString());
    }

    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
        characterReader.unconsume();
        assertEquals('h', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
        characterReader.advance();
        assertEquals("is", characterReader.consumeAsString());
    }

    @Test
    public void testMarkAndRewindToMark() {
        assertEquals('T', characterReader.consume());
        characterReader.mark();
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
        characterReader.rewindToMark();
        assertEquals('h', characterReader.consume());
        assertEquals("his", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This", characterReader.consumeTo(' '));
        assertEquals("is", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' ','.' ));
        assertEquals(" ", characterReader.consumeToAny(' ','.' ));
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
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("123AaBc");
        assertEquals("123AaBc", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("123AaBc");
        assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("t"));
        assertTrue(characterReader.matchesIgnoreCase("tHIS"));
        assertFalse(characterReader.matchesIgnoreCase("y"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader = new CharacterReader("123");
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(characterReader.matchesDigit());
        characterReader = new CharacterReader("abc");
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Th"));
        assertFalse(characterReader.matchConsume("is"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Th"));
        assertFalse(characterReader.matchConsumeIgnoreCase("is"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("tHiS"));
        assertFalse(characterReader.containsIgnoreCase("y"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
    }

}