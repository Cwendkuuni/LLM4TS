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
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        assertEquals("", reader.consumeAsString());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("ab", reader.consumeToEnd());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));
        reader.pos++;
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
        reader.pos += 2;
        assertEquals(1, reader.nextIndexOf("ef"));
        reader.pos += 1;
        assertEquals(-1, reader.nextIndexOf("ef"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("ef", reader.consumeTo("ef"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToAny('b', 'c'));
        assertEquals("ef", reader.consumeToAny('e', 'f'));
        assertEquals("", reader.consumeToAny('x', 'z'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToAnySorted('b', 'c'));
        assertEquals("ef", reader.consumeToAnySorted('e', 'f'));
        assertEquals("", reader.consumeToAnySorted('x', 'z'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&", reader.consumeData().charAt(0));
        assertEquals('<', reader.consumeData().charAt(1));
        assertEquals('', reader.consumeData().charAt(2));
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("/title>");
        assertEquals("/title", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdABCdef123");
        assertEquals("abcd", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("%#61%#63%");
        assertEquals("%#61%#63%", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("123456", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("bc"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("bc"));
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAnySorted('a'));
        assertTrue(reader.matchesAnySorted('b'));
        assertFalse(reader.matchesAnySorted('x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("bc"));
        assertEquals(2, reader.pos());
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("bc"));
        assertTrue(reader.matchConsumeIgnoreCase("AbC"));
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("/title>");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }
}