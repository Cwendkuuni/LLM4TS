package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("ef", reader.consumeTo('g'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeToAny('d', 'e'));
        assertEquals("f", reader.consumeToAny('g'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABc");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("c"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("d"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABc");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertFalse(reader.matchConsumeIgnoreCase("c"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a Test");
        assertTrue(reader.containsIgnoreCase("is"));
        assertFalse(reader.containsIgnoreCase("not"));
    }
}