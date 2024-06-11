package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals("Hello, World!", reader.input);
        assertEquals(13, reader.length);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        reader.advance();
        reader.advance();
        assertEquals('l', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals("ll", reader.consumeAsString());
        reader.consume();
        reader.consume();
        assertEquals("ld", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello, World!<html>");
        assertEquals(" World!", reader.consumeTo(' '));
        assertEquals("<html>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello, World!%&/@");
        assertEquals("ld!%", reader.consumeToAny('l', 'd', '%'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals("World!", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("HeLLo, WOrld!");
        assertEquals("HeLLo", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12A3B4C5D");
        assertEquals("12A3B4C5", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches('!'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertTrue(reader.matchesIgnoreCase("l"));
        assertTrue(reader.matchesIgnoreCase("ll"));
        assertFalse(reader.matchesIgnoreCase("!"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello, World!123%&/@");
        assertTrue(reader.matchesAny('l', 'd'));
        assertTrue(reader.matchesAny('l', 'd', '%'));
        assertFalse(reader.matchesAny('!'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertTrue(reader.matchesLetter());
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello, World!12345");
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello, World!<html>");
        assertTrue(reader.matchConsume("ll"));
        assertEquals(" World!", reader.input.substring(reader.pos));

        reader.reset();
        assertTrue(reader.matchConsumeIgnoreCase("html"));
        assertEquals("<html>", reader.input.substring(reader.pos));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello, World!<html><title>Test</title>");
        assertTrue(reader.containsIgnoreCase("html"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("TTT"));
    }
}