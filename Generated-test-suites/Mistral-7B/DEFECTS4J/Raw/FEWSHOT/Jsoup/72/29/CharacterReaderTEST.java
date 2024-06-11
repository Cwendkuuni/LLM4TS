package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.UncheckedIOException;
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
        assertNull(reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo('o'));
        assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeToAny(new char[]{'o'}));
        assertEquals("foo", reader.consumeToAny(new char[0]));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeToAnySorted(new char[]{'o'}));
        assertEquals("foo", reader.consumeToAnySorted(new char[]{'f'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("foo&bar"));
        assertEquals("foo", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<foo>"));
        assertEquals("foo", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123foo456"));
        assertEquals("foo", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo%#A1B2C3"));
        assertEquals("%#A1B2", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo123"));
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesIgnoreCase("o"));
        assertFalse(reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAny('o'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAnySorted(new char[]{'o'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('x'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("foo123"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('x'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.matchConsume("oo"));
        assertEquals("", reader.matchConsume("xx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.matchConsumeIgnoreCase("oo"));
        assertEquals("", reader.matchConsumeIgnoreCase("XX"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("foo") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
    }
}