package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_emptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReader_singleCharInput() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(1, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_multipleCharsInput() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertEquals(4, reader.length);
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals('D', reader.current());
        assertEquals('D', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_markUnconsume() {
        CharacterReader reader = new CharacterReader("123");
        reader.mark();
        assertEquals('1', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('1', reader.current());
    }

    @Test
    public void testCharacterReader_posAndMark() {
        CharacterReader reader = new CharacterReader("123");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_nextIndexOf() {
        CharacterReader reader = new CharacterReader("123ababc");
        assertEquals(2, reader.nextIndexOf('2'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(6, reader.nextIndexOf("ab"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader reader = new CharacterReader("123ababc");
        assertEquals("12", reader.consumeTo('2'));
        assertEquals("ab", reader.consumeTo("ab"));
        assertEquals("abc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("123ababc");
        assertEquals("12", reader.consumeToAny('1', '2'));
        assertEquals("abc", reader.consumeToAny('a', 'z'));
        assertEquals("", reader.consumeToAny('x', 'y'));
    }

    @Test
    public void testCharacterReader_consumeToAnySorted() {
        CharacterReader reader = new CharacterReader("123ababc");
        assertEquals("12", reader.consumeToAnySorted(new char[]{'1', '2'}));
        assertEquals("abc", reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'x', 'y'}));
    }

    @Test
    public void testCharacterReader_consumeData() {
        CharacterReader reader = new CharacterReader("1&2<3");
        assertEquals("1&2", reader.consumeData());
        assertEquals("3", reader.consumeData());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeTagName() {
        CharacterReader reader = new CharacterReader("<p class=\"foo\">Hello</p>");
        assertEquals("p", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("123ababc");
        assertEquals("123ababc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x21&x41&x61");
        assertEquals("!Aa", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matches('3'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesIgnoreCase("three"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesAny('3', '4', '5'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testCharacterReader_matchesAnySorted() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesAnySorted(new char[]{'3', '4', '5'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x', 'y'}));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('x'));
    }

    @Test
    public void testCharacterReader_toString() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.toString());
    }

    @Test
    public void testCharacterReader_cacheString() {
        CharacterReader reader = new CharacterReader("12345");
        String str1 = reader.cacheString(0, 5);
        String str2 = reader.cacheString(0, 5);
        assertEquals(str1, str2);
        assertNotSame(str1, str2);
    }

    @Test
    public void testCharacterReader_rangeEquals() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.rangeEquals(0, 5, "12345"));
    }
}