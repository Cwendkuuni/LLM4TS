package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsumeChar() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals((char) 'f', reader.consume());
        assertEquals((char) 'o', reader.consume());
        assertEquals((char) 'o', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertEquals("foo", reader.consumeTo(' '));
        assertEquals("bar", reader.consumeTo(' '));
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeStringWithDelimiter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo,bar,baz"));
        assertEquals("foo", reader.consumeTo(','));
        assertEquals("bar", reader.consumeTo(','));
        assertEquals("baz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("alpha beta gamma"));
        assertEquals("alpha", reader.consumeLetterSequence());
        assertEquals("beta", reader.consumeLetterSequence());
        assertEquals("gamma", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("alpha1 beta2 gamma3"));
        assertEquals("alpha1", reader.consumeLetterThenDigitSequence());
        assertEquals("beta2", reader.consumeLetterThenDigitSequence());
        assertEquals("gamma3", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0xFoo"));
        assertEquals("Foo", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertTrue(reader.matchConsume("foo"));
        assertEquals("bar", reader.consume());
        assertTrue(reader.matchConsume("baz"));
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("FOO bar BAZ"));
        assertTrue(reader.matchConsumeIgnoreCase("foo"));
        assertEquals("bar", reader.consume());
        assertTrue(reader.matchConsumeIgnoreCase("baz"));
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertTrue(reader.matches('o'));
        assertTrue(reader.matches("ar"));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("qux"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("FOO bar BAZ"));
        assertTrue(reader.matchesIgnoreCase("AR"));
        assertTrue(reader.matchesIgnoreCase("bar"));
        assertFalse(reader.matchesIgnoreCase("qux"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertTrue(reader.matchesAny('o', 'a'));
        assertTrue(reader.matchesAny('r', 'a'));
        assertFalse(reader.matchesAny('x', 'a'));
        assertFalse(reader.matchesAny('z', 'b'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertTrue(reader.matchesAnySorted('o', 'a', 'r'));
        assertTrue(reader.matchesAnySorted('r', 'a', 'o'));
        assertFalse(reader.matchesAnySorted('x', 'a', 'z'));
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertEquals(1, reader.nextIndexOf('o'));
        assertEquals(3, reader.nextIndexOf("ar"));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(6, reader.nextIndexOf("baz"));
    }

    @Test
    public void testNextIndexOfWithNull() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar baz"));
        assertNull(reader.nextIndexOf(null));
    }

    @Test
    public void testNextIndexOfEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testNextIndexOfEmptyStringCaseInsensitive() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(-1, reader.nextIndexOf("", true));
    }

    @Test
    public void testPositionAndEmptyStatus() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPositionAndNotEmptyStatus() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }
}