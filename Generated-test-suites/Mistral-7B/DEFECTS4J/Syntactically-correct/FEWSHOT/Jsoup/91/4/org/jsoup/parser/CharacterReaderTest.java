package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("foo"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(0, reader.pos());

        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(EOF, reader.consume());

        reader = new CharacterReader(new StringReader(""));
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.current());
        reader.advance();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.advance();
        reader.advance();
        reader.advance();
        reader.consume();
        assertEquals(6, reader.pos());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(3, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(3, reader.nextIndexOf("oo"));
        assertEquals(-1, reader.nextIndexOf("zz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo('o'));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo("oo"));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeToAny(new char[]{'o'}));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeToAnySorted(new char[]{'o'}));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>"));
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        reader.bufferUp();
        reader.consume();
        assertEquals(7, reader.bufLength);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesIgnoreCase("o"));
        assertTrue(reader.matchesIgnoreCase("Foo"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAny(new char[]{'o'}));
        assertTrue(reader.matchesAny(new char[]{'f', 'o'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAnySorted(new char[]{'f', 'o'}));
        assertTrue(reader.matchesAnySorted(new char[]{'o'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter(EOF));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("foo1"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit(EOF));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchConsume("oo"));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchConsumeIgnoreCase("oo"));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        String str1 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        String str2 = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        assertEquals(str1, str2);
    }
}