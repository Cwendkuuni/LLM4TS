
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.consume());
        reader.advance();
        assertEquals('e', reader.consume());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        assertEquals('H', reader.consume());
        reader.rewindToMark();
        assertEquals('H', reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello", reader.consumeTo(' '));
        assertEquals("World", reader.consumeTo(' '));
        assertEquals("", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello", reader.consumeToAny(' ', 'W'));
        assertEquals(" ", reader.consumeToAny('W'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123 World");
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123ABC", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches("World"));
        reader.consume();
        assertTrue(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("hELLO"));
        assertFalse(reader.matchesIgnoreCase("Hello"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny('H', 'W'));
        assertFalse(reader.matchesAny('W'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("World"));
        assertTrue(reader.matchConsumeWhitespace());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsumeIgnoreCase("hELLO"));
        assertFalse(reader.matchConsumeIgnoreCase("World"));
        assertTrue(reader.matchConsumeIgnoreCase(" world"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.containsIgnoreCase("hELLO"));
        assertFalse(reader.containsIgnoreCase("WORLD"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());
        reader.consume();
        assertEquals("ello World", reader.toString());
    }
}
