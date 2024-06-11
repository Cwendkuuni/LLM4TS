package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeAndAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        reader.advance();
        assertEquals(3, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(3, reader.nextIndexOf('c'));
        assertEquals(5, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('g'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeTo('c').trim());
        assertEquals("cdef", reader.consumeTo('e').trim());
        assertEquals("def", reader.consumeTo('f').trim());
        assertEquals("", reader.consumeTo('g').trim());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToAny('b', 'c').trim());
        assertEquals("d", reader.consumeToAny('d').trim());
        assertEquals("", reader.consumeToAny('g').trim());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToAnySorted('b', 'c').trim());
        assertEquals("d", reader.consumeToAnySorted('d').trim());
        assertEquals("", reader.consumeToAnySorted('g').trim());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("abc&def");
        assertEquals("abc", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<abcd>efgh");
        assertEquals("abcd", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbC");
        assertTrue(reader.matchesIgnoreCase("aBc"));
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAnySorted('a', 'b', 'c'));
        assertFalse(reader.matchesAnySorted('d'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testMarkAndRewindTwice() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.mark();
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }
}