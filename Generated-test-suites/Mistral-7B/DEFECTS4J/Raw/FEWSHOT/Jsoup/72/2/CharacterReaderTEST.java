package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertFalse(reader.matches('a'));
        assertNull(reader.consume());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertTrue(reader.matches('a'));
        assertEquals('a', reader.consume());
        assertEquals("", reader.consumeToEnd());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
    }

    @Test
    public void testCharacterReaderMultiplyCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals("", reader.consumeToEnd());

        assertEquals('a', reader.current());
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderDifferentCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals("", reader.consumeToEnd());

        assertEquals('a', reader.current());
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        String result = reader.consumeTo('c');
        assertEquals("ab", result);
        assertEquals('d', reader.current());
        assertEquals("d", reader.consumeToEnd());

        reader = new CharacterReader(new StringReader("abcd"));
        reader.mark();
        result = reader.consumeTo('c');
        assertEquals("ab", result);
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        String result = reader.consumeToEnd();
        assertEquals("abcd", result);
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        String result = reader.consumeToAny(new char[]{'a', 'e'});
        assertEquals("a", result);
        assertEquals('b', reader.current());

        result = reader.consumeToAnySorted(new char[]{'e', 'a'});
        assertEquals("e", result);
        assertEquals('f', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("ab12cd3ef"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("ab1", result);
        assertEquals('2', reader.current());

        reader.mark();
        result = reader.consumeLetterThenDigitSequence();
        assertEquals("ab1", result);
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        String result = reader.consumeDigitSequence();
        assertEquals("12345", result);
        assertEquals('6', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x123456"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        String result = reader.consumeHexSequence();
        assertEquals("0x1234", result);
        assertEquals('5', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());

        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("xyz"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'e'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.matchesLetter());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public void mark(int readlimit) throws IOException {
                throw new IOException();
            }
        });
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        fail();
    }

}