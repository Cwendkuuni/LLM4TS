package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_init() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input.toCharArray(), reader.input);
        assertEquals(input.length(), reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
        assertArrayEquals(new String[0], reader.stringCache);
    }

    @Test
    public void testCharacterReader_isEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testCharacterReader_unconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReader_advance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReader_mark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("d", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("f", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testCharacterReader_nextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeTo('b').toString());
        assertEquals("bcdef", reader.consumeToEnd().toString());
        assertEquals("", reader.consumeToAny('x').toString());
        assertEquals("", reader.consumeToAnySorted(new char[]{'x', 'y'}).toString());
        assertEquals("&", reader.consumeData().toString());
        assertEquals("<title", reader.consumeTagName().toString());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeToAny(new char[]{'a', 'b'}).toString());
        assertEquals("def", reader.consumeToAny(new char[]{'e', 'f'}).toString());
        assertEquals("", reader.consumeToAny(new char[]{'x', 'y'}).toString());
    }

    @Test
    public void testCharacterReader_consumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeToAnySorted(new char[]{'a', 'b'}).toString());
        assertEquals("def", reader.consumeToAnySorted(new char[]{'e', 'f'}).toString());
        assertEquals("", reader.consumeToAnySorted(new char[]{'x', 'y'}).toString());
    }

    @Test
    public void testCharacterReader_consumeData() {
        CharacterReader reader = new CharacterReader("&abcdef");
        assertEquals("&", reader.consumeData().toString());
    }

    @Test
    public void testCharacterReader_consumeTagName() {
        CharacterReader reader = new CharacterReader("<title>abcdef");
        assertEquals("<title", reader.consumeTagName().toString());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("abc", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("&
\n<h1 id=\"header-1\">\u0061\u0062\u0063</h1>\u0064\u0065");
        assertEquals("\u0061\u0062\u0063", reader.consumeHexSequence().toString());
        assertEquals("\u0064\u0065", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertTrue(reader.matchesIgnoreCase("ABCd"));
        assertTrue(reader.matchesIgnoreCase("abc"));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReader_matchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAnySorted('e'));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_matchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("abcd"));
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        assertTrue(reader.matchConsumeIgnoreCase("ABC"));
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<title>XYZ</title>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("XYZ"));
        assertFalse(reader.containsIgnoreCase("/header"));
    }

    @Test
    public void testCharacterReader_toString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testCharacterReader_cacheString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String cached = reader.cacheString(0, 3);
        assertEquals("abc", cached);
        cached = reader.cacheString(3, 3);
        assertEquals("def", cached);
    }
}