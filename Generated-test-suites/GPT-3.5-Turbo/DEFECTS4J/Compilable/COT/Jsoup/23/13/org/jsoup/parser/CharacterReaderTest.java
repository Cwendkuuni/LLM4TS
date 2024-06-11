package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertEquals('H', reader1.current());
        
        CharacterReader reader2 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader2.current());
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
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        reader.advance();
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
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("He", reader.consumeTo('l'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("H", reader.consumeToAny('e', 'l'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertEquals("123ABC", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
        assertTrue(reader.matches("Hello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesIgnoreCase("hello"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesAny('e', 'o', 'H'));
        assertFalse(reader.matchesAny('e', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsume("Hel"));
        assertFalse(reader.matchConsume("lo"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsumeIgnoreCase("HEL"));
        assertFalse(reader.matchConsumeIgnoreCase("lo"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.containsIgnoreCase("he"));
        assertFalse(reader.containsIgnoreCase("Hi"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        assertEquals("ello", reader.toString());
    }
}
