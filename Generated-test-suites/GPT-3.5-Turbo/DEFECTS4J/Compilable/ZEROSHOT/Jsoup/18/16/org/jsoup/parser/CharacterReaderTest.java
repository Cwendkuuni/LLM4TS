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
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());
        CharacterReader reader2 = new CharacterReader("Hello World");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals((char) -1, reader1.current());
        CharacterReader reader2 = new CharacterReader("Hello World");
        assertEquals('H', reader2.current());
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
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
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
        assertEquals("Hello ", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello", reader.consumeToAny(' ', 'o'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('W'));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny('H', 'W'));
        assertFalse(reader.matchesAny('A', 'B'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("Hello World");
        assertTrue(reader1.matchesLetter());
        CharacterReader reader2 = new CharacterReader("123");
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("Hello World");
        assertFalse(reader1.matchesDigit());
        CharacterReader reader2 = new CharacterReader("123");
        assertTrue(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsume("He"));
        assertFalse(reader.matchConsume("Wo"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsumeIgnoreCase("HE"));
        assertFalse(reader.matchConsumeIgnoreCase("WO"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.containsIgnoreCase("HeLlO"));
        assertFalse(reader.containsIgnoreCase("WoRlD"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());
    }
}