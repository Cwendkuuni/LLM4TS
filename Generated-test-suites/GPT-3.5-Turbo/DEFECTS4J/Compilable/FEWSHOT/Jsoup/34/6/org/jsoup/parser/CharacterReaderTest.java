package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("a", reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("a", reader.consume());
        assertEquals("b", reader.consume());
        assertEquals("c", reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals("b", reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        assertEquals("b", reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.mark();
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals("a", reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(3, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(3, reader.nextIndexOf("de"));
        assertEquals(-1, reader.nextIndexOf("xy"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo("de"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeToAny('d', 'e', 'f'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3");
        assertEquals("a1", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3");
        assertEquals("a1", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3");
        assertEquals("1", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("aBcdefg");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesAny('a', 'x', 'c'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("aBcdefg");
        assertTrue(reader.matchConsumeIgnoreCase("ABC"));
        assertFalse(reader.matchConsumeIgnoreCase("XYZ"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertFalse(reader.containsIgnoreCase("xyz"));
        assertTrue(reader.containsIgnoreCase("AB"));
    }
}