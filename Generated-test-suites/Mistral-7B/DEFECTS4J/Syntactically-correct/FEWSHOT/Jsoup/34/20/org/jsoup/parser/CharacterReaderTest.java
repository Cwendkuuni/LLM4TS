package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        assertEquals("A", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals("ab", reader.consumeAsString(2));
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        assertEquals("abc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals("a", reader.consumeAsString());
        assertEquals("a", reader.consumeAsString());
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(5, reader.nextIndexOf('f'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(4, reader.nextIndexOf('e'));
    }

    @Test
    public void testCharacterReaderConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bcd", reader.consumeTo('d'));
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo("cd"));
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("2345", reader.consumeToAny('3', '5'));
        assertEquals("", reader.consumeToAny('x', 'y'));
        assertEquals("90", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("0123456789");
        assertTrue(reader.matchesAny('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        assertFalse(reader.matchesAny('0', 'x', '9'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("0123456789");
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
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertEquals("abcd", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123DEF");
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123def456GHIJ");
        assertEquals("123456", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
    }
}