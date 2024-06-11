package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_isEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReader_singleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('', reader.consumeAsString());
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('b'));
    }

    @Test
    public void testCharacterReader_multipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(4, reader.pos());
        assertEquals("ab", reader.consumeTo("ab"));
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReader_markAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_nextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testCharacterReader_nextIndexOf_String() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(2, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("zx"));
    }

    @Test
    public void testCharacterReader_consumeTo_char() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("abc", reader.consumeTo('a'));
        assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testCharacterReader_consumeTo_String() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("bc", reader.consumeTo("c"));
        assertEquals("abc", reader.consumeTo("a"));
        assertEquals("", reader.consumeTo("x"));
    }

    @Test
    public void testCharacterReader_consumeToAny_charArray() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("2345", reader.consumeToAny('3', '4', '5'));
        assertEquals("", reader.consumeToAny('0', '1', 'z'));
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123eF");
        assertEquals("abcd", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123eF");
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_matchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("d"));
        assertFalse(reader.matchConsume("e"));
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertTrue(reader.matchConsumeIgnoreCase("D"));
        assertFalse(reader.matchConsumeIgnoreCase("e"));
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><title>Test</title></html>");
        assertTrue(reader.containsIgnoreCase("/title/"));
        assertTrue(reader.containsIgnoreCase("/TITLE/"));
        assertFalse(reader.containsIgnoreCase("/style/"));
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_inputIsNull() {
        new CharacterReader(null);
    }
}