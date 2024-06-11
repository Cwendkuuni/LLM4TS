package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());

        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());

        reader = new CharacterReader("");
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.advance();

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.consumeAsString());

        reader = new CharacterReader("");
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd-efgh");
        assertEquals("cd", reader.consumeTo('d'));
        assertEquals("efgh", reader.consumeToEnd());

        reader = new CharacterReader("--ab--cd--ef--gh");
        assertEquals("ab", reader.consumeTo("--"));
        assertEquals("cd", reader.consumeTo("--"));
        assertEquals("ef", reader.consumeTo("--"));
        assertEquals("gh", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345abcdefABCDEF");
        assertEquals("12345abcdefABCDEF", reader.consumeToAny('1', '2', '3', '4', '5', 'a', 'b', 'c', 'd', 'e', 'f'));

        reader = new CharacterReader("012345abcdefABCDEF");
        assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("c", reader.consumeToEnd().charAt(0));

        reader = new CharacterReader("--abc--");
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123xyz");
        assertEquals("abc", reader.consumeLetterSequence());

        reader = new CharacterReader("123xyz");
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12abcdef");
        assertEquals("12abcdef", reader.consumeHexSequence());

        reader = new CharacterReader("0x12");
        assertEquals("12", reader.consumeHexSequence());

        reader = new CharacterReader("0x");
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345abc");
        assertEquals("12345", reader.consumeDigitSequence());

        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));

        reader.advance();
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("a"));

        reader = new CharacterReader("--abc--");
        assertTrue(reader.matches("--"));
        assertTrue(reader.matches("--"));

        reader.advance();
        assertTrue(reader.matches("a"));
        assertTrue(reader.matches("a"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));

        reader.advance();
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("B"));

        reader.advance();
        assertFalse(reader.matchesIgnoreCase("a"));
        assertFalse(reader.matchesIgnoreCase("A"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("012345abcdefABCDEF");
        assertTrue(reader.matchesAny('1', '2', '3', '4', '5', 'a', 'b', 'c', 'd', 'e', 'f'));
        assertTrue(reader.matchesAny('1', '2'));
        assertFalse(reader.matchesAny('z'));

        reader = new CharacterReader("012345abcdefABCDEF");
        assertFalse(reader.matchesAny('z', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abc");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("a"));
        assertEquals('b', reader.current());

        reader = new CharacterReader("--abc--");
        assertTrue(reader.matchConsume("--"));
        assertEquals('a', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCabc");
        assertTrue(reader.matchConsumeIgnoreCase("a"));
        assertEquals('b', reader.current());

        reader = new CharacterReader("ABCabc");
        assertTrue(reader.matchConsumeIgnoreCase("A"));
        assertEquals('b', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><title>My Title</title>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.toString());

        reader = new CharacterReader("--abc--");
        assertEquals("abc", reader.toString());
    }
}