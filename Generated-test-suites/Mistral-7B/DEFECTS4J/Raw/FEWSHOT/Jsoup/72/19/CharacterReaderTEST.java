package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesAny('a'));
        assertFalse(reader.matchesAnySorted(new char[]{'b'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertNull(reader.consume());
        assertEquals("", reader.consumeToEnd());
        assertEquals("", reader.consumeTo(""));
        assertEquals("", reader.consumeTo(Character.MIN_VALUE));
        assertEquals("", reader.consumeTo(Character.MAX_VALUE));
        assertEquals("", reader.consumeTo(new char[0]));
        assertEquals("", reader.consumeTo(new String("")));
        assertEquals("", reader.consumeTo(new String("ab")));
        assertEquals("", reader.consumeToAny());
        assertEquals("", reader.consumeToAnySorted());
        assertEquals("", reader.consumeData());
        assertEquals("", reader.consumeTagName());
        assertEquals("", reader.consumeToEnd());
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeDigitSequence());
        assertEquals(0, reader.pos());
        assertFalse(reader.isInstanceOf(CharacterReader.class));
    }

    @Test
    public void testReaderWithCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAnySorted(new char[]{'a'}));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertEquals('a', reader.consume());
        assertEquals("bc", reader.consumeToEnd());
        assertEquals("a", reader.consumeTo('a'));
        assertEquals("bc", reader.consumeTo('b'));
        assertEquals("c", reader.consumeTo('c'));
        assertEquals("ab", reader.consumeToAny());
        assertEquals("a", reader.consumeToAnySorted(new char[]{'a'}));
        assertEquals("abc", reader.consumeData());
        assertEquals("a", reader.consumeTagName());
        assertEquals("bc", reader.consumeToEnd());
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("b", reader.consumeLetterThenDigitSequence());
        assertEquals("a", reader.consumeHexSequence());
        assertEquals("1", reader.consumeDigitSequence());
        assertEquals(5, reader.pos());
        assertFalse(reader.matches('d'));
        assertFalse(reader.matches("cd"));
        assertFalse(reader.matchesIgnoreCase("CD"));
        assertFalse(reader.matchesAny('d'));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertNull(reader.consume());
    }

    @Test
    public void testReaderWithSpecialCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null\t\n\r\f "));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertFalse(reader.matches('&'));
        assertTrue(reader.matches("&"));
        assertTrue(reader.matchesIgnoreCase("&"));
        assertTrue(reader.matchesAny('&'));
        assertTrue(reader.matchesAnySorted(new char[]{'&'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertEquals('&', reader.consume());
        assertEquals("<null\t\n\r\f ", reader.consumeToEnd());
        assertEquals("", reader.consumeTo('&'));
        assertEquals("<null\t\n\r\f ", reader.consumeTo('<'));
        assertEquals("null\t\n\r\f ", reader.consumeTo('\t'));
        assertEquals("null\n\r\f ", reader.consumeTo('\n'));
        assertEquals("null\r\f ", reader.consumeTo('\r'));
        assertEquals("null\f ", reader.consumeTo('\f'));
        assertEquals(" ", reader.consumeTo(' '));
        assertEquals("", reader.consumeToAny());
        assertEquals("", reader.consumeToAnySorted());
        assertEquals("&<null\t\n\r\f ", reader.consumeData());
        assertEquals("", reader.consumeTagName());
        assertEquals("<null\t\n\r\f ", reader.consumeToEnd());
        assertEquals("", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeDigitSequence());
        assertEquals(9, reader.pos());
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("z"));
        assertFalse(reader.matchesIgnoreCase("Z"));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertNull(reader.consume());
    }
}