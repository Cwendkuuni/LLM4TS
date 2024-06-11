package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertNull(reader.current());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReader_singleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(1, reader.length());
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
    }

    @Test
    public void testCharacterReader_multipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(3, reader.length());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReader_consumeAndUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_markAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_advance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_nextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals(5, reader.nextIndexOf('d'));
    }

    @Test
    public void testCharacterReader_nextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcdababcdabcd");
        assertEquals(11, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReader_consumeTo_char() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeTo('d');
        assertEquals("abcd", reader.toString());
        assertEquals("cd", consumed);
    }

    @Test
    public void testCharacterReader_consumeTo_String() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeTo("cd");
        assertEquals("cd", consumed);
        assertEquals("ab", reader.toString());
    }

    @Test
    public void testCharacterReader_consumeToAny_singleCharacter() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeToAny('d');
        assertEquals("abcd", reader.toString());
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReader_consumeToAny_multipleCharacters() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeToAny('c', 'd');
        assertEquals("abcd", reader.toString());
        assertEquals("b", consumed);
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToEnd();
        assertEquals("abcd", reader.toString());
        assertEquals("cd", consumed);
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcd", reader.toString());
        assertEquals("efg", consumed);
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abcd", reader.toString());
        assertEquals("123", consumed);
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd%#efg");
        String consumed = reader.consumeHexSequence();
        assertEquals("abcd%#efg", reader.toString());
        assertEquals
                ("%25%23", consumed); // "%#" is a hex sequence for "%" character
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String consumed = reader.consumeDigitSequence();
        assertEquals("abcd123efg", reader.toString());
        assertEquals("123", consumed);
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('d'));
        assertTrue(reader.matches("cd"));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("abcd");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('1'));
    }

    @Test
    public void testCharacterReader_matchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("cd"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abCD");
        assertTrue(reader.matchConsumeIgnoreCase("CD"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abCDabcd");
        assertTrue(reader.containsIgnoreCase("CD"));
        assertTrue(reader.containsIgnoreCase("cd"));
        assertFalse(reader.containsIgnoreCase("EF"));
    }
}