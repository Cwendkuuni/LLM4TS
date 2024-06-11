package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());

        reader.unconsume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('a', reader.current());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('c', reader.current());
        reader.consume();
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testPositionAndConsuming() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());

        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(2, reader.pos());
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals(3, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));

        reader = new CharacterReader(new StringReader("abcc"));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(2, reader.nextIndexOf('c'));
        assertEquals(3, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfWithCharSequences() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        reader.advance();

        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(2, reader.nextIndexOf("cc"));
        assertEquals(-1, reader.nextIndexOf("dd"));

        reader = new CharacterReader(new StringReader("abcc"));
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(2, reader.nextIndexOf("cc"));
        assertEquals(3, reader.nextIndexOf("cc"));
        assertEquals(-1, reader.nextIndexOf("dd"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("d", reader.current());

        reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("cd", reader.consumeTo('d'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToWithCharSequences() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("d", reader.current());

        reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("cd", reader.consumeTo("cd"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("bcd", reader.consumeToAny(new char[]{'b', 'c', 'd'}));
        assertEquals("", reader.consumeToAny(new char[]{'a'}));
        assertEquals("", reader.consumeToAny(new char[]{'e'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("bcd", reader.consumeToAnySorted(new char[]{'b', 'c', 'd'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'a'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'e'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<"));
        assertEquals("&", reader.consumeData());
        assertEquals("<", reader.current());

        reader = new CharacterReader(new StringReader("ab&cd<ef"));
        assertEquals("ab", reader.consumeData());
        assertEquals("&cd<ef", reader.toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t<tag>"));
        assertEquals("\t<tag>", reader.consumeTagName());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("<tag>\n"));
        assertEquals("<tag>", reader.consumeTagName());
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("<tag>\r"));
        assertEquals("<tag>", reader.consumeTagName());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abcd", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("d", reader.current());

        reader = new CharacterReader(new StringReader("ABCd"));
        assertEquals("ABC", reader.consumeLetterSequence());
        assertEquals("d", reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("a1b2c3"));
        assertEquals("a1b2", reader.consumeLetterThenDigitSequence());
        assertEquals("c3", reader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&#x123;"));
        assertEquals("\u007b", reader.consumeHexSequence());
        assertEquals("", reader.toString());

        reader = new CharacterReader(new StringReader("&#x1234;"));
        assertEquals("\u007b\u0034", reader.consumeHexSequence());
        assertEquals("", reader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertEquals("123", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));

        reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbC"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("dEf"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAny(new char[]{'d'}));

        reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'d'}));

        reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny(new char[]{'c', 'a'}));
        assertFalse(reader.matchesAny(new char[]{'d'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));

        reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));

        reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted(new char[]{'c', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader = new CharacterReader(new StringReader("ABC"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        assertTrue(reader.matchConsume("bc"));
        assertEquals("d", reader.current());

        reader = new CharacterReader(new StringReader("abcd"));
        assertTrue(reader.matchConsume("cd"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"));
        assertTrue(reader.matchConsumeIgnoreCase("BC"));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("AbCd"));
        assertTrue(reader.matchConsumeIgnoreCase("cd"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/BODY"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}