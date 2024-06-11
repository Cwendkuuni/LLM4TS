package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_initialization() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(5, reader.length);
    }

    @Test
    public void testCharacterReader_pos() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReader_mark() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_consume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReader_unconsume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_current() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_isEmpty() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTo() {
        String input = "abcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("efg", reader.consumeTo('g'));
        assertEquals("h", reader.current());
        reader.consume();
        assertEquals("i", reader.current());
    }

    @Test
    public void testCharacterReader_consumeTo_emptySequence() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("", reader.consumeToAny());
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_consumeTo_singleCharacter() {
        String input = "abcde";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("b", reader.consumeTo('b'));
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_consumeTo_multipleCharacters() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("cd", reader.consumeTo("de"));
        assertEquals('f', reader.current());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("de", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("23", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("23", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_matches() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        String input = "ABCDEF";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("G"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('e', 'f'));
        assertFalse(reader.matchesAny('g', 'f'));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReader_matchConsume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("ef"));
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        String input = "ABCDEF";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        String input = "ab<title>cd</title>ef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("<title>"));
    }

    @Test
    public void testCharacterReader_toString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.toString());
    }
}