package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
        assertEquals("abcd", reader.consumeTo('d'));
        assertEquals("abcdabcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("abc", reader.consumeToAny('a', 'c'));
        assertEquals("xyz", reader.consumeToAnySorted('z', 'x', 'y'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("xyz", reader.consumeToAnySorted('z', 'x', 'y'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<nul>"));
        assertEquals("&<", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tab>\n<title>Test Title</title>\n</tab>"));
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc&def&ghi0123ABC4D5E6F"));
        assertEquals("&def&ghi0123ABC4D5E6F", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertEquals("0123456789", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesIgnoreCase("Ae"));
        assertFalse(reader.matchesIgnoreCase("G"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesAnySorted('z', 'x', 'y'));
        assertFalse(reader.matchesAnySorted('g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String expected = "def";
        reader.matchConsume("def");
        assertEquals(expected, reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String expected = "DEF";
        reader.matchConsumeIgnoreCase("def");
        assertEquals(expected, reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(3, reader.pos());
        reader.advance();
        assertEquals(4, reader.pos());
    }
}