package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_initialization() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(5, reader.length());
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReader_consume() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        assertEquals("b", reader.consumeAsString());
        assertEquals('c', reader.current());
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("ef", reader.consumeToEnd());
        assertEquals(7, reader.pos());
        assertTrue(reader.isEmpty());

        reader.rewindToMark();
        assertEquals('c', reader.current());
        assertEquals("ef", reader.consumeToEnd());
        assertEquals(8, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_markAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efgh");

        assertEquals("abcd", reader.consumeLetterSequence());
        assertEquals("efgh", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghijklmnopqrstuvwxyz1234567890ABCDEF");

        assertEquals("12", reader.consumeHexSequence());
        assertEquals("ABCDEF", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890123456789012345678901234567890");

        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("4567890123456789012345678901234567890", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz1234567890ABCDEF");

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('!'));
        assertFalse(reader.matches("!!"));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches("gh"));
        assertTrue(reader.matchesIgnoreCase("gh"));
        assertFalse(reader.matchesIgnoreCase("GH"));
        assertFalse(reader.matchesAny('!', '1'));
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matches("xyz"));

        CharacterReader reader2 = new CharacterReader("xyz");
        assertFalse(reader.matches(reader2));
        assertFalse(reader2.matches(reader));
    }

    @Test
    public void testCharacterReader_matchConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz1234567890ABCDEF");

        assertTrue(reader.matchConsume("ab"));
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCdEfGhIjKLmNoPqRsTuVwXyZ1234567890");

        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<title>efgh</title>ijklmnopqrstuvwxyz1234567890ABCDEF");

        assertTrue(reader.containsIgnoreCase("<title>"));
        assertFalse(reader.containsIgnoreCase("TITLE"));
    }
}