package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals("A", reader.consumeToEnd());
        assertEquals(1, reader.nextIndexOf('A'));
        assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals("H", reader.consumeToEnd());
        assertEquals(1, reader.nextIndexOf('H'));
        assertEquals(11, reader.nextIndexOf("World"));
        assertEquals("Hello", reader.consumeTo("l"));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf("def"));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bcde", reader.consumeTo("f"));
        assertEquals("ef", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertEquals("abcd", reader.consumeToAny('a', 'b', 'c', 'd'));
        assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("0123456789");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("ef", reader.matchConsume("ef"));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        assertEquals("ef", reader.matchConsumeIgnoreCase("ef"));
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<head>"));
        assertFalse(reader.containsIgnoreCase("<body>"));
    }
}