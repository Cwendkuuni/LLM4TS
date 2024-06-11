package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    private static final int maxBufferLen = 1024 * 32;

    @Test
    public void testCharacterReaderInitialization() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals(maxBufferLen, reader.charBuf.length);
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.bufLength);
        assertEquals(0, reader.bufMark);
        assertEquals(0, reader.bufSplitPoint);
        assertEquals(0, reader.readerPos);
    }

    @Test
    public void testCharacterReaderWithNullInput() {
        assertNull(new CharacterReader(null, maxBufferLen));
    }

    @Test
    public void testCharacterReaderWithEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""), maxBufferLen);
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderWithSingleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("a"), maxBufferLen);
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), maxBufferLen);
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderWithReaderOnlyInput() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        reader.bufferUp();
        reader.bufferUp();
        reader.advance();
        assertEquals("f", reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(4, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(0, reader.nextIndexOf('f'));
        assertEquals(1, reader.nextIndexOf('o'));
        assertEquals(3, reader.nextIndexOf('o'));
        assertEquals("foo", reader.consumeTo("oo"));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"), maxBufferLen);
        assertEquals(-1, reader.nextIndexOf("qux"));
        assertEquals(0, reader.nextIndexOf("foo"));
        assertEquals(6, reader.nextIndexOf("bar"));
        assertEquals(9, reader.nextIndexOf("baz"));
        assertEquals("foo", reader.consumeTo("oo"));
        assertEquals("bar", reader.consumeTo("az"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals("f", reader.consumeTo('o').trim());
        assertEquals("o", reader.consumeTo('o').trim());
        assertEquals("", reader.consumeTo(EOF).trim());
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals("", reader.consumeToAny('\n'));
        assertEquals("f", reader.consumeToAny('o').trim());
        assertEquals("foo", reader.consumeToAny(new char[]{'o', 'o'}));
        assertEquals("foo", reader.consumeToAny(new char[]{'o', 'o', 'o'}));
        assertEquals("foo", reader.consumeToAny(new char[]{'o', 'o', 'o', 'o'}));
        assertEquals("foo", reader.consumeToAny(new char[]{'o'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals("", reader.consumeToAnySorted(new char[]{}));
        assertEquals("f", reader.consumeToAnySorted(new char[]{'o'}));
        assertEquals("foo", reader.consumeToAnySorted(new char[]{'o', 'o'}));
        assertEquals("foo", reader.consumeToAnySorted(new char[]{'o', 'o', 'o'}));
        assertEquals("foo", reader.consumeToAnySorted(new char[]{'o', 'o', 'o', 'o'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals("foo", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>test</title>"), maxBufferLen);
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>test</title>"), maxBufferLen);
        assertEquals("</title>", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertEquals("foo", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo123"), maxBufferLen);
        assertEquals("foo123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&#x61;&#x6C;&#x65;"), maxBufferLen);
        assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123"), maxBufferLen);
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches("foo"));
        assertFalse(reader.matches("bar"));
        reader.advance();
        assertFalse(reader.matches('f'));
        assertTrue(reader.matches("oo"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertTrue(reader.matchesIgnoreCase("foo"));
        assertTrue(reader.matchesIgnoreCase("Foo"));
        assertTrue(reader.matchesIgnoreCase("FOO"));
        assertFalse(reader.matchesIgnoreCase("bar"));
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("oo"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertTrue(reader.matchesAny('f'));
        assertTrue(reader.matchesAny('o'));
        assertFalse(reader.matchesAny('x'));
        reader.advance();
        assertTrue(reader.matchesAny('o'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertTrue(reader.matchesAnySorted(new char[]{'f', 'o'}));
        assertTrue(reader.matchesAnySorted(new char[]{'o', 'f'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
        reader.advance();
        assertTrue(reader.matchesAnySorted(new char[]{'o'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"), maxBufferLen);
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"), maxBufferLen);
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }
}