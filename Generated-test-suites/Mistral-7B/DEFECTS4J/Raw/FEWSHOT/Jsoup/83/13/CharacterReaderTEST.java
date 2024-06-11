package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());

        reader.advance();
        assertEquals(1, reader.pos());

        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));

        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));

        assertEquals("bc", reader.consumeTo('b'));
        assertEquals("cd", reader.consumeTo('c'));
        assertEquals("d", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('e'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));

        assertEquals("123", reader.consumeToAny(new char[]{'1', '2', '3'}));
        assertEquals("456", reader.consumeToAny(new char[]{'4', '5', '6'}));
        assertEquals("789", reader.consumeToAny(new char[]{'7', '8', '9'}));
        assertEquals("", reader.consumeToAny(new char[]{'0'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));

        assertEquals("123", reader.consumeToAnySorted(new char[]{'1', '2', '3'}));
        assertEquals("234", reader.consumeToAnySorted(new char[]{'2', '3', '4'}));
        assertEquals("456", reader.consumeToAnySorted(new char[]{'4', '5', '6'}));
        assertEquals("567", reader.consumeToAnySorted(new char[]{'5', '6', '7'}));
        assertEquals("678", reader.consumeToAnySorted(new char[]{'6', '7', '8'}));
        assertEquals("789", reader.consumeToAnySorted(new char[]{'7', '8', '9'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'0'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));

        assertEquals("&", reader.consumeData().charAt(0));
        assertEquals('<', reader.consumeData().charAt(1));
        assertEquals('\u0000', reader.consumeData().charAt(2));
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("  <title>\t<h1>Hello World</h1>"));

        assertEquals("<title>", reader.consumeTagName());
        assertEquals("<h1>", reader.consumeTagName());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));

        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));

        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));

        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAny(new char[]{'b', 'c'}));
        assertFalse(reader.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));

        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456"));

        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }
}