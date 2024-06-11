package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals(0, reader.pos());
        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('\n', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('W', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals(' ', reader.consume());
        assertEquals(4, reader.pos());
        assertEquals('W', reader.consume());
        assertEquals(5, reader.pos());
        assertEquals('!', reader.consume());
        assertEquals(6, reader.pos());
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(6, reader.pos());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(6, reader.pos());

        CharacterReader reader2 = new CharacterReader("Hello World!");
        reader2.mark();
        reader2.consumeLetterSequence();
        reader2.rewindToMark();
        assertEquals("ell", reader2.consumeLetterSequence());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("llo", reader.consumeAsString());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("llo", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo('d'));
        assertEquals("World!", reader.consumeToEnd());
        assertEquals(11, reader.pos());
        assertNull(reader.consume());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("ll", reader.consumeToAny('l'));
        assertEquals("World!", reader.consumeToAny('!'));
        assertEquals("World!", reader.consumeToEnd());
        assertNull(reader.consume());
    }

    @Test
    public void testConsumeToAnyEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals("", reader.consumeToAny('x'));
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());
        assertNull(reader.consume());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        CharacterReader reader2 = new CharacterReader("Hello World!");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches('W'));
        assertFalse(reader.matches('!'));
        assertFalse(reader.matches('Z'));
        CharacterReader reader2 = new CharacterReader("abc");
        assertFalse(reader2.matches('b'));
        assertTrue(reader2.matches('a'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertTrue(reader.matchesIgnoreCase("world"));
        assertTrue(reader.matchesIgnoreCase("HEllo"));
        assertTrue(reader.matchesIgnoreCase("WORLD"));
        assertTrue(reader.matchesIgnoreCase(""));
        CharacterReader reader2 = new CharacterReader("abc");
        assertFalse(reader2.matchesIgnoreCase("B"));
        assertFalse(reader2.matchesIgnoreCase("aB"));
        assertTrue(reader2.matchesIgnoreCase("Abc"));
        assertTrue(reader2.matchesIgnoreCase("aBc"));
        assertTrue(reader2.matchesIgnoreCase(""));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesAny('H', 'W'));
        assertTrue(reader.matchesAny('W', 'W'));
        assertFalse(reader.matchesAny('!', 'Z'));
        CharacterReader reader2 = new CharacterReader("abc");
        assertTrue(reader2.matchesAny('a', 'b'));
        assertTrue(reader2.matchesAny('b', 'b'));
        assertFalse(reader2.matchesAny('b', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        CharacterReader reader2 = new CharacterReader("123");
        assertFalse(reader2.matchesLetter());
        assertFalse(reader2.matchesLetter());
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        CharacterReader reader2 = new CharacterReader("123");
        assertTrue(reader2.matchesDigit());
        assertTrue(reader2.matchesDigit());
        assertTrue(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("ello", reader.consume(5));
        assertEquals("ld", reader.consume(2));
        assertEquals("!", reader.consume());
        assertEquals(11, reader.pos());
        assertNull(reader.consume());

        CharacterReader reader2 = new CharacterReader("Hello World!");
        assertEquals("Hello", reader2.matchConsume("Hello"));
        assertEquals("World!", reader2.matchConsume("World!"));
        assertEquals("", reader2.matchConsume(""));
        assertEquals(0, reader2.pos());

        CharacterReader reader3 = new CharacterReader("abc");
        assertEquals("bc", reader3.matchConsume("bc"));
        assertEquals("", reader3.matchConsume("a"));
        assertEquals("", reader3.matchConsume("b"));
        assertEquals("a", reader3.matchConsume("a"));
        assertEquals("", reader3.matchConsume("c"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("ello", reader.matchConsumeIgnoreCase("Hello"));
        assertEquals("ld", reader.matchConsumeIgnoreCase("world"));
        assertEquals("", reader.matchConsumeIgnoreCase(""));
        assertEquals(0, reader.pos());

        CharacterReader reader2 = new CharacterReader("Hello World!");
        assertEquals("", reader2.matchConsumeIgnoreCase("HELLO"));
        assertEquals("", reader2.matchConsumeIgnoreCase("WORLD"));
        assertEquals("", reader2.matchConsumeIgnoreCase(""));
        assertEquals(0, reader2.pos());

        CharacterReader reader3 = new CharacterReader("Hello World!");
        assertEquals("", reader3.matchConsumeIgnoreCase("HEllo"));
        assertEquals("", reader3.matchConsumeIgnoreCase("WORLD"));
        assertEquals("", reader3.matchConsumeIgnoreCase(""));
        assertEquals(0, reader3.pos());

        CharacterReader reader4 = new CharacterReader("abc");
        assertEquals("bc", reader4.matchConsumeIgnoreCase("bc"));
        assertEquals("a", reader4.matchConsumeIgnoreCase("a"));
        assertEquals("", reader4.matchConsumeIgnoreCase("b"));
        assertEquals("b", reader4.matchConsumeIgnoreCase("b"));
        assertEquals("a", reader4.matchConsumeIgnoreCase("a"));
        assertEquals("", reader4.matchConsumeIgnoreCase("c"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertTrue(reader.containsIgnoreCase("HELLO"));
        assertTrue(reader.containsIgnoreCase("world"));
        assertTrue(reader.containsIgnoreCase(""));
        CharacterReader reader2 = new CharacterReader("Hello World!");
        assertFalse(reader2.containsIgnoreCase("HELLOo"));
        assertFalse(reader2.containsIgnoreCase("WORLD"));
        assertFalse(reader2.containsIgnoreCase("abc"));
        CharacterReader reader3 = new CharacterReader("abc");
        assertFalse(reader3.containsIgnoreCase("B"));
        assertFalse(reader3.containsIgnoreCase("aB"));
        assertTrue(reader3.containsIgnoreCase("Abc"));
        assertTrue(reader3.containsIgnoreCase("aBc"));
        assertTrue(reader3.containsIgnoreCase(""));
        CharacterReader reader4 = new CharacterReader("");
        assertFalse(reader4.containsIgnoreCase(""));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals("World!", reader.toString());

        CharacterReader reader2 = new CharacterReader("abc");
        assertEquals("", reader2.toString());

        CharacterReader reader3 = new CharacterReader("123");
        assertEquals("123", reader3.toString());
    }
}