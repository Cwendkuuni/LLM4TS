package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_ReaderInput() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals("abcdefg", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_StringInput() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), "abcdefg".length());
        assertEquals("abcdefg", reader.consumeToEnd());
    }

    @Test
    public void testBufferUp() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        reader.bufferUp();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertFalse(reader.isEmpty());

        reader.bufferUp();
        reader.bufferUp();
        assertTrue(reader.isEmpty());

        reader.advance();
        assertFalse(reader.isEmpty());

        reader.rewindToMark();
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertNull(reader.consume());
    }

    @Test
    public void testUnconsume() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf_char() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals(4, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('h'));
    }

    @Test
    public void testNextIndexOf_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals(3, reader.nextIndexOf("cd"));
        assertEquals(-1, reader.nextIndexOf("xy"));
    }

    @Test
    public void testConsumeTo_char() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("efg", reader.consumeTo(CharacterReader.EOF));
    }

    @Test
    public void testConsumeTo_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals("cd", reader.consumeTo("cd"));
        assertEquals("efg", reader.consumeTo(Character.class.getName()));
    }

    @Test
    public void testConsumeToAny_charArray() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals("bcde", reader.consumeToAny(new char[]{'b', 'c', 'd', 'e'}));
        assertEquals("", reader.consumeToAny(new char[]{'h', 'i'}));
    }

    @Test
    public void testConsumeToAnySorted_charArray() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals("defg", reader.consumeToAnySorted(new char[]{'d', 'e', 'f', 'g'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'a', 'b'}));
    }

    @Test
    public void testConsumeData() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertEquals("ab&lt;", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader(" <title>Hello World</title> "), charBuf.length);
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test(expected = UncheckedIOException.class)
    public void testConsumeToEnd_IOException() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abc") {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        }, charBuf.length);
        reader.consumeToEnd();
    }

    @Test
    public void testConsumeLetterSequence() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefgABCDEFG"), charBuf.length);
        assertEquals("abcdefgABCDEFG", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"), charBuf.length);
        assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abc%#xdef%#x6"), charBuf.length);
        assertEquals("abc%#xdef%#x6", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"), charBuf.length);
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('h'));
    }

    @Test
    public void testMatches_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("cd"));
        assertFalse(reader.matches("xy"));
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("CD"));
        assertFalse(reader.matchesIgnoreCase("XY"));
    }

    @Test
    public void testMatchesAny_charArray() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'d', 'e'}));
        assertFalse(reader.matchesAny(new char[]{'h', 'i'}));
    }

    @Test
    public void testMatchesAnySorted_charArray() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matchesAnySorted(new char[]{'d', 'e', 'f', 'g'}));
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
    }

    @Test
    public void testMatchesLetter() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefgABCDEFG"), charBuf.length);
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
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
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"), charBuf.length);
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
    public void testMatchConsume_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cd", reader.matchConsume("cd"));
        assertFalse(reader.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeIgnoreCase_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), charBuf.length);
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("CD"));
        assertFalse(reader.matchConsumeIgnoreCase("XY"));
    }

    @Test
    public void testContainsIgnoreCase_String() {
        char[] charBuf = new char[256];
        CharacterReader reader = new CharacterReader(new StringReader("<title>Hello World</title>"), charBuf.length);
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("XYZ"));
    }
}