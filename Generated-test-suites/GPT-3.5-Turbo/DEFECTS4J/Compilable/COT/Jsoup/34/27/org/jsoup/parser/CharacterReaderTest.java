package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        reader.mark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOf_withCharSequence() {
        CharacterReader reader = new CharacterReader("ababcabcab");
        assertEquals(4, reader.nextIndexOf("abc"));
        assertEquals(-1, reader.nextIndexOf("abcd"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals("ab", reader.consumeTo('c'));
    }

    @Test
    public void testConsumeTo_withString() {
        CharacterReader reader = new CharacterReader("abcdabcab");
        assertEquals("ab", reader.consumeTo("c"));
        assertEquals("ab", reader.consumeTo("c"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc123abc");
        assertEquals("abc123", reader.consumeToAny('1', '2'));
        assertEquals("abc123", reader.consumeToAny('1', '2'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3");
        assertEquals("a1b2c3", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3");
        assertEquals("1", reader.consumeDigitSequence());
        assertEquals("2", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatches_withString() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("bc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("AB"));
        assertFalse(reader.matchesIgnoreCase("BC"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc1");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("bc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertFalse(reader.matchConsumeIgnoreCase("BO"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.containsIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("XY"));
    }
}
