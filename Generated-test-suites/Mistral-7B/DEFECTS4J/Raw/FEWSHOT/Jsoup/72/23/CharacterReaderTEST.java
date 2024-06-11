package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
        reader.advance();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
        reader.consume();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
        reader.advance();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
        reader.advance();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
        reader.advance();
        assertEquals(2, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals(2, reader.pos());
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
        reader.advance();
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('c');
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("ab", consumed);

        consumed = reader.consumeTo('e');
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("def", consumed);

        consumed = reader.consumeTo('z');
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        consumed = reader.consumeTo(null);
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        consumed = reader.consumeTo("");
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        consumed = reader.consumeTo("");
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        consumed = reader.consumeToAny(new char[]{'d', 'e'});
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("def", consumed);

        consumed = reader.consumeToAny(new char[]{'z', 'x'});
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'z', 'x', 'd', 'e'});
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("de", consumed);

        consumed = reader.consumeData();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcde", consumed);

        consumed = reader.consumeTagName();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        consumed = reader.consumeToEnd();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcde", consumed);

        consumed = reader.consumeLetterSequence();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abc", consumed);

        consumed = reader.consumeLetterThenDigitSequence();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcdef", consumed);

        consumed = reader.consumeHexSequence();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcdef", consumed);

        consumed = reader.consumeDigitSequence();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("012345", consumed);

        consumed = reader.consumeHexSequence();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcdef", consumed);

        consumed = reader.consumeDigitSequence();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("012345", consumed);

        reader.mark();
        consumed = reader.consumeLetterThenDigitSequence();
        reader.rewindToMark();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcdef", consumed);

        reader.mark();
        consumed = reader.consumeToAny(new char[]{'z', 'x'});
        reader.rewindToMark();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("", consumed);

        reader.mark();
        consumed = reader.consumeToAnySorted(new char[]{'z', 'x', 'd', 'e'});
        reader.rewindToMark();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("de", consumed);

        reader.mark();
        consumed = reader.consumeData();
        reader.rewindToMark();
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals("abcde", reader.toString());
        assertEquals("abcde", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('f'));

        reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('f'));

        reader = new CharacterReader(new StringReader("abcde"));
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("bc"));
        assertTrue(reader.matches("cd"));
        assertTrue(reader.matches("de"));
        assertFalse(reader.matches("ef"));

        reader = new CharacterReader(new StringReader("abcde"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("bc"));
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertTrue(reader.matchesIgnoreCase("de"));
        assertFalse(reader.matchesIgnoreCase("ef"));

        reader = new CharacterReader(new StringReader("abcde"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
        assertTrue(reader.matchesAny('d'));
        assertTrue(reader.matchesAny('e'));
        assertFalse(reader.matchesAny('f'));

        reader = new CharacterReader(new StringReader("abcde"));
        assertTrue(reader.matchesAnySorted('a'));
        assertTrue(reader.matchesAnySorted('b'));
        assertTrue(reader.matchesAnySorted('c'));
        assertTrue(reader.matchesAnySorted('d'));
        assertTrue(reader.matchesAnySorted('e'));
        assertFalse(reader.matchesAnySorted('f'));

        reader = new CharacterReader(new StringReader("abcde"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        assertEquals("abcde", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcde") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.advance();
        reader.consume();
    }

}