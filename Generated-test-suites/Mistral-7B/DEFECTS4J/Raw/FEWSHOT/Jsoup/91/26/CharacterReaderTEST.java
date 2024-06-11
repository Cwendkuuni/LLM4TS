package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

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
        assertEquals(1, reader.pos());

        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.current());

        reader.advance();
        assertEquals('o', reader.current());

        reader.consume();
        assertEquals('o', reader.current());

        reader.consume();
        assertEquals('f', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.consume());

        assertEquals('o', reader.current());

        assertEquals('o', reader.consume());

        assertEquals('f', reader.consume());

        assertEquals(EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.consume();
        reader.unconsume();

        assertEquals('f', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.advance();

        assertEquals('o', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();

        reader.consume();

        reader.rewindToMark();

        assertEquals('f', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();

        reader.advance();
        reader.advance();
        reader.advance();

        reader.rewindToMark();

        assertEquals('f', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(1, reader.nextIndexOf('o'));

        reader = new CharacterReader(new StringReader("foo bar"));
        assertEquals(5, reader.nextIndexOf('b'));

        reader = new CharacterReader(new StringReader("foo bar"));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSeq() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar"));
        assertEquals(5, reader.nextIndexOf(" bar"));

        reader = new CharacterReader(new StringReader("foo bar baz"));
        assertEquals(8, reader.nextIndexOf(" baz"));

        reader = new CharacterReader(new StringReader("foo bar baz"));
        assertEquals(-1, reader.nextIndexOf(" qux"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("f", reader.consumeTo('o').toString());

        assertEquals("o", reader.consumeTo('o').toString());

        assertEquals("foo", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToSeq() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar"));
        assertEquals("foo", reader.consumeTo(" bar").toString());

        assertEquals("bar", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>"));
        assertEquals("foo", reader.consumeToAny(new char[]{'<', '>'}).toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo<bar>"));
        assertEquals("foo", reader.consumeToAnySorted(new char[]{'<', '>'}).toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;"));
        assertEquals("&lt;", reader.consumeData().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        assertEquals("<tag>", reader.consumeTagName().toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foobar"));
        assertEquals("foo", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foobar123"));
        assertEquals("foo", reader.consumeLetterThenDigitSequence().toString());
        assertEquals("123", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&x61;x62;x63"));
        assertEquals("abc", reader.consumeHexSequence().toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesSeq() {
        CharacterReader reader = new CharacterReader(new StringReader("foo bar"));
        assertTrue(reader.matches("foo"));
        assertFalse(reader.matches("baz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesIgnoreCase("o"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAny('o', 'x'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesAnySorted(new char[]{'o', 'x'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsumeSeq() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchConsume("o"));
        assertTrue(reader.isEmpty());

        reader.rewindToMark();

        assertTrue(reader.matchConsume("foo"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCaseSeq() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertTrue(reader.matchConsumeIgnoreCase("o"));
        assertTrue(reader.isEmpty());

        reader.rewindToMark();

        assertTrue(reader.matchConsumeIgnoreCase("foo"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>My Page</title></head><body>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/body"));
        assertFalse(reader.containsIgnoreCase("/header"));
    }
}