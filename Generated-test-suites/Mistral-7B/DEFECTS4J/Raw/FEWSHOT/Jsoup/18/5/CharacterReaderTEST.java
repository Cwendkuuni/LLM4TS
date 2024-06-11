package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals(1, reader.pos());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeAsString();
        assertEquals("ab", consumed);
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo('c');
        assertEquals("ab", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo("bc");
        assertEquals("ab", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeToAnyChar() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToAny('a', 'e');
        assertEquals("ab", consumed);
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToEnd();
        assertEquals("abcd", consumed);
        assertEquals(-1, reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
        assertEquals('1', reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc%#def");
        String consumed = reader.consumeHexSequence();
        assertEquals("%#", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'd'));
        assertFalse(reader.matchesAny('e'));
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
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        String seq = "bc";
        String consumed = reader.matchConsume(seq);
        assertEquals("bc", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String seq = "AB";
        String consumed = reader.matchConsumeIgnoreCase(seq);
        assertEquals("AB", consumed);
        assertEquals('C', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
        String seq = "</title>";
        assertTrue(reader.containsIgnoreCase(seq));
    }

    @Test
    public void testAdvanceAndMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumeAfterEndOfStream() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
    }
}