package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals('D', reader.current());
        assertEquals('D', reader.consume());
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABCD");
        reader.mark();
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(4, reader.pos());
        assertEquals('D', reader.current());
        reader.rewindToMark();
        assertEquals(3, reader.pos());
        assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals("AB", reader.consumeAsString());
        assertEquals("CD", reader.consumeAsString());
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals(3, reader.nextIndexOf('C'));
        assertEquals(7, reader.nextIndexOf('E'));
        assertEquals(-1, reader.nextIndexOf('F'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals("BC", reader.consumeTo('C'));
        assertEquals("D", reader.consumeTo('d'));
        assertEquals("ABCD", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BCDE", reader.consumeTo("DE"));
        assertEquals("EF", reader.consumeTo("F"));
        assertEquals("ABCDEF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertEquals("BC", reader.consumeToAny('B', 'C'));
        assertEquals("EF", reader.consumeToAny('E', 'F'));
        assertEquals("ABCDEF", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("AB"));
        assertFalse(reader.matches("CD"));
        assertFalse(reader.matches("EF"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("AbCd"));
        assertFalse(reader.matchesIgnoreCase("Ef"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesAny('A', 'B'));
        assertTrue(reader.matchesAny('B', 'C'));
        assertTrue(reader.matchesAny('D', 'D'));
        assertFalse(reader.matchesAny('E', 'F'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit('A'));
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchConsume("AB"));
        assertEquals("CD EF", reader.toString());
        assertFalse(reader.matchConsume("EF"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("cd ef", reader.toString());
        assertFalse(reader.matchConsumeIgnoreCase("ef"));
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ<title><script>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/script"));
    }
}