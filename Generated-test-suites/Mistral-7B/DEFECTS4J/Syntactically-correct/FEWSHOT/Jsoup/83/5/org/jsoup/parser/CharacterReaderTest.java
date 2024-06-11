package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testNewCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testNewCharacterReaderWithLimit() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 2);
        assertEquals("ab", reader.toString());
    }

    @Test
    public void testNewCharacterReaderWithInput() {
        CharacterReader reader = new CharacterReader(new String("abc").toCharArray(), "abc".length());
        assertEquals("abc", reader.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNewCharacterReaderWithNullInput() {
        CharacterReader reader = new CharacterReader(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewCharacterReaderWithNegativeSize() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), -1);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""), 1);
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"), 1);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 1);
        assertEquals(0, reader.pos());

        reader.advance();
        reader.advance();
        reader.advance();
        reader.mark();
        reader.rewindToMark();

        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmptyNoBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 1);
        assertTrue(reader.isEmptyNoBufferUp());

        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.mark();
        reader.rewindToMark();

        assertFalse(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 1);
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 1);
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader.unconsume();
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 1);
        reader.advance();
        reader.advance();
        reader.advance();

        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader.rewindToMark();
        reader.advance();
        reader.advance();

        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 1);
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();

        reader.rewindToMark();
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(3, reader.nextIndexOf("cd"));
        assertEquals(-1, reader.nextIndexOf("ac"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("cd", reader.consumeTo("d"));
        assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals("bc", reader.consumeTo("c"));
        assertEquals("cd", reader.consumeTo("d"));
        assertEquals("", reader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals("", reader.consumeToAny());
        assertEquals("ab", reader.consumeToAny(
                'a', 'b'));
        assertEquals("cd", reader.consumeToAny(
                'c', 'd'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals("", reader.consumeToAnySorted());
        assertEquals("ab", reader.consumeToAnySorted('a', 'b'));
        assertEquals("cd", reader.consumeToAnySorted('c', 'd'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd&efg<hij"), 1);
        assertEquals("abcd", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<abc><def><ghi>, <jkl>"), 1);
        assertEquals("abc", reader.consumeTagName());
        assertEquals("def", reader.consumeTagName());
        assertEquals("ghi", reader.consumeTagName());
        assertEquals("jkl", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals("abcd", reader.consumeToEnd());

        reader = new CharacterReader(new StringReader("abcd<efg>"), 1);
        assertEquals("abcd<efg>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd1234efghijklmnopqrs"), 1);
        assertEquals("abcdefghijklmnopqrs", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd1234efghijklmnopqrs"), 1);
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("cd"));
        assertFalse(reader.matches("ac"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"), 1);
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertFalse(reader.matchesIgnoreCase("ac"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAnySorted('e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234"), 1);
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertTrue(reader.matchConsume("ab"));
        assertTrue(reader.matchConsume("cd"));
        assertFalse(reader.matchConsume("ac"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"), 1);
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("cd"));
        assertFalse(reader.matchConsumeIgnoreCase("ac"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>"), 1);
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/h1>"));
        assertFalse(reader.containsIgnoreCase("/head>"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        assertEquals("abcd", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"), 1);
        String str = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        assertEquals("abcd", str);

        reader.advance();
        reader.advance();
        reader.advance();

        str = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        assertEquals("cd", str);

        reader.consume();
        reader.consume();
        reader.consume();

        str = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        assertEquals("", str);
    }
}