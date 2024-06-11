package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark();
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark();
        reader.consume();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertEquals("This ", reader.consumeTo(' '));
        assertEquals("is a", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToNull() {
        CharacterReader reader = new CharacterReader("No space");
        assertEquals("No", reader.consumeTo(' '));
        assertEquals("space", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertEquals("This", reader.consumeToAny(' ', ','));
        assertEquals(" is", reader.consumeToAny(' ', ','));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertEquals("This is a test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("This is 1 test");
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals("is", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("This is #FFFF test");
        assertEquals("FFFF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("This is 123 test");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matches("This"));
        assertFalse(reader.matches("this"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matchesIgnoreCase("This"));
        assertTrue(reader.matchesIgnoreCase("this"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matchesAny('T', 't'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matchConsume("This"));
        assertEquals(' ', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.matchConsumeIgnoreCase("This"));
        assertEquals(' ', reader.current());
        assertTrue(reader.matchConsumeIgnoreCase("is"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test");
        assertTrue(reader.containsIgnoreCase("this"));
        assertFalse(reader.containsIgnoreCase("that"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("This is a test");
        reader.consume();
        assertEquals("his is a test", reader.toString());
    }
}