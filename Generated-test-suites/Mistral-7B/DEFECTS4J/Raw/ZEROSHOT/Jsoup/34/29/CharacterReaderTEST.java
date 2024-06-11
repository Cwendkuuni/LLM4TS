package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_SingleCharacterInput() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("A", reader.consumeToEnd());
        assertEquals('A', reader.consumeLetter());
        assertEquals(2, reader.pos());
        assertEquals("", reader.consumeLetterSequence());
        assertFalse(reader.matches('B'));
        assertTrue(reader.matches('A'));
        assertFalse(reader.matches("AB"));
        assertTrue(reader.matchesAny('A'));
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("AB"));
    }

    @Test
    public void testCharacterReader_MultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("ab123CD");;
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeTo('b'));
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals("ab1", reader.consumeTo('1'));
        assertEquals("ab12", reader.consumeTo('3'));
        assertEquals("ab123", reader.consumeToEnd());
        assertEquals("ab", reader.consumeLetterSequence());
        assertEquals("CD", reader.consumeLetterThenDigitSequence());
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("AD"));
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("Ab"));
        assertTrue(reader.containsIgnoreCase("CD"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReader_NullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcde");
        reader.mark();
        assertEquals("a", reader.consumeTo('a'));
        assertEquals(3, reader.pos());
        reader.rewindToMark();
        assertEquals("a", reader.consumeTo('a'));
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReader_ScanSequences() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("def", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReader_ScanHexSequence() {
        CharacterReader reader = new CharacterReader("abc#x12F#x456");
        assertEquals("12F", reader.consumeHexSequence());
        assertEquals("456", reader.consumeHexSequence());
    }
}