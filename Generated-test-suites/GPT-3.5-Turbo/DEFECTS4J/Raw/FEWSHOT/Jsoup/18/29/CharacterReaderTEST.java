package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        String input = "Test string";
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");

        assertTrue(reader.isEmpty());

        reader = new CharacterReader("Test string");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test string");

        assertEquals('T', reader.current());

        reader.consume();
        
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test string");

        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test string");

        reader.consume();
        reader.unconsume();

        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test string");

        reader.advance();

        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Test string");

        reader.mark();

        reader.advance();

        assertEquals('e', reader.current());

        reader.rewindToMark();

        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test string");

        String result = reader.consumeAsString();

        assertEquals("T", result);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test string");

        String result = reader.consumeTo(" ");

        assertEquals("Test", result);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test string");

        String result = reader.consumeToAny(" ");

        assertEquals("Test", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Test string");

        String result = reader.consumeToEnd();

        assertEquals("Test string", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Test string");

        String result = reader.consumeLetterSequence();

        assertEquals("Test", result);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");

        String result = reader.consumeHexSequence();

        assertEquals("abc123", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");

        String result = reader.consumeDigitSequence();

        assertEquals("123", result);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.matches("Test"));
        assertFalse(reader.matches("test"));

        reader.consume();

        assertTrue(reader.matches("est"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.matchesIgnoreCase("Test"));
        assertTrue(reader.matchesIgnoreCase("test"));

        reader.consume();

        assertTrue(reader.matchesIgnoreCase("est"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.matchesAny('T', 't'));
        assertFalse(reader.matchesAny('a', 'b'));

        reader.consume();

        assertTrue(reader.matchesAny('e', 'E'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.matchesLetter());

        reader.consume();

        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test string");

        assertFalse(reader.matchesDigit());

        reader.consume();

        assertFalse(reader.matchesDigit());

        reader.consume();

        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.matchConsume("Test"));

        assertFalse(reader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.matchConsumeIgnoreCase("Test"));

        assertTrue(reader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test string");

        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test string");

        reader.consume();

        assertEquals("est string", reader.toString());
    }
}