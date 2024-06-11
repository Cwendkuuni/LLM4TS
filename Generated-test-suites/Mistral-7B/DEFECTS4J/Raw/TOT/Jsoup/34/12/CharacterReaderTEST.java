package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals("A", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals(0, reader.pos());
        assertEquals('1', reader.current());
        assertEquals('1', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("1", reader.consumeAsString());
        assertEquals('2', reader.current());
        // test consuming multiple characters at once
        assertEquals("23", reader.consumeTo('3'));
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(4, reader.pos());
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(10, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testReaderWithMarksAndRewinds() {
        CharacterReader reader = new CharacterReader("abab");
        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(2, reader.pos());
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals(3, reader.pos());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(2, reader.pos());
    }

    @Test(expected = IllegalStateException.class)
    public void testReaderConsumesBeyondEndOfInput() {
        CharacterReader reader = new CharacterReader("12");
        reader.consume();
        reader.consume();
        reader.consume(); // should throw exception
    }

    @Test
    public void testReaderWithScanForSingleCharacter() {
        CharacterReader reader = new CharacterReader("122a");
        assertEquals(1, reader.nextIndexOf('2'));
        assertEquals(2, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testReaderWithScanForCharSequence() {
        CharacterReader reader = new CharacterReader("12hella12worlda12");
        assertEquals(5, reader.nextIndexOf("hella".toCharArray()));
        assertEquals(11, reader.nextIndexOf("world".toCharArray()));
        assertEquals(-1, reader.nextIndexOf("zzzz".toCharArray()));
    }

    @Test
    public void testReaderWithConsumeSequence() {
        CharacterReader reader = new CharacterReader("123abc123");
        assertEquals("123", reader.consumeTo('3'));
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testReaderWithMatches() {
        CharacterReader reader = new CharacterReader("123abc123");
        assertTrue(reader.matches('1'));
        assertTrue(reader.matches("123"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("456"));
    }

    @Test
    public void testReaderWithMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("123abc123ABC");
        assertTrue(reader.matchesIgnoreCase("1"));
        assertTrue(reader.matchesIgnoreCase("123"));
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("456"));
    }

    @Test
    public void testReaderWithMatchesAny() {
        CharacterReader reader = new CharacterReader("123abc123");
        assertTrue(reader.matchesAny(new char[]{'1', '2'}));
        assertTrue(reader.matchesAny(new char[]{'1', '2', '3'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testReaderWithMatchesLetter() {
        CharacterReader reader = new CharacterReader("123abc123");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testReaderWithMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abc123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testReaderWithContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("1<title>2Hello<title>3World</title>4");
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}