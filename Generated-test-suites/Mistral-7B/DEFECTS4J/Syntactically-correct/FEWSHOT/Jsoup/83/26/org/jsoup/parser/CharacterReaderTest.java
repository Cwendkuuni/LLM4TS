package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("foo"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(0, reader.pos());

        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(' ', reader.consume()); // expected EOF, but there is a space character instead
        assertEquals(EOF, reader.consume());
        assertFalse(reader.matches('o'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeTo('o'));
        assertEquals("o", reader.consume());
        assertEquals(EOF, reader.consume());

        reader = new CharacterReader(new StringReader("foo bar baz"));
        assertEquals("foo ", reader.consumeTo(' '));
        assertEquals("bar baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertEquals("foo<", reader.consumeToAny(new char[]{'<'}));
        assertEquals("bar", reader.consumeLetterSequence());
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertEquals("foo<", reader.consumeToAnySorted(new char[]{'<'}));
        assertEquals("bar", reader.consumeLetterSequence());
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("foo&amp;bar&baz"));
        assertEquals("foo&amp;", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Foo</title></head>"));
        assertEquals("<html><head><title>", reader.consumeTagName());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertEquals(3, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(1, reader.nextIndexOf('<'));
        assertEquals(5, reader.nextIndexOf('z', 4));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertEquals(3, reader.nextIndexOf("oo"));
        assertEquals(-1, reader.nextIndexOf("zz"));
        assertEquals(1, reader.nextIndexOf("<bar>"));
        assertEquals(5, reader.nextIndexOf("baz", 4));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches('@'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<Bar>baz"));
        assertTrue(reader.matchesIgnoreCase("foo"));
        assertTrue(reader.matchesIgnoreCase("bar"));
        assertFalse(reader.matchesIgnoreCase("zaz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertTrue(reader.matchesAny(new char[]{'o', 'f'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
        assertTrue(reader.matchesAnySorted(new char[]{'o', 'f'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySortedWithEmptyArray() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertTrue(reader.matchesAnySorted(new char[0]));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('z'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("foo123<bar>baz"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertTrue(reader.matchConsume("oo"));
        assertEquals("bar", reader.consumeLetterSequence());
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("FOO<BAR>BAZ"));
        assertTrue(reader.matchConsumeIgnoreCase("oo"));
        assertEquals("BAR", reader.consumeLetterSequence());
        assertEquals("BAZ", reader.consumeToEnd());
    }

    @Test
    public void testContainIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Foo</title></head>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/head"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }
}