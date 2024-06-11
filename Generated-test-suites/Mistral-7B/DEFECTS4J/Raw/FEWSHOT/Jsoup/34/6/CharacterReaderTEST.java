package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
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
        assertEquals("", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.mark();
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        reader.advance();
        assertEquals(1, reader.pos());
        assertEquals('B', reader.current());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
    }

    @Test
    public void testReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("A", reader.consumeAsString());
        assertEquals("B", reader.consumeAsString());
        assertEquals("C", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals(1, reader.nextIndexOf('B'));
        assertEquals(-1, reader.nextIndexOf('D'));
        assertEquals(3, reader.nextIndexOf('C'));
        assertEquals(3, reader.nextIndexOf("BC"));
        assertEquals(0, reader.nextIndexOf("AB"));
    }

    @Test
    public void testReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("A", reader.consumeTo('B'));
        assertEquals("BC", reader.consumeToEnd());
    }

    @Test
    public void testReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("AB", reader.consumeTo("BC"));
        assertEquals("C", reader.consumeToEnd());
    }

    @Test
    public void testReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals("A", reader.consumeToAny('A'));
        assertEquals("BC", reader.consumeToAny('C'));
        assertEquals("", reader.consumeToAny('D'));
    }

    @Test
    public void testReaderMatches() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches("A"));
        assertFalse(reader.matches('B'));
        assertFalse(reader.matches("B"));
        assertFalse(reader.matches('D'));
        assertFalse(reader.matches("D"));
    }

    @Test
    public void testReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase("B"));
        assertFalse(reader.matchesIgnoreCase("c"));
        assertFalse(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesAny('A'));
        assertTrue(reader.matchesAny('B'));
        assertTrue(reader.matchesAny('C'));
        assertFalse(reader.matchesAny('D'));
    }

    @Test(expected = IllegalStateException.class)
    public void testReaderAfterEndOfInput() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("aBcD");
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("Bc", reader.consumeLetterSequence());
        assertEquals("D", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("a1B2c3D");
        assertEquals("a1", reader.consumeLetterThenDigitSequence());
        assertEquals("B2c3", reader.consumeLetterThenDigitSequence());
        assertEquals("D", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x0F");
        assertEquals("&x0F", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }
}