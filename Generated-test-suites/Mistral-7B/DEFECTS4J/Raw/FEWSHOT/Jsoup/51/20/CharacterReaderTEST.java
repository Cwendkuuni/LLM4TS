package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(6, reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
        assertNull(reader.stringCache);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_NullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReader_isEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
        assertEquals(0, reader.mark());
        assertEquals(0, reader.length());
    }

    @Test
    public void testCharacterReader_notEmpty() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        assertEquals(3, reader.pos());
        assertEquals(0, reader.mark());
        assertEquals(3, reader.length());
    }

    @Test
    public void testCharacterReader_pos() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReader_consume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        reader.advance();
        reader.advance();
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReader_unconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        reader.unconsume();
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReader_advance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_mark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_rewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        reader.advance();
        reader.advance();
        assertEquals("c", reader.consumeAsString());
        assertEquals(EOF, reader.consumeAsString());
    }

    @Test
    public void testCharacterReader_nextIndexOf() {
        CharacterReader reader = new CharacterReader("fooBar");
        assertEquals(3, reader.nextIndexOf('o'));
        assertEquals(4, reader.nextIndexOf("Bar"));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("a", reader.consumeTo('b').toString());
        assertEquals("bc", reader.consumeTo("cd").toString());
        assertEquals("d", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("01", reader.consumeToAny('1', '3').toString());
        assertEquals("45", reader.consumeToAny('4', '5').toString());
        assertEquals("", reader.consumeToAny('0', '9').toString());
    }

    @Test
    public void testCharacterReader_consumeToAnySorted() {
        CharacterReader reader = new CharacterReader("54123");
        assertEquals("12", reader.consumeToAnySorted('1', '2').toString());
        assertEquals("3", reader.consumeToAnySorted(new char[]{'3', '4'}).toString());
        assertEquals("", reader.consumeToAnySorted(new char[]{'5', '1'}).toString());
    }

    @Test
    public void testCharacterReader_consumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&", reader.consumeData().toString());
    }

    @Test
    public void testCharacterReader_consumeTagName() {
        CharacterReader reader = new CharacterReader("<title>");
        assertEquals("title", reader.consumeTagName().toString());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello world", reader.consumeToEnd().toString());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("b123a456");
        assertEquals("b123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x0f&x23");
        assertEquals("fx", reader.consumeHexSequence().toString());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches("ell"));
        assertFalse(reader.matches('!'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertTrue(reader.matchesIgnoreCase("WORLD"));
        assertFalse(reader.matchesIgnoreCase("FOO"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader reader = new CharacterReader("012345");
        assertTrue(reader.matchesAny('0', '5'));
        assertFalse(reader.matchesAny('6'));
    }

    @Test
    public void testCharacterReader_matchesAnySorted() {
        CharacterReader reader = new CharacterReader("54123");
        assertTrue(reader.matchesAnySorted(new char[]{'1', '3'}));
        assertTrue(reader.matchesAnySorted(new char[]{'3', '5'}));
        assertFalse(reader.matchesAnySorted(new char[]{'2', '5'}));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("hello world");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter(' '));
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("hello123world");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReader_matchConsume() {
        CharacterReader reader = new CharacterReader("fooBar");
        assertTrue(reader.matchConsume("oo"));
        assertEquals("Bar", reader.toString());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("FOO Bar");
        assertTrue(reader.matchConsumeIgnoreCase("oo"));
        assertEquals("Bar", reader.toString());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<title>Hello World</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HELLO"));
        assertTrue(reader.containsIgnoreCase("WORLD"));
    }

    @Test
    public void testCharacterReader_toString() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals("hello world", reader.toString());
    }

    @Test
    public void testCharacterReader_cacheString() {
        CharacterReader reader = new CharacterReader("hello world");
        String cached1 = reader.cacheString(0, 5);
        assertEquals("hello", cached1);
        String cached2 = reader.cacheString(6, 5);
        assertEquals("world", cached2);
        String cached3 = reader.cacheString(1, 1);
        assertEquals("h", cached3);
    }

    @Test
    public void testCharacterReader_rangeEquals() {
        CharacterReader reader = new CharacterReader("hello world");
        char[] input = reader.input;
        String cached = reader.cacheString(0, 5);
        assertTrue(reader.rangeEquals(0, 5, cached));
        assertFalse(reader.rangeEquals(0, 6, cached));
    }
}