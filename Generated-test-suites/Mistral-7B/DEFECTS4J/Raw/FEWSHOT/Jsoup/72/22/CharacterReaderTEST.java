package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertNull(reader.consume());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
        reader.mark();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        reader.bufferUp();
        assertFalse(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertEquals(2, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf(null));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("bcdefg", reader.consumeTo('g'));
        assertEquals("", reader.consumeTo('x'));
        assertEquals("efg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertEquals("bcd", reader.consumeToAny(new char[]{'b', 'c'}));
        assertEquals("", reader.consumeToAny(new char[]{'x'}));
        assertEquals("cd", reader.consumeToAnySorted(new char[]{'d', 'c'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("ab&cd&efg"));
        assertEquals("ab&cd", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name='value'/>"));
        assertEquals("tag", reader.consumeTagName());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc#x12f&def"));
        assertEquals("x12f", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches(null));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEF"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertTrue(reader.matchesIgnoreCase("f"));
        assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'c', 'd'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }
}