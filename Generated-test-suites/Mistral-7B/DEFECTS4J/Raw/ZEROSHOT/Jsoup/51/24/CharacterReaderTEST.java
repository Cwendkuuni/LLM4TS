package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals("A", reader.toString());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals("", reader.consumeAsString());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertFalse(reader.isEmpty());
        assertEquals("123ABC", reader.toString());
        assertEquals('1', reader.current());
        assertEquals('1', reader.consume());
        assertEquals('2', reader.current());
        assertEquals('2', reader.consume());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals("", reader.consumeAsString());
        assertEquals(5, reader.pos());
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(0, reader.nextIndexOf('1'));
        assertEquals(1, reader.nextIndexOf('2'));
        assertEquals(2, reader.nextIndexOf('A'));
        assertEquals(3, reader.nextIndexOf('B'));
        assertEquals(4, reader.nextIndexOf('C'));
        assertEquals("123", reader.consumeTo('3'));
        assertEquals("ABC", reader.consumeToEnd());
        assertEquals("ABC", reader.consumeToAny(new char[]{'A', 'B', 'C'}));
        assertEquals("ABC", reader.consumeToAnySorted(new char[]{'B', 'A', 'C'}));
        assertEquals("123", reader.consumeData());
        assertEquals("ABC", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcABC123");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("ABC", reader.consumeLetterThenDigitSequence());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterReaderConsumeInvalidSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        reader.consumeHexSequence();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("123ABC");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('1', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("123ABC");
        assertTrue(reader.matches('1'));
        assertTrue(reader.matches("1"));
        assertTrue(reader.matchConsume("1"));
        assertFalse(reader.matches('2'));
        assertFalse(reader.matches("2"));
        assertFalse(reader.matchConsume("2"));
        assertTrue(reader.matchesIgnoreCase("1"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase("2"));
        assertFalse(reader.matchesIgnoreCase("z"));
        assertFalse(reader.containsIgnoreCase("Z"));
    }
}