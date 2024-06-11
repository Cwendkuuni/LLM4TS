package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());

        reader.mark();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.rewindToMark();
        assertEquals('b', reader.current());

        reader.consumeTo('f');
        assertEquals("bcdef", reader.toString());
        assertEquals("f", reader.consume());
        assertEquals("def", reader.toString());

        reader.consumeTo("de");
        assertEquals("def", reader.toString());
        reader.consumeToAny('a', 'e');
        assertEquals("def", reader.toString());

        reader.consumeToAnySorted('e', 'a');
        assertEquals("def", reader.toString());

        reader.consumeData();
        assertEquals("cdef", reader.toString());

        reader.consumeTagName();
        assertEquals("", reader.toString());

        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String str = reader.cacheString(0, 5);
        assertEquals("abcde", str);
        assertEquals(5, str.length());
        reader.cacheString(0, 5); // same string, should not create a new instance
        assertEquals("abcde", str);

        reader.cacheString(5, 2);
        String str2 = reader.cacheString(0, 10);
        assertEquals("abcdefabcde", str2);
        assertEquals(10, str2.length());
        assertEquals("abcde", str.substring(0, 5));
        assertEquals("fabcde", str2.substring(0, 6));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
        assertFalse(reader.matches("efg"));

        reader.mark();
        reader.consume();
        assertFalse(reader.matches('a'));
        assertEquals('b', reader.current());
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("G"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("efg"));

        reader.mark();
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("a"));
        assertEquals('b', reader.current());

        reader.consumeTo("ABC");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertEquals("ABC", reader.consumeToEnd());
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'e'));
        assertTrue(reader.matchesAnySorted('e', 'a'));
        assertTrue(reader.matchesAny('a', 'f', 'g'));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAnySorted('z', 'a'));

        reader.consumeTo('f');
        assertTrue(reader.matchesAny('a', 'f'));
        assertEquals("bcdef", reader.toString());
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.consumeTo('f');
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());

        reader.consumeTo('5');
        assertTrue(reader.matchesDigit());
        assertEquals("def", reader.toString());
    }
}