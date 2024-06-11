package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(0, reader.pos());
        assertEquals('h', reader.current());
        assertEquals('h', reader.consume());
        assertEquals(1, reader.pos());
        assertNull(reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('e', reader.current());
        // test empty
        CharacterReader emptyReader = new CharacterReader("");
        assertEquals(0, emptyReader.pos());
        assertNull(emptyReader.consume());
        assertEquals(0, emptyReader.pos());
        // test end of string
        CharacterReader endReader = new CharacterReader("hello world");
        endReader.pos = endReader.length;
        assertNull(endReader.consume());
        assertEquals(length, endReader.pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("hello world");
        CharacterReader emptyReader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertTrue(emptyReader.isEmpty());
        reader.pos = 1;
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(6, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("foo"));
        CharacterReader emptyReader = new CharacterReader("");
        assertEquals(-1, emptyReader.nextIndexOf('x'));
        CharacterReader endReader = new CharacterReader("hello world");
        endReader.pos = endReader.length;
        assertEquals(-1, endReader.nextIndexOf('x'));
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("h", reader.consumeAsString());
        assertEquals("ello world", reader.consumeToEnd());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("llo", reader.consumeTo('l'));
        assertEquals("world", reader.consumeToEnd());
        assertEquals("", reader.consumeToAny('z'));
        assertEquals("lo", reader.consumeToAnySorted(new char[]{'l', 'o'}));
        assertEquals("world", reader.consumeData());
        assertEquals(" title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("&lt;foo&gt;bar&amp;baz");
        assertEquals("foo", reader.consumeToAny(new char[]{'f', 'o'}));
        assertEquals("baz", reader.consumeToAnySorted(new char[]{'a', 'b', 'z'}));
        assertEquals("", reader.consumeToAny(new char[]{'x'}));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello", reader.consumeLetterSequence());
        assertEquals("world", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches("he"));
        assertFalse(reader.matches("o"));
        assertFalse(reader.matches("1"));
        CharacterReader emptyReader = new CharacterReader("");
        assertFalse(emptyReader.matches('x'));
        CharacterReader endReader = new CharacterReader("hello world");
        endReader.pos = endReader.length;
        assertFalse(endReader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesIgnoreCase("he"));
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertFalse(reader.matchesIgnoreCase("O"));
        assertFalse(reader.matchesIgnoreCase("1"));
        CharacterReader emptyReader = new CharacterReader("");
        assertFalse(emptyReader.matchesIgnoreCase("X"));
        CharacterReader endReader = new CharacterReader("hello world");
        endReader.pos = endReader.length;
        assertFalse(endReader.matchesIgnoreCase("X"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello world", reader.toString());
        CharacterReader emptyReader = new CharacterReader("");
        assertEquals("", emptyReader.toString());
    }
}