package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_nullInput() {
        try {
            new CharacterReader(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCharacterReader_emptyInput() {
        CharacterReader reader = new CharacterReader("");

        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.pos());
        assertEquals(-1, reader.mark());
        assertNull(reader.current());
        assertNull(reader.consume());

        reader.mark();
        reader.unconsume();
        reader.advance();
        reader.rewindToMark();
        reader.consume();

        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_singleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");

        assertEquals(0, reader.pos());
        assertEquals(0, reader.mark());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(1, reader.mark());

        reader.mark();
        reader.unconsume();
        reader.advance();
        reader.rewindToMark();
        reader.consume();

        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_multipleCharactersInput() {
        CharacterReader reader = new CharacterReader("abcd");

        assertEquals(0, reader.pos());
        assertEquals(0, reader.mark());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(1, reader.mark());

        reader.mark();
        reader.unconsume();
        reader.advance();
        reader.rewindToMark();
        reader.consume();

        assertEquals('b', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());

        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        assertEquals('d', reader.current());
        assertEquals(4, reader.pos());
        assertTrue(reader.isEmpty());

        reader.consumeTo('c');

        assertEquals("ab", reader.toString());
        assertEquals(2, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abcd");

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
        assertFalse(reader.matches('e'));

        reader.consumeTo('c');

        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");

        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("d"));
        assertFalse(reader.matchesIgnoreCase("e"));

        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        assertTrue(reader.matchesIgnoreCase("d"));
        assertFalse(reader.matchesIgnoreCase("c"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("abcd");

        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('a', 'c'));
        assertTrue(reader.matchesAny('a', 'd'));
        assertTrue(reader.matchesAny('b', 'd'));
        assertFalse(reader.matchesAny('e', 'd'));

        reader.consumeTo('c');

        assertTrue(reader.matchesAny('d', 'c'));
        assertFalse(reader.matchesAny('c', 'c'));
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("", reader.consumeTo(' '));
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("cdef", reader.consumeTo('e'));
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123def");

        assertEquals("123", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");

        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_markUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.mark();
        reader.consume();
        reader.unconsume();

        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_rewindToMark() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.mark();
        reader.consume();
        reader.rewindToMark();

        assertEquals('a', reader.current());
    }
}