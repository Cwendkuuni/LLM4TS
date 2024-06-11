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
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.consumeAsString());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123ABC");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("ABC", reader.consumeLetterSequence());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123ABC%#ABC");
        assertEquals("123", reader.consumeHexSequence());
        assertEquals("ABC%", reader.consumeLetterSequence());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123ABC");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("def", reader.consumeTo('g'));
        assertEquals("efg", reader.consumeToEnd());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123ef456g");
        assertEquals("123", reader.consumeToAny('3', '6'));
        assertEquals("ef456g", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd123ef456g");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd123EF456g");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd123ef456g");
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('x', 'x'));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ab<title>cd123ef<style>456g");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<style>"));
        assertFalse(reader.containsIgnoreCase("<script>"));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.mark();
        assertEquals("abc", reader.consumeLetterSequence());
        reader.rewindToMark();
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals('f', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
}