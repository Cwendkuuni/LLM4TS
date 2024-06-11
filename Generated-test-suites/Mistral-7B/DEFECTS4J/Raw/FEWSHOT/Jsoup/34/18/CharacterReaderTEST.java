package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_isEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReader_notEmpty() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals("a", reader.consume().toString());
        assertEquals("bc", reader.toString());
    }

    @Test
    public void testCharacterReader_consume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consume().toString());
        assertEquals("bc", reader.toString());
        assertEquals("b", reader.consume().toString());
        assertEquals("c", reader.consume().toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_mark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals("a", reader.current().toString());
    }

    @Test
    public void testCharacterReader_nextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(-1, reader.nextIndexOf((char) 0));
    }

    @Test
    public void testCharacterReader_nextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(5, reader.nextIndexOf("e"));
        assertEquals(-1, reader.nextIndexOf("zx"));
        assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReader_consumeTo_char() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals("bc", reader.consumeTo('c').toString());
        assertEquals("de", reader.consumeTo('e').toString());
        assertEquals("", reader.consumeTo('z').toString());
    }

    @Test
    public void testCharacterReader_consumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals("bc", reader.consumeTo("c").toString());
        assertEquals("de", reader.consumeTo("e").toString());
        assertEquals("", reader.consumeTo("z").toString());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeToAny('0', '1', '2').toString());
        assertEquals("", reader.consumeToAny('4').toString());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals("abcde", reader.consumeToEnd().toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abcde");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches(' '));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("z"));
        assertFalse(reader.matchesIgnoreCase(" "));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("abcde");
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAny(' '));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("abcde");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter(' '));
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit(' '));
    }
}