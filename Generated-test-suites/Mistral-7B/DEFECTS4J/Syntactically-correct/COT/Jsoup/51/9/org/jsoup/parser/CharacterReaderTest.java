package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.parser.CharacterReader;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.toString());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentAndConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());

        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());

        reader.advance();
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("bc", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abccdeaabccde");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(4, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('x'));

        reader.advance();
        reader.advance();
        assertEquals(6, reader.nextIndexOf('c'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abccdeaabccde");
        assertEquals("b", reader.consumeTo('c').toString());
        assertEquals("cd", reader.consumeTo('d').toString());
        assertEquals("e", reader.consumeTo('e').toString());
        assertEquals("", reader.consumeTo('x').toString());
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abccdeaabccde");
        assertEquals("bcd", reader.consumeToAny(new char[]{'b', 'c', 'd'}).toString());
        assertEquals("e", reader.consumeToAny(new char[]{'a', 'e'}).toString());
        assertEquals("", reader.consumeToAny(new char[]{'x'}).toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abccdeaabccde");
        assertEquals("bcd", reader.consumeToAnySorted(new char[]{'b', 'c', 'd'}).toString());
        assertEquals("e", reader.consumeToAnySorted(new char[]{'a', 'e'}).toString());
        assertEquals("", reader.consumeToAnySorted(new char[]{'x'}).toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<abc");
        assertEquals("&<", reader.consumeData().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Hello</title>");
        assertEquals("title", reader.consumeTagName().toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<title>Hello</title>");
        assertEquals("Hello</title>", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab123cd");
        assertEquals("ab123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testConsumeHexSequence() {   
        CharacterReader reader = new CharacterReader("%#1234%");
        assertEquals("%#1234", reader.consumeHexSequence().toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("aBcD"));
        assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'e'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'e'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testToString() {
        assertEquals("", new CharacterReader("").toString());
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.cacheString(0, 3).toString());
        assertEquals("def", reader.cacheString(3, 3).toString());
        assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.rangeEquals(0, 3, "abc"));
        assertFalse(reader.rangeEquals(0, 4, "abcde"));
    }
}