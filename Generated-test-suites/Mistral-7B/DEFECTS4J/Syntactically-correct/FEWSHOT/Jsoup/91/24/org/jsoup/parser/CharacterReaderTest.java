package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testisEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("foo"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testpos() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(0, reader.pos());

        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('', reader.consume());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testconsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo('o'));
        assertEquals("", reader.consumeTo('x'));
        assertEquals("foo", reader.consumeToEnd());
    }

    @Test
    public void testconsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertEquals("bar", reader.consumeToAny('b', 'z'));
        assertEquals("baz", reader.consumeToAny('z'));
        assertEquals("", reader.consumeToAny('x'));
    }

    @Test
    public void testconsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>baz"));
        assertEquals("bar", reader.consumeToAnySorted('b', 'z'));
        assertEquals("", reader.consumeToAnySorted(new char[] {'x'}));
    }

    @Test
    public void testconsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;foo&gt;"));
        assertEquals("&lt;foo&gt;", reader.consumeData());
    }

    @Test
    public void testconsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        assertEquals("title", reader.consumeTagName());
    }

    @Test
    public void testmatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testmatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("FOO"));
        assertTrue(reader.matchesIgnoreCase("foo"));
        assertFalse(reader.matchesIgnoreCase("BAR"));
    }

    @Test
    public void testmatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAny('f', 'o'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testmatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAnySorted(new char[] {'f', 'o'}));
        assertFalse(reader.matchesAnySorted(new char[] {'x'}));
    }

    @Test
    public void testmatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testmatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("foo1"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testmark() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('f', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testmarkWithoutMarkSupport() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();
        reader.reader.close();
        reader.rewindToMark();
    }

    @Test
    public void testnextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(1, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(1, reader.nextIndexOf("oo"));
        assertEquals(-1, reader.nextIndexOf("xx"));
    }

    @Test
    public void testnextIndexOfWithOffset() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(1, reader.nextIndexOf('o', 1));
        assertEquals(-1, reader.nextIndexOf('x', 1));
        assertEquals(1, reader.nextIndexOf("oo", 1));
        assertEquals(-1, reader.nextIndexOf("xx", 1));
        assertEquals(0, reader.nextIndexOf('f', 0));
        assertEquals(1, reader.nextIndexOf('o', 0));
    }

    @Test
    public void testconsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testconsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo1"));
        assertEquals("foo1", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testconsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("\\u0061\\u0062"));
        assertEquals("ab", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testconsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }
}