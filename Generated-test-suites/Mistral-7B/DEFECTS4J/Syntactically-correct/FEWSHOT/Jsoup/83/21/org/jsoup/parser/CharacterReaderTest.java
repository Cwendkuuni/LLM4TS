package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader reader = new StringReader("abc");
        CharacterReader charReader = new CharacterReader(reader);
        charReader.bufferUp();
        reader.close();
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(6, reader.nextIndexOf('f'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals(3, reader.nextIndexOf("def"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("def", reader.consumeTo("def"));
        assertEquals("ijk", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals("abcdef", reader.consumeToSequence("def"));
        assertEquals("ijk", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals("bcde", reader.consumeToAny(new char[]{'b', 'c', 'd', 'e'}));
        assertEquals("ijk", reader.consumeToAny(new char[]{'z', 'w'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals("bcde", reader.consumeToAnySorted(new char[]{'b', 'c', 'd', 'e'}));
        assertEquals("ijk", reader.consumeToAnySorted(new char[]{'z', 'w'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals("abc&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader(" <title>Test Page</title>"));
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader(" <title>Test Page</title>"));
        assertEquals("Test Page</title>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        assertEquals("abcdef", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testMatchesWithSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("ac"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesIgnoreCase("AB"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("ac"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'c', 'd'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'c'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("ab", reader.matchConsume("ab"));
        assertEquals("c", reader.matchConsume("c"));
        assertEquals("", reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals("AB", reader.matchConsumeIgnoreCase("AB"));
        assertEquals("ab", reader.matchConsumeIgnoreCase("ab"));
        assertEquals("", reader.matchConsumeIgnoreCase("xyz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Page</title></head><body>..."));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/script"));
    }
}