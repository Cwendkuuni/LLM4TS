package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testNewInstanceWithReader() {
        Reader reader = new StringReader("hello world");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(reader, characterReader.reader);
    }

    @Test
    public void testNewInstanceWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world"));
        assertEquals("hello world", characterReader.consumeToEnd());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        characterReader.bufferUp();
        assertEquals(6, characterReader.pos());
        characterReader.consume();
        assertEquals(7, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.isEmpty());
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        characterReader.consume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        characterReader.advance();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(5, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(5, characterReader.nextIndexOf("de"));
        assertEquals(-1, characterReader.nextIndexOf("abcd"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bcdefg", characterReader.consumeTo('f'));
        assertEquals("", characterReader.consumeTo('z'));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("de", characterReader.consumeTo("ef"));
        assertEquals("", characterReader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bcdefg", characterReader.consumeToAny(new char[]{'f'}));
        assertEquals("", characterReader.consumeToAny(new char[]{'x'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bcdefg", characterReader.consumeToAnySorted(new char[]{'f', 'd'}));
        assertEquals("", characterReader.consumeToAnySorted(new char[]{'x', 'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("\t\n\r\f   / >"));
        assertEquals("\t\n\r\f   / >", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello world"));
        assertEquals("world", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        assertEquals("abcde", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        assertEquals("abcd123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matches('b'));
        assertTrue(characterReader.matches("de"));
        assertFalse(characterReader.matches('z'));
        assertFalse(characterReader.matches("abcd"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesIgnoreCase("de"));
        assertTrue(characterReader.matchesIgnoreCase("ef"));
        assertFalse(characterReader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesAny(new char[]{'b', 'd'}));
        assertFalse(characterReader.matchesAny(new char[]{'x', 'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'b', 'd'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'x', 'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('4'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg12345"));
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchConsume("de"));
        assertEquals("efg", characterReader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchConsumeIgnoreCase("ef"));
        assertEquals("g", characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>My Title</title></html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertFalse(characterReader.containsIgnoreCase("/body"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        characterReader.consume();
    }
}