package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_DefaultConstructor() {
        CharacterReader reader = new CharacterReader();
        assertSame(CharacterReader.class, reader.getClass());
        assertNull(reader.reader);
        assertEquals(CharacterReader.maxBufferLen, reader.charBuf.length);
    }

    @Test
    public void testCharacterReader_StringConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.reader instanceof StringReader);
        assertEquals(10, reader.charBuf.length);
        assertEquals(3, reader.pos());
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReader_ReaderConstructor() {
        StringReader reader = new StringReader("abcd");
        CharacterReader charReader = new CharacterReader(reader, 11);
        assertEquals(CharacterReader.class, charReader.getClass());
        assertSame(reader, charReader.reader);
        assertEquals(11, charReader.charBuf.length);
        assertEquals(0, charReader.pos());
        assertEquals('\u0000', charReader.current());
        charReader.advance();
        assertEquals('a', charReader.current());
    }

    @Test
    public void testCharacterReader_ReaderAndSizeConstructor() {
        StringReader reader = new StringReader("abcd");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals(CharacterReader.class, charReader.getClass());
        assertSame(reader, charReader.reader);
        assertEquals(CharacterReader.maxBufferLen, charReader.charBuf.length);
        assertEquals(0, charReader.pos());
        assertEquals('\u0000', charReader.current());
        charReader.advance();
        assertEquals('a', charReader.current());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        reader.bufferUp();
        assertEquals(3, reader.bufLength);
        assertEquals(3, reader.bufSplitPoint);
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.readerPos);
        assertEquals(0, reader.bufMark);
        assertEquals('a', reader.charBuf[0]);
        assertEquals('b', reader.charBuf[1]);
        assertEquals('c', reader.charBuf[2]);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.isEmpty());
        reader.mark();
        reader.rewindToMark();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        reader.advance();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(5, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        assertEquals(1, reader.nextIndexOf("ab"));
        assertEquals(5, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("bc", reader.consumeTo('b'));
        assertEquals("d", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("d", reader.consumeTo("d"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abcd", reader.consumeToAny(new char[]{'a', 'b', 'c', 'd'}));
        assertEquals("cd", reader.consumeToAny(new char[]{'c', 'd'}));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abcd", reader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        assertEquals("cd", reader.consumeToAnySorted(new char[]{'c', 'd'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;<foo>"));
        assertEquals("amp;&lt;foo&gt;", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>"));
        assertEquals("title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGh"));
        assertEquals("AbCdEfGh", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AbC123D4E5F"));
        assertEquals("AbC123D4E5F", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&#x61; &#x62; &#x63;"));
        assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testMatchesWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
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
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("bc", reader.matchConsume("bc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("bc", reader.matchConsumeIgnoreCase("bc"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
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
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String str1 = reader.cacheString(reader.charBuf, reader.stringCache, 0, 4);
        String str2 = reader.cacheString(reader.charBuf, reader.stringCache, 0, 4);
        assertSame(str1, str2);

        reader.cacheString(reader.charBuf, reader.stringCache, 0, 5);
        String str3 = reader.cacheString(reader.charBuf, reader.stringCache, 0, 4);
        assertNotSame(str1, str3);
    }
}