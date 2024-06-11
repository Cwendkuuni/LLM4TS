package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals(-1, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {}) {
            @Override
            public void mark(int readLimit) {
                throw new IOException();
            }
        };
        reader.mark();
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume(); // consume beyond the end, should return -1
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertFalse(reader.isEmpty());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeTo('c');
        assertEquals("ab", consumed);
        assertEquals('c', reader.current());
        reader.consume(); // consume beyond the end, should return -1
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToEnd();
        assertEquals("abcd", consumed);
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        String consumed = reader.consumeToAny(new char[]{'0', '1', '2'});
        assertEquals("012", consumed);
        reader.consume(); // consume beyond the end, should return empty string
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        String consumed = reader.consumeToAnySorted(new char[]{'1', '2', '0'});
        assertEquals("01", consumed);
        reader.consume(); // consume beyond the end, should return empty string
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String consumed = reader.consumeData();
        assertEquals("&<", consumed);
        assertEquals('>', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>My Title</title>"));
        String consumed = reader.consumeTagName();
        assertEquals("title", consumed);
        assertEquals('>', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(6, reader.nextIndexOf('g'));
        assertEquals(-1, reader.nextIndexOf('h'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefabcdef"));
        assertEquals(4, reader.nextIndexOf("def"));
        assertEquals(-1, reader.nextIndexOf("ghi"));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCD"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
        assertTrue(reader.matchesAny('d'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertTrue(reader.matchesAnySorted('1'));
        assertTrue(reader.matchesAnySorted('2'));
        assertTrue(reader.matchesAnySorted('0'));
        assertFalse(reader.matchesAnySorted('7'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('2'));
        assertFalse(reader.matchesLetter('d'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
        assertFalse(reader.matchesDigit('b'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchConsume("a"));
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('B', reader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><title>My Title</title></body></html>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/HEAD"));
    }
}