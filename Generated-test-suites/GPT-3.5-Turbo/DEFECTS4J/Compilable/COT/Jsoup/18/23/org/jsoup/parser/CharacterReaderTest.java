package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        reader = new CharacterReader("Hello");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        reader.consume();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("H", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals("Hello", reader.consumeTo(','));
        assertEquals(" World!", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello, [World]");
        assertEquals("Hello, [", reader.consumeToAny(',', ']'));
        assertEquals("World]", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1A3B5F");
        assertEquals("1A3B5F", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        assertEquals("123456", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("world"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesAny('H', 'e'));
        assertFalse(reader.matchesAny('e', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello123");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello, World");
        assertTrue(reader.matchConsume("Hello"));
        assertFalse(reader.matchConsume("World"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello, World");
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
        assertFalse(reader.matchConsumeIgnoreCase("world"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello, World");
        assertTrue(reader.containsIgnoreCase("Hello"));
        assertFalse(reader.containsIgnoreCase("World"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.toString());
    }
}