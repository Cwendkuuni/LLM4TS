package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('h', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('h', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello");
        reader.consume();
        reader.unconsume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("hello");
        reader.advance();
        reader.mark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("hello");
        reader.advance();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("h", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello ", reader.consumeTo(' '));
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToIgnoreCase() {
        CharacterReader reader = new CharacterReader("HELLO world");
        assertEquals("HELLO", reader.consumeToIgnoreCase(" "));
        assertEquals(" world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello < world");
        assertEquals("hello <", reader.consumeToAny(' ', '<'));
        assertEquals(" world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("hello", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("helLo");
        assertEquals("helLo", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("AB1z");
        assertEquals("AB1", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello");
        assertTrue(reader.matches('h'));
        assertFalse(reader.matches('e'));

    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("HELLO");
        assertTrue(reader.matchesIgnoreCase("he"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello");
        assertTrue(reader.matchesAny('h', 'l'));
        assertFalse(reader.matchesAny('e', 'w'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("hello");
        assertTrue(reader.matchConsume("he"));
        assertEquals('l', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("HELLO");
        assertTrue(reader.matchConsumeIgnoreCase("he"));
        assertEquals('L', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello");
        assertTrue(reader.containsIgnoreCase("he"));
    }

    @Override
    @Test
    public String toString() {
        CharacterReader reader = new CharacterReader("hello");
        assertEquals("hello", reader.toString());
    }
}