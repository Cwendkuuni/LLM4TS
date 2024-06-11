package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello world");
        String expected = "hello";
        assertEquals(expected, reader.consumeAsString());
        expected = " world";
        assertEquals(expected, reader.consumeAsString());
        assertEquals(-1, reader.consumeAsString().length());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("1234ab5cd");
        String expected = "234";
        assertEquals(expected, reader.consumeTo('3'));
        expected = "5cd";
        assertEquals(expected, reader.consumeTo('d'));
        expected = "ab5cd";
        assertEquals(expected, reader.consumeTo('e'));
        assertEquals(-1, reader.consumeTo('z').length());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234ab5cd");
        String expected = "234";
        assertEquals(expected, reader.consumeToAny('3'));
        expected = "5cd";
        assertEquals(expected, reader.consumeToAny('d'));
        expected = "ab5cd";
        assertEquals(expected, reader.consumeToAny('e'));
        expected = "";
        assertEquals(expected, reader.consumeToAny('z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("1234ab5cd");
        String expected = "cd";
        assertEquals(expected, reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String expected = "abc";
        assertEquals(expected, reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234ab5cd");
        String expected = "1234";
        assertEquals(expected, reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234ab5cd");
        String expected = "1234";
        assertEquals(expected, reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        CharacterReader reader2 = new CharacterReader("abcdef");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        assertEquals('a', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
}