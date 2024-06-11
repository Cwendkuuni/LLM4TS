package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('e'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertEquals("01234", reader.consumeToAny(new char[]{'0', '1', '2', '3', '4'}));
        assertEquals("6789", reader.consumeToAny(new char[]{'6', '7', '8', '9'}));
        assertEquals("", reader.consumeToAny(new char[]{'0', 'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertEquals("6789", reader.consumeToAnySorted(new char[]{'6', '7', '8', '9'}));
        assertEquals("01234", reader.consumeToAnySorted(new char[]{'0', '1', '2', '3', '4'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&", reader.consumeData().charAt(0));
        assertEquals('<', reader.consumeData().charAt(0));
        assertEquals('\u0000', reader.consumeData().charAt(0));
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        reader = new CharacterReader(new StringReader(" "));
        assertFalse(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.mark();
        assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(4, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCdEFG"));
        assertEquals(4, reader.nextIndexOfIgnoreCase("d"));
        assertEquals(-1, reader.nextIndexOfIgnoreCase("z"));
    }

}