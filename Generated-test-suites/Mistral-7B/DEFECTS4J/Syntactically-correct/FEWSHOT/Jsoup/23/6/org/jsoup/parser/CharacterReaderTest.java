package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertNull(reader.consume());
    }

    @Test
    public void testBasicFunctionality() {
        String input = "hello world";
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());
        assertEquals('h', reader.current());
        assertEquals('h', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('e', reader.current());

        reader.unconsume();
        assertEquals('h', reader.current());

        reader.advance();
        assertEquals('e', reader.current());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());

        String consumed = reader.consumeAsString();
        assertEquals("llo world", reader.toString());
        assertEquals("llo", consumed);

        consumed = reader.consumeTo('l');
        assertEquals("ello world", reader.toString());
        assertEquals("llo", consumed);

        consumed = reader.consumeTo("llo");
        assertEquals("world", reader.toString());
        assertEquals("llo", consumed);

        consumed = reader.consumeToAny('l', 'o');
        assertEquals("world", reader.toString());
        assertEquals("ll", consumed);

        consumed = reader.consumeToEnd();
        assertEquals("", reader.toString());
        assertEquals("world", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);

        String sequence = reader.consumeLetterSequence();
        assertEquals("abc", sequence);
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "ab12cd34ef";
        CharacterReader reader = new CharacterReader(input);

        String sequence = reader.consumeHexSequence();
        assertEquals("12", sequence);
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "ab12cd34ef";
        CharacterReader reader = new CharacterReader(input);

        String sequence = reader.consumeDigitSequence();
        assertEquals("1234", sequence);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello world");

        assertTrue(reader.matches('h'));
        assertTrue(reader.matches("he"));
        assertFalse(reader.matches("llo"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello world");

        assertTrue(reader.matchesIgnoreCase("he"));
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("LLo"));
        assertFalse(reader.matchesIgnoreCase('z'));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello world");

        assertTrue(reader.matchesAny('h', 'e'));
        assertFalse(reader.matchesAny('l', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("hello world");

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('3'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("hello world");

        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('3'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        String sequence = reader.matchConsume("bc");
        assertEquals("bc", sequence);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");

        String sequence = reader.matchConsumeIgnoreCase("bc");
        assertEquals("bc", sequence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        new CharacterReader(null);
    }
}