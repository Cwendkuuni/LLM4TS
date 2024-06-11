package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(3, reader.pos());
        assertEquals(-1, reader.nextIndexOf('x'));
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(2, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderWithEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithNull() {
        CharacterReader reader = new CharacterReader(null);
        assertNull(reader);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abc") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('x'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("ab", reader.consumeToAny(new char[] {'a', 'b'}));
        assertEquals("cd", reader.consumeToAny(new char[] {'c', 'd'}));
        assertEquals("", reader.consumeToAny(new char[] {'x'}));
        assertEquals("", reader.consumeToAnySorted(new char[] {'x'}));
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<abc"));
        assertEquals("&<", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name=\"foo\""));
        assertEquals("<tag", reader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789ABCDEFabcdef"));
        assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertEquals("0123456789", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesIgnoreCase("aBc"));
        assertFalse(reader.matchesIgnoreCase("xYz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny(new char[] {'a', 'b', 'c'}));
        assertFalse(reader.matchesAny(new char[] {'x'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted(new char[] {'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[] {'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertTrue(reader.matchConsume("ab"));
        assertTrue(reader.matchConsume("cd"));
        assertFalse(reader.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCD"));
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertTrue(reader.matchConsumeIgnoreCase("CD"));
        assertFalse(reader.matchConsumeIgnoreCase("XY"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head><body>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/FOO"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", reader.cacheString(reader.charBuf, reader.stringCache, 0, 3));
        assertEquals("abcd", reader.cacheString(reader.charBuf, reader.stringCache, 0, 4));
        assertEquals("efg", reader.cacheString(reader.charBuf, reader.stringCache, 0, 3));
        assertEquals("hij", reader.cacheString(reader.charBuf, reader.stringCache, 0, 3));
        assertEquals("ijk", reader.cacheString(reader.charBuf, reader.stringCache, 0, 3));
    }
}