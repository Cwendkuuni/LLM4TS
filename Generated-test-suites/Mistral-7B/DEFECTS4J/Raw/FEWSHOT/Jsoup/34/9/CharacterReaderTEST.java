package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_init() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(6, reader.length);
        assertEquals(0, reader.pos);
        assertEquals(-1, reader.mark);
        assertEquals('a', reader.current());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        assertEquals('b', reader.current());
        assertEquals(1, reader.pos);
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals('e', reader.current());
        assertEquals(5, reader.pos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToEnd();
        assertEquals("def", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo('d');
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo_empty() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo('g');
        assertEquals("", consumed);
        assertEquals(0, reader.pos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo_beyondEnd() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo('x');
        assertEquals("", consumed);
        assertEquals(6, reader.pos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo_match() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo("de");
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo_notMatch() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo("fg");
        assertEquals("", consumed);
        assertEquals(0, reader.pos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToAny('a', 'b');
        assertEquals("a", consumed);
        assertEquals(1, reader.pos);
        assertFalse(reader.isEmpty());
        consumed = reader.consumeToAny('x', 'y');
        assertEquals("", consumed);
        assertEquals(1, reader.pos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToAny_matchAll() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToAny('a', 'e');
        assertEquals("abcde", consumed);
        assertEquals(5, reader.pos);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcde", consumed);
        assertEquals(5, reader.pos);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", consumed);
        assertEquals(6, reader.pos);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        String input = "abc#x123def";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeHexSequence();
        assertEquals("x123", consumed);
        assertEquals(5, reader.pos);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals(4, reader.pos);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_matches() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches('g'));
        assertFalse(reader.matches("ab"));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        String input = "ABCDEF";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("AB"));
        assertFalse(reader.matchesIgnoreCase("G"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('a', 'e'));
        assertTrue(reader.matchesAny('e', 'a'));
        assertFalse(reader.matchesAny('g'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReader_mark() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos);
        assertEquals(1, reader.mark);
    }

    @Test
    public void testCharacterReader_nextIndexOf() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('g'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testCharacterReader_nextIndexOf_empty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

}