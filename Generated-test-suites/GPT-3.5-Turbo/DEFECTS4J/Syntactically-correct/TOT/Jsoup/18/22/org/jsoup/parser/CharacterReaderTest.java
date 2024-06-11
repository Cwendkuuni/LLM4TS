package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consume();
        reader.consume();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        for (int i = 0; i < 12; i++) {
            reader.consume();
        }
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        reader.consume();
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('l', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
        reader.advance();
        reader.advance();
        assertEquals(' ', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.consume();
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("He", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
        reader.advance();
        assertEquals("o", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello W", reader.consumeTo('W'));
        assertEquals("orld!", reader.consumeTo('!'));
    }

    @Test
    public void testConsumeToNotFound() {
        assertEquals("Hello World!", reader.consumeTo('?'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("H", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("ello ", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("W", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
        reader.consume();
        assertEquals("orld", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("ABC0123");
        assertEquals("ABC", reader.consumeHexSequence());
        reader.consume();
        assertEquals("0123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("0123ABC");
        assertEquals("0123", reader.consumeDigitSequence());
        reader.consume();
        assertEquals("ABC", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertFalse(reader.matches('W'));
        assertTrue(reader.matches('H'));
    }

    @Test
    public void testMatchesSequence() {
        assertFalse(reader.matches("World!"));
        assertFalse(reader.matches("Hello "));
        assertTrue(reader.matches("Hello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("WORLD!"));
        assertFalse(reader.matchesIgnoreCase("hello "));
        assertTrue(reader.matchesIgnoreCase("HeLlO"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(reader.matchesAny('a', 'e', 'i', 'o', 'u'));
        assertTrue(reader.matchesAny('H'));
    }

    @Test
    public void testMatchesLetter() {
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(reader.matchConsume("World!"));
        assertFalse(reader.matchConsume("Hello "));
        assertTrue(reader.matchConsume("Hello"));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("WORLD!"));
        assertFalse(reader.matchConsumeIgnoreCase("hello "));
        assertTrue(reader.matchConsumeIgnoreCase("HeLlO"));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("llo"));
        assertFalse(reader.containsIgnoreCase("ABC"));
    }

    @Test
    public void testToString() {
        assertEquals("Hello World!", reader.toString());
        reader.consume();
        assertEquals("ello World!", reader.toString());
    }
}