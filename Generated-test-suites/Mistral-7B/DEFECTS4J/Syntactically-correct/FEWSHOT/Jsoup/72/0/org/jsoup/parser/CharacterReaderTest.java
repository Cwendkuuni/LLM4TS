package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testNewInstance() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testCurrentAndConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.advance();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(6, reader.nextIndexOf('g'));
        assertEquals(-1, reader.nextIndexOf('h'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(6, reader.nextIndexOf("g"));
        assertEquals(-1, reader.nextIndexOf("hij"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("g", reader.consumeTo('g'));
        assertEquals("", reader.consumeTo('h'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bc", reader.consumeToAny(new char[]{'b', 'c'}));
        assertEquals("g", reader.consumeToAny(new char[]{'g'}));
        assertEquals("", reader.consumeToAny(new char[]{'h'}));
        assertEquals("", reader.consumeToAny(new char[]{'_', 'h'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bc", reader.consumeToAnySorted(new char[]{'b', 'c'}));
        assertEquals("g", reader.consumeToAnySorted(new char[]{'g'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'_', 'h'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader(" \t<title>\n"));
        assertEquals("title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abcdefg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('h'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("h"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesAny('a'));
        assertFalse(reader.matchesAny('h'));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'b', 'a'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'h', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertTrue(reader.matchConsume("ab"));
        assertTrue(reader.matchConsume("bc"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcAbC"));
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("bc"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        String string1 = reader.consumeTo('a');
        String string2 = reader.consumeTo('a');
        assertNotEquals(string1, string2);
        assertEquals(string1, reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos));
        assertEquals(string2, reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos));
    }
}