package org.jsoup.parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(26, reader.nextIndexOf('z'));
        assertEquals(-1, reader.nextIndexOf('A'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(0, reader.nextIndexOf("abc"));
        assertEquals(23, reader.nextIndexOf("xyz"));
        assertEquals(-1, reader.nextIndexOf("ABC"));
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("abcdefghijklm", reader.consumeTo('n'));
        assertEquals("nopqrstuvwxyz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("abcdefghi", reader.consumeTo("jkl"));
        assertEquals("jklmnopqrstuvwxyz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("abcdefghi", reader.consumeToAny('j', 'k', 'l'));
        assertEquals("jklmnopqrstuvwxyz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('A'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("ABC"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('A', 'B', 'C'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("ab"));
        assertEquals('c', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals('c', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("ABC"));
    }

    @Override
    public String toString() {
        return "abcdefghijklmnopqrstuvwxyz";
    }
}