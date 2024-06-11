package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_init() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(6, reader.length());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_nullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals('h', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.consume();
        reader.unconsume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("hello world");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("llo", reader.consumeAsString());
        assertEquals("rld", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("llo", reader.consumeTo('l'));
        assertEquals("rld", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("llo", reader.consumeToAny('l'));
        assertEquals("rld", reader.consumeToAny('d'));
        assertEquals("world", reader.consumeToAny('w'));
        assertEquals("", reader.consumeToAny('x'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1234abcd");
        assertEquals("1234", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches("he"));
        assertFalse(reader.matches('w'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("HELLO WORLD");
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertTrue(reader.matchesIgnoreCase("l"));
        assertFalse(reader.matchesIgnoreCase("w"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesAny('h', 'e'));
        assertTrue(reader.matchesAny('l', 'd'));
        assertFalse(reader.matchesAny('w'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("ello", reader.matchConsume("ell"));
        assertSame(reader, reader.matchConsumeIgnoreCase("HELLO"));
        assertEquals("rld", reader.matchConsume("rld"));
        assertNull(reader.matchConsume("w"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/body"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}