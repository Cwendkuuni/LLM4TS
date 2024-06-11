package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.toString());
        assertEquals(0, reader.pos());
        assertEquals(5, reader.length());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());
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
        String consumed = reader.consumeAsString();
        assertEquals("abc", consumed);
        reader.consume();
        consumed = reader.consumeAsString();
        assertEquals("", consumed);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNextIndexOfCharNegativePosition() {
        CharacterReader reader = new CharacterReader("abc");
        reader.nextIndexOf(EOF);
    }

    @Test
    public void testNextIndexOfCharSingleCharFound() {
        CharacterReader reader = new CharacterReader("abcabc");
        int index = reader.nextIndexOf('a');
        assertEquals(0, index);
    }

    @Test
    public void testNextIndexOfCharMultipleCharsFound() {
        CharacterReader reader = new CharacterReader("abcabca");
        int index = reader.nextIndexOf('a');
        assertEquals(0, index);
        index = reader.nextIndexOf('a');
        assertEquals(4, index);
    }

    @Test
    public void testNextIndexOfCharCharNotPresent() {
        CharacterReader reader = new CharacterReader("abc");
        int index = reader.nextIndexOf('e');
        assertEquals(-1, index);
    }

    @Test
    public void testNextIndexOfCharArraySingleCharFound() {
        CharacterReader reader = new CharacterReader("abcabc");
        int index = reader.nextIndexOf(new char[]{'a'});
        assertEquals(0, index);
    }

    @Test
    public void testNextIndexOfCharArrayMultipleCharsFound() {
        CharacterReader reader = new CharacterReader("abcabca");
        int index = reader.nextIndexOf(new char[]{'a', 'b'});
        assertEquals(0, index);
        index = reader.nextIndexOf(new char[]{'a', 'b'});
        assertEquals(4, index);
    }

    @Test
    public void testNextIndexOfCharArrayCharNotPresent() {
        CharacterReader reader = new CharacterReader("abc");
        int index = reader.nextIndexOf(new char[]{'e'});
        assertEquals(-1, index);
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo('c');
        assertEquals("ab", consumed);
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo("bc");
        assertEquals("ab", consumed);
    }

    @Test
    public void testConsumeToAnySingleChar() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToAny('d');
        assertEquals("abcd", consumed);
    }

    @Test
    public void testConsumeToAnyMultipleChars() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToAny(new char[]{'b', 'c'});
        assertEquals("bcd", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToEnd();
        assertEquals("abcd", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeLetterSequence();
        assertEquals("ab", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab12cd");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("ab1", consumed);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>My Title</title></head><body>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HEAD"));
        assertFalse(reader.containsIgnoreCase("body"));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("E"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('a', 'c'));
        assertFalse(reader.matchesAny('e', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

}