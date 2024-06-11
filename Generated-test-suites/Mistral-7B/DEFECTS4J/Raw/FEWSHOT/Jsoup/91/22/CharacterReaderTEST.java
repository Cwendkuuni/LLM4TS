package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('f', reader.current());
        reader.advance();
        assertEquals('o', reader.current());
        reader.advance();
        assertEquals('o', reader.current());
        assertEquals(3, reader.pos());

        reader.consume();
        assertEquals('o', reader.current());
        assertEquals(4, reader.pos());

        reader.unconsume();
        assertEquals('o', reader.current());
        assertEquals(4, reader.pos());

        reader.mark();
        reader.rewindToMark();
        assertEquals('o', reader.current());
        assertEquals(3, reader.pos());

        reader.consumeTo('o');
        assertEquals("oo", reader.consumeToEnd());
        assertEquals(6, reader.pos());

        reader.consumeTo("bar");
        assertEquals("bar", reader.consumeToEnd());
        assertEquals(10, reader.pos());

        reader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("ba", reader.consumeToEnd());
        assertEquals(10, reader.pos());

        reader.consumeToAnySorted(new char[]{'b', 'a'});
        assertEquals("ab", reader.consumeToEnd());
        assertEquals(10, reader.pos());

        reader.consumeData();
        assertEquals("&<null", reader.consumeToEnd());
        assertEquals(9, reader.pos());

        reader.consumeTagName();
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());

        reader.consumeToEnd();
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());

        reader.consumeLetterSequence();
        assertEquals("abcdef", reader.consumeToEnd());
        assertEquals(10, reader.pos());

        reader.consumeLetterThenDigitSequence();
        assertEquals("ab1cd2ef", reader.consumeToEnd());
        assertEquals(13, reader.pos());

        reader.consumeHexSequence();
        assertEquals("1234567890abcdef", reader.consumeToEnd());
        assertEquals(16, reader.pos());

        reader.consumeDigitSequence();
        assertEquals("1234567890", reader.consumeToEnd());
        assertEquals(10, reader.pos());

        reader.matches('o');
        assertTrue(reader.matches("oo"));
        assertFalse(reader.matches("ab"));

        reader.matchesIgnoreCase("AB");
        assertTrue(reader.matchesIgnoreCase("oo"));
        assertFalse(reader.matchesIgnoreCase("ab"));

        reader.matchesAny('o');
        assertTrue(reader.matchesAny(new char[]{'o', 'o'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'b'}));

        reader.matchesAnySorted(new char[]{'b', 'a'});
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'c', 'b'}));

        reader.matchesLetter();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());

        reader.matchesDigit();
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());

        reader.matchConsume("oo");
        assertEquals(6, reader.pos());

        reader.matchConsumeIgnoreCase("OO");
        assertEquals(6, reader.pos());

        reader.containsIgnoreCase("ba");
        assertTrue(reader.containsIgnoreCase("ba"));
        assertFalse(reader.containsIgnoreCase("ab"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream("foo".getBytes());
        CharacterReader readerStream = new CharacterReader(new InputStreamReader(inputStream));
        assertEquals("foo", readerStream.consumeToEnd());
        assertEquals(6, readerStream.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        reader.consume();
    }

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());
        reader.consume();
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());
    }

}