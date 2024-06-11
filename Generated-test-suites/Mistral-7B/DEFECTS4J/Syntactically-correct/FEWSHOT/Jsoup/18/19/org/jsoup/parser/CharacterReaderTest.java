package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        CharacterReader reader1 = new CharacterReader("abc");
        assertFalse(reader1.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());

        CharacterReader reader1 = new CharacterReader("abc");
        assertEquals(0, reader1.pos());

        reader1.advance();
        assertEquals(1, reader1.pos());
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
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.consume());

        CharacterReader reader1 = new CharacterReader("abc");
        assertEquals('a', reader1.consume());
        assertEquals('b', reader1.consume());
        assertEquals('c', reader1.consume());
        assertEquals(EOF, reader1.consume());

        CharacterReader reader2 = new CharacterReader("abc");
        reader2.advance();
        reader2.advance();
        assertEquals('c', reader2.consume());
        assertEquals(EOF, reader2.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());

        reader.advance();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.consumeAsString());

        reader.advance();
        assertEquals("c", reader.consumeAsString());
        assertEquals(EOF, reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("foo--bar");
        assertEquals("bar", reader.consumeTo('r'));
        assertEquals("foobar", reader.toString());

        reader = new CharacterReader("foo--bar");
        assertEquals("foobar", reader.consumeTo(String.valueOf(' ')));
        assertEquals(EOF, reader.consume());

        reader = new CharacterReader("foo--bar");
        assertEquals("foobar", reader.consumeToEnd());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcd123abcd123");
        assertEquals("123", reader.consumeToAny('1', '2'));
        assertEquals("abcd123abcd123", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(EOF, reader.consume());

        reader = new CharacterReader("--abc");
        assertEquals("abc", reader.consumeToEnd());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghijk");
        assertEquals("abcd", reader.consumeLetterSequence());
        assertEquals("efghijk", reader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abcd123efghijk");
        assertEquals("12", reader.consumeHexSequence());
        assertEquals("3abcd123efghijk", reader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abcd123efghijk");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("abcd123efghijk", reader.toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));

        reader.consume();
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("c"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("c"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('b', 'a'));
        assertFalse(reader.matchesAny('c', 'a'));

        reader.consume();
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('c', 'a'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abc");
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertEquals("c", reader.current());

        reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertEquals("c", reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmno");
        assertTrue(reader.containsIgnoreCase("ef"));
        assertTrue(reader.containsIgnoreCase("gh"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.toString());

        reader.advance();
        assertEquals("c", reader.toString());
        assertEquals(EOF, reader.toString());
    }
}