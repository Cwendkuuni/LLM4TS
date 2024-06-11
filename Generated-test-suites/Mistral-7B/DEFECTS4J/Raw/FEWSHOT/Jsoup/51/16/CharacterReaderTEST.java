package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.input);
        assertEquals(6, reader.length);
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
        reader.consumeToEnd();
        assertEquals(Character.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());
        reader.consumeToEnd();
        assertEquals(Character.EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("bc", reader.consumeToEnd());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(5, reader.nextIndexOf("def"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeTo('b').trim());
        assertEquals("def", reader.consumeTo("def").trim());
        assertEquals("", reader.consumeTo('z').trim());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        assertEquals("d123", reader.consumeToAny('d', '3').trim());
        assertEquals("ef", reader.consumeToAnySorted(new char[]{'e', 'f'}).trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}).trim());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("abcd&123ef");
        assertEquals("abcd", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<abc def>");
        assertEquals("abc", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
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
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd%65ef");
        assertEquals("abcdef", reader.consumeHexSequence().trim());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abcdef");
        assertEquals("123", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("b"));
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
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("a"));
        assertEquals("b", reader.current());
        reader.matchConsume("b");
        assertEquals("c", reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("A"));
        assertEquals("b", reader.current());
        reader.matchConsumeIgnoreCase("B");
        assertEquals("c", reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head>");
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertFalse(reader.containsIgnoreCase("/body>"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        String cached = reader.cacheString(0, 4);
        assertEquals("abcd", cached);
        cached = reader.cacheString(0, 6);
        assertEquals("abcd123", cached);
        cached = reader.cacheString(6, 3);
        assertEquals("ef", cached);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        assertTrue(reader.rangeEquals(0, 4, "abcd"));
        assertFalse(reader.rangeEquals(0, 4, "abce"));
    }
}