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

        reader = new CharacterReader(new StringReader("foo\nbar"));
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

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.current());

        reader.advance();
        assertEquals('o', reader.current());

        reader.consume();
        assertEquals('o', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.consume();
        assertEquals('o', reader.current());

        reader.advance();
        reader.consume();
        reader.consume();
        assertEquals('r', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();
        reader.unconsume();
        reader.unconsume();
        assertEquals('f', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('r', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('f', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(3, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('z'));

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals(4, reader.nextIndexOf('b'));
        assertEquals(5, reader.nextIndexOf('r'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(3, reader.nextIndexOf("oo"));
        assertEquals(-1, reader.nextIndexOf("zz"));

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals(4, reader.nextIndexOf("bar"));
        assertEquals(5, reader.nextIndexOf("r"));
        assertEquals(-1, reader.nextIndexOf("zz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo('o'));
        assertEquals("r", reader.current());

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals("bar", reader.consumeTo("ar"));
        assertEquals("r", reader.current());
    }

    @Test
    public void testConsumeToWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo("oo"));
        assertEquals("r", reader.current());

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals("bar", reader.consumeTo("ar"));
        assertEquals("r", reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("o", reader.consumeToAny(new char[]{'o'}));
        assertEquals("r", reader.current());

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals("bar", reader.consumeToAny(new char[]{'a', 'r'}));
        assertEquals("r", reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("o", reader.consumeToAnySorted(new char[]{'o'}));
        assertEquals("r", reader.current());

        reader = new CharacterReader(new StringReader("foobar"));
        assertEquals("bar", reader.consumeToAnySorted(new char[]{'a', 'r'}));
        assertEquals("r", reader.current());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;foo&gt;"));
        assertEquals("&lt;foo&gt;", reader.consumeData());
        assertEquals(Character.MIN_VALUE, reader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>foo</title>"));
        assertEquals("<title>", reader.consumeTagName());
        assertEquals("/", reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeToEnd());
        assertEquals(Character.MIN_VALUE, reader.current());

        reader = new CharacterReader(new StringReader("<title>foo</title>"));
        assertEquals("<title>foo</title>", reader.consumeToEnd());
        assertEquals(Character.MIN_VALUE, reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&x0f;"));
        assertEquals("\u000f", reader.consumeHexSequence());
        assertEquals(Character.MIN_VALUE, reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence());
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('a'));

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches('o'));
        assertTrue(reader.matches('r'));
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesIgnoreCase("oo"));
        assertTrue(reader.matchesIgnoreCase("FOO"));
        assertFalse(reader.matchesIgnoreCase("za"));

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matchesIgnoreCase("bar"));
        assertTrue(reader.matchesIgnoreCase("AR"));
        assertFalse(reader.matchesIgnoreCase("za"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAny(new char[]{'f', 'o'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matchesAny(new char[]{'f', 'o', 'r'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAnySorted(new char[]{'f', 'o'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matchesAnySorted(new char[]{'f', 'o', 'r'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchConsume("oo"));
        assertEquals("oo", reader.toString());

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matchConsume("ar"));
        assertEquals("ar", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchConsumeIgnoreCase("OO"));
        assertEquals("OO", reader.toString());

        reader = new CharacterReader(new StringReader("foobar"));
        assertTrue(reader.matchConsumeIgnoreCase("ar"));
        assertEquals("ar", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>My Page</title></head><body>..."));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.toString());
    }
}