package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Reader() {
        char[] expectedChars = new char[]{'a', 'b', 'c'};
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        char currentChar;

        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        currentChar = reader.current();
        assertEquals('a', currentChar);
        reader.consume();
        assertEquals('b', currentChar);
        reader.consume();
        assertEquals('c', currentChar);
        reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReader_String() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        CharacterReader stringReader = new CharacterReader("abc");
        char currentChar;

        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        currentChar = reader.current();
        assertEquals('a', currentChar);
        reader.consume();
        assertEquals('b', currentChar);
        reader.consume();
        assertEquals('c', currentChar);
        reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals(4, reader.pos());

        assertEquals(0, stringReader.pos());
        assertFalse(stringReader.isEmpty());
        currentChar = stringReader.current();
        assertEquals('a', currentChar);
        stringReader.consume();
        assertEquals('b', currentChar);
        stringReader.consume();
        assertEquals('c', currentChar);
        stringReader.consume();
        assertTrue(stringReader.isEmpty());
        assertEquals(4, stringReader.pos());

        assertSame(reader, stringReader);
    }

    @Test
    public void testCharacterReader_ReaderInt() {
        char[] expectedChars = new char[]{'a', 'b', 'c'};
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 3);
        char currentChar;

        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        currentChar = reader.current();
        assertEquals('a', currentChar);
        reader.consume();
        assertEquals('b', currentChar);
        reader.consume();
        assertEquals('c', currentChar);
        reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReader_CharArray() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), "abc".toCharArray());
        char currentChar;

        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        currentChar = reader.current();
        assertEquals('a', currentChar);
        reader.consume();
        assertEquals('b', currentChar);
        reader.consume();
        assertEquals('c', currentChar);
        reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 5);
        reader.bufferUp();
        assertEquals(5, reader.bufLength);
        assertEquals(0, reader.bufPos);
        assertEquals(5, reader.bufSplitPoint);
        assertEquals(5, reader.readerPos);
        assertEquals(0, reader.bufMark);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(9, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();
        reader.consume();
        reader.consume();

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();

        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        char currentChar;

        reader.consume();

        currentChar = reader.current();
        assertEquals('b', currentChar);

        reader.consume();

        currentChar = reader.current();
        assertEquals('c', currentChar);

        reader.consume();

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();

        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();
        reader.advance();

        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();

        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals(-1, reader.nextIndexOf('d'));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals(-1, reader.nextIndexOf("def"));
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(2, reader.nextIndexOf("c"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("b", reader.consumeTo('b'));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("c", reader.consumeTo("c"));
        assertEquals("", reader.consumeTo("d"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("b", reader.consumeToAny('b'));
        assertEquals("bc", reader.consumeToAny('c'));
        assertEquals("", reader.consumeToAny('d'));
        assertEquals("ac", reader.consumeToAny(new char[]{'a', 'c'}));
        assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b', 'c'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("bc", reader.consumeToAnySorted(new char[]{'b', 'c'}));
        assertEquals("c", reader.consumeToAnySorted(new char[]{'c'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'d'}));
        assertEquals("ac", reader.consumeToAnySorted(new char[]{'a', 'c'}));
        assertEquals("abc", reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abc&def"));

        assertEquals("abc", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Hello World</title>"));

        assertEquals("/title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));

        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatches_CharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("CD"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));

        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit('3'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("cd"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertFalse(reader.matchConsumeIgnoreCase("CD"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head><body>"));

        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/HEAD"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        String cachedString;

        cachedString = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        assertEquals("abc", cachedString);

        cachedString = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos + 1);
        assertEquals("abc", cachedString);

        reader.consume();

        cachedString = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos + 1);
        assertEquals("bc", cachedString);
    }
}