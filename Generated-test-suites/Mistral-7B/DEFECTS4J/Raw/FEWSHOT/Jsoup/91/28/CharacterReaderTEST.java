package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
        assertEquals(-1, reader.pos());
        assertEquals(0, reader.isEmptyNoBufferUp());
        assertNull(reader.stringCache);

        reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmptyNoBufferUp());
        assertEquals("", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 0));
        assertEquals("a", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 1));
        assertEquals("ab", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 2));
        assertEquals("abc", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 3));

        reader = new CharacterReader(new StringReader("abc\ndefg")); // 1024 * 32 is the maxBufferLen
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmptyNoBufferUp());
        assertEquals("a", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 1));
        assertEquals("ab", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 2));
        assertEquals("abc", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 3));
        assertEquals("abcd", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 4));
        assertEquals("abcdefg", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 7));

        reader = new CharacterReader(new StringReader("abc\ndefg"), 100);
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmptyNoBufferUp());
        assertEquals("a", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 1));
        assertEquals("ab", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 2));
        assertEquals("abc", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 3));
        assertEquals("abcd", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 4));
        assertEquals("abcdefg", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 7));

        reader = new CharacterReader(new StringReader("abc\ndefg"), 1);
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmptyNoBufferUp());
        assertEquals("a", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 1));

        reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertEquals('', reader.current());
        assertEquals('', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(true, reader.isEmptyNoBufferUp());
        assertEquals("", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 0));

        reader = new CharacterReader(new StringReader(""), 1);
        assertEquals(0, reader.pos());
        assertEquals('', reader.current());
        assertEquals('', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(true, reader.isEmptyNoBufferUp());
        assertEquals("", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 0, 0));

        reader = new CharacterReader(null);
        assertNull(reader.reader);
        assertNull(reader.charBuf);
        assertEquals(-1, reader.bufLength);
        assertEquals(-1, reader.bufSplitPoint);
        assertEquals(-1, reader.bufMark);
        assertEquals(-1, reader.bufPos);
        assertEquals(-1, reader.readerPos);
        assertEquals(null, reader.stringCache);

        try {
            reader = new CharacterReader(new StringReader(""), 0);
            reader.bufferUp();
            assertFalse(reader.isEmpty());
        } catch (IllegalArgumentException e) {
            // expected
        }

        reader = new CharacterReader(new StringReader(""), 1025 * 32);
        reader.bufferUp();
        assertEquals(-1, reader.bufPos);
        assertEquals(-1, reader.readerPos);

        reader.mark();
        reader.rewindToMark();
        assertEquals(reader.pos(), reader.bufPos);
        assertEquals(reader.pos(), reader.readerPos);

        reader.advance();
        assertFalse(reader.isEmpty());
        assertEquals(reader.pos(), reader.bufPos + 1);
        assertEquals(reader.pos(), reader.readerPos + 1);

        reader.advance();
        assertFalse(reader.isEmpty());
        assertEquals(reader.pos(), reader.bufPos + 2);
        assertEquals(reader.pos(), reader.readerPos + 2);

        reader.advance();
        assertFalse(reader.isEmpty());
        assertEquals(reader.pos(), reader.bufPos + 3);
        assertEquals(reader.pos(), reader.readerPos + 3);

        reader.advance();
        assertFalse(reader.isEmpty());
        assertEquals(reader.pos(), reader.bufPos + 4);
        assertEquals(reader.pos(), reader.readerPos + 4);

        reader.rewindToMark();
        reader.rewindToMark();
        assertEquals(reader.pos(), reader.bufPos - 1);
        assertEquals(reader.pos(), reader.readerPos - 1);

        reader.rewindToMark();
        reader.rewindToMark();
        reader.rewindToMark();
        assertEquals(reader.pos(), reader.bufPos - 2);
        assertEquals(reader.pos(), reader.readerPos - 2);
    }

    @Test
    public void testReaderPosition() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        assertEquals(reader.pos(), reader.bufPos);
        assertEquals(reader.pos(), reader.readerPos);
        assertEquals(5, reader.bufLength);
        assertEquals(4, reader.bufSplitPoint);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(0, reader.pos());
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.readerPos);
        assertEquals(6, reader.bufLength);
        reader.mark();
        reader.rewindToMark();
        assertEquals(-1, reader.bufPos);
        assertEquals(-1, reader.readerPos);
    }

    @Test
    public void testReaderWithInputString() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.bufferUp();
        assertEquals(reader.pos(), reader.bufPos);
        assertEquals(reader.pos(), reader.readerPos);
        assertEquals(6, reader.bufLength);
        assertEquals(5, reader.bufSplitPoint);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(0, reader.pos());
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.readerPos);
        assertEquals(6, reader.bufLength);
        reader.mark();
        reader.rewindToMark();
        assertEquals(-1, reader.bufPos);
        assertEquals(-1, reader.readerPos);
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("a", reader.consumeTo('a'));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("a", reader.consumeTo("a").trim());
        assertEquals("bc", reader.consumeTo("c").trim());
        assertEquals("def", reader.consumeTo("f").trim());
        assertEquals("abcdef", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeToAnyChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("abcdef", reader.consumeToAny('\u0000'));
        assertEquals("ab", reader.consumeToAny('b'));
        assertEquals("cd", reader.consumeToAny('d'));
        assertEquals("ef", reader.consumeToAny('f'));
    }

    @Test
    public void testConsumeToAnyCharSorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("ab", reader.consumeToAnySorted(new char[]{'b', 'a'}));
        assertEquals("cd", reader.consumeToAnySorted(new char[]{'d', 'c'}));
        assertEquals("ef", reader.consumeToAnySorted(new char[]{'f'}));
        assertEquals("abcdef", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("&<null", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("  <title>My Title</title>"));
        reader.bufferUp();

        assertEquals("title", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("abcdef", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals("ab", reader.consumeLetterSequence().trim());
        assertEquals("abcdefg", reader.consumeLetterThenDigitSequence().trim());
        assertEquals("123", reader.consumeDigitSequence().trim());
        assertEquals("abc123", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
        assertFalse(reader.matches("ef"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("cd"));
        assertFalse(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("eF"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertFalse(reader.matchesAny('c'));
        assertTrue(reader.matchesAny("ab"));
        assertFalse(reader.matchesAny("cd"));
        assertFalse(reader.matchesAny("ef"));
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('a', 'c'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'c', 'd'}));
        assertFalse(reader.matchesAnySorted(new char[]{'e', 'f'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        reader.bufferUp();

        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();

        assertEquals(5, reader.pos());
        reader.matchConsume("ab");
        assertEquals(7, reader.pos());
        assertEquals("cd", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 5, 2));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        reader.bufferUp();

        assertEquals(6, reader.pos());
        reader.matchConsumeIgnoreCase("ab");
        assertEquals(8, reader.pos());
        assertEquals("cd", reader.cacheString(CharacterReader.charBuf, CharacterReader.stringCache, 6, 2));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>My Title</title>"));
        reader.bufferUp();

        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/HEAD"));
        assertFalse(reader.containsIgnoreCase("/BODY"));
    }
}