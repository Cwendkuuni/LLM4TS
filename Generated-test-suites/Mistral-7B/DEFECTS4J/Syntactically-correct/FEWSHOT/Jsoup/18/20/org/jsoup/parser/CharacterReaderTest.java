package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.isEmpty());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        String input = "a";
        CharacterReader reader = new CharacterReader(input);

        assertEquals('a', reader.consume());
        assertEquals('', reader.consumeAsString());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        String input = "hello";
        CharacterReader reader = new CharacterReader(input);

        assertEquals('h', reader.consume());
        assertEquals("ello", reader.consumeTo('l'));
        assertEquals("llo", reader.consumeTo('o'));
        assertEquals("llo", reader.consumeToAny('l', 'o'));
        assertEquals("llo", reader.consumeToEnd());
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("123", reader.consumeDigitSequence());

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeMultipleSameCharacters() {
        String input = "aaaa";
        CharacterReader reader = new CharacterReader(input);

        assertEquals('a', reader.current());
        assertEquals("aaa", reader.consumeTo('a'));
        assertEquals("aa", reader.consumeToAny('a'));
        assertEquals("aaa", reader.consumeToEnd());

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        String input = "hello world";
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matches('h'));
        assertTrue(reader.matches("el"));
        assertFalse(reader.matches("ll"));
        assertFalse(reader.matchesIgnoreCase("LL"));
        assertFalse(reader.matchesAny('b'));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        assertEquals("ello", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo("ld"));
        assertEquals("ld", reader.consumeToAny('l', 'd'));
        assertEquals("world", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        String input = "hello world";
        CharacterReader reader = new CharacterReader(input);

        reader.mark();
        assertEquals('h', reader.current());
        reader.rewindToMark();
        assertEquals('h', reader.current());

        reader.advance();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('h', reader.current());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderToString() {
        String input = "hello world";
        CharacterReader reader = new CharacterReader(input);

        assertEquals("world", reader.toString());
    }
}