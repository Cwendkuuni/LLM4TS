package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(3, reader.pos());
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(6, reader.pos());
        assertEquals("defg", reader.consumeTo('a'));
        assertEquals('e', reader.current());
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertEquals("abcd", reader.consumeTo('a'));
        assertEquals("cdabcd", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('z'));
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeTo('z'));
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abcdefg", reader.consumeToEnd());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("defg", reader.consumeToAny(new char[]{'a', 'b'}));
        assertEquals("", reader.consumeToAny(new char[]{'}'}));
        assertEquals("abcdefg", reader.consumeToAny(new char[]{}));
        assertEquals("abc", reader.consumeToAny(new char[]{'c'}));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("defg", reader.consumeToAnySorted(new char[]{'d', 'f', 'g'}));
        assertEquals("abc", reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}));
        assertEquals("abcdefg", reader.consumeToAnySorted(new char[]{}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd&efg"));
        assertEquals("abcd", reader.consumeData());

        reader = new CharacterReader(new StringReader("abcd&efg<html>"));
        assertEquals("abcd", reader.consumeData());

        reader = new CharacterReader(new StringReader("abcd&efg<html>"));
        assertEquals("efg<html>", reader.consumeData());

        reader = new CharacterReader(new StringReader("abcd&efg<html>"));
        assertEquals("", reader.consumeData());

        reader = new CharacterReader(new StringReader("abcd&efg<html>"));
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        assertEquals("title", reader.consumeTagName());

        reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        assertEquals("", reader.consumeTagName());

        reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
        assertEquals("", reader.consumeTagName());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertEquals(3, reader.nextIndexOf('c'));
        assertEquals(6, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));

        reader = new CharacterReader(new StringReader(""));
        assertEquals(-1, reader.nextIndexOf('z'));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(6, reader.nextIndexOf("def"));
        assertEquals(-1, reader.nextIndexOf("zxy"));
    }

    @Test
    public void testMatches() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('z'));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matches('z'));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matches("def"));
        assertFalse(reader.matches("zxy"));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesIgnoreCase("efg"));
        assertFalse(reader.matchesIgnoreCase("zxy"));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesAny(new char[]{'c', 'd'}));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesAnySorted(new char[]{'d', 'c'}));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));

        reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matches(0x00));

        reader = new StringReader("");
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matches(0x00));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matchConsume("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("abc"));
        assertTrue(reader.containsIgnoreCase("abc"));
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertFalse(reader.containsIgnoreCase("xyz"));

        reader = new CharacterReader(new StringReader("abcdefg<html><head><title>Test</title></head>"));
        assertTrue(reader.containsIgnoreCase("html"));
        assertTrue(reader.containsIgnoreCase("head"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("body"));
    }
}