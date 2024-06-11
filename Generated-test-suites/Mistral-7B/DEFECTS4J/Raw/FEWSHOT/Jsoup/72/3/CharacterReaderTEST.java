package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertEquals(0, reader.pos());
        assertEquals(Character.EOF, reader.current());
        assertFalse(reader.isEmpty());

        reader.advance();
        assertEquals('f', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());

        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('f', reader.current());
        assertEquals(1, reader.pos());
        assertEquals(1, reader.nextIndexOf('f'));
        assertEquals("f", reader.consumeTo('f'));
        assertEquals("foobar", reader.toString());

        reader.consume();
        reader.consume();
        assertEquals('b', reader.current());
        assertEquals(3, reader.pos());
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals("bar", reader.consumeTo("bar"));
        assertEquals("baz", reader.consumeToEnd());
        assertEquals(6, reader.pos());
        assertEquals(6, reader.nextIndexOf('z'));
        assertEquals(5, reader.nextIndexOfAny(new char[] {'a', 'b'}));
        assertEquals("ba", reader.consumeToAny(new char[] {'a', 'b'}));
        assertFalse(reader.matches('z'));
        assertTrue(reader.matchesIgnoreCase("Z"));
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAnySorted(new char[] {'b', 'a'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertNull(reader.consumeLetterThenDigitSequence());
        assertNull(reader.consumeHexSequence());
        assertNull(reader.consumeDigitSequence());
        assertEquals(Character.EOF, reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertEquals(Character.EOF, reader.current());
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals(Character.EOF, reader.current());
        assertEquals(0, reader.pos());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());
        assertEquals(0, reader.nextIndexOf('a'));
        assertFalse(reader.matches('a'));
        assertFalse(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesAny('a'));
        assertFalse(reader.matchesAnySorted(new char[] {'a'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertNull(reader.consumeLetterThenDigitSequence());
        assertNull(reader.consumeHexSequence());
        assertNull(reader.consumeDigitSequence());
        assertEquals(Character.EOF, reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("foo") {
            @Override
            public void close() throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
    }

}