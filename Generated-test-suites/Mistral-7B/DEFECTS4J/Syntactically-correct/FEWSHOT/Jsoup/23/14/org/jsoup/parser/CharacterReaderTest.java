package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReader_SingleCharacterInput() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacterInput() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.consume());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals("BC", reader.consumeTo('C'));
        assertEquals('D', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals("ABCD", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdABCeFG");
        assertEquals("DCeFG", reader.consumeToIgnoreCase("dcEFG"));
        assertEquals("ABC", reader.consumeLetterSequence());
        assertEquals("efg", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeAny() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("1", reader.consumeAny('1'));
        assertEquals("2", reader.consumeAny('2'));
        assertEquals("3", reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('g'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches("gh"));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'g'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesIgnoreCase("ABC"));
        assertTrue(reader.containsIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("efg"));
        assertTrue(reader.matchesIgnoreCase("Ef"));
        assertTrue(reader.matches("ef"));
        assertTrue(reader.matches("EF"));
        assertTrue(reader.matches("efg"));
        assertTrue(reader.matches("Efg"));
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        assertEquals("ab", reader.consumeTo('c'));
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
}