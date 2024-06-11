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
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals('H', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
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
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("H", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals(2, reader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader("Hello, world!");
        assertEquals(6, reader.nextIndexOf(", "));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello, world!");
        assertEquals("Hello", reader.consumeTo(','));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader("Hello, world!");
        assertEquals("Hello", reader.consumeTo(", "));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello, world!");
        assertEquals("Hello", reader.consumeToAny(','));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("Hello, world!");
        assertEquals("Hello", reader.consumeToAnySorted(',', ' '));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("Hello &amp; world!");
        assertEquals("Hello ", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("Hello <tag>");
        assertEquals("Hello", reader.consumeTagName());
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
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("FFAE");
        assertEquals("FFAE", reader.consumeHexSequence());
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
    }

    @Test
    public void testMatchesWithSequence() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matches("Hell"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesIgnoreCase("HELLO"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesAny('H', 'W'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesAnySorted('H', 'W'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsume("Hello"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.matchConsumeIgnoreCase("HELLO"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        assertTrue(reader.containsIgnoreCase("HELLO"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        assertEquals("Hello", reader.toString());
    }
}