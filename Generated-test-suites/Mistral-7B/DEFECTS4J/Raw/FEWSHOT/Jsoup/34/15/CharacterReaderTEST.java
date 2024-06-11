package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReader_SingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("bc", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("he", reader.consumeAsString());
        assertEquals("llo w", reader.consumeAsString());
        assertEquals("rld", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
        assertEquals("", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_NextIndexOf() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(6, reader.nextIndexOf('l'));
        assertEquals(11, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("ello", reader.consumeTo('l'));
        assertEquals(" world", reader.consumeToEnd());
        assertEquals("hello", reader.consumeTo('h'));
        assertEquals("world", reader.consumeTo('w'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeToAny('h', 'e', 'l', 'o'));
        assertEquals("world", reader.consumeToAny('w', 'r', 'd'));
        assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_ConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("hello123 world456");
        assertEquals("hello123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a 0x2b 0x3c");
        assertEquals("0x1a 0x2b", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123 456 789");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertFalse(reader.matchesIgnoreCase("ZOO"));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesAny('h', 'e', 'l', 'o'));
        assertFalse(reader.matchesAny('z'));
    }
}