package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertEquals("Test string", reader.toString());
    }

    @Test
    public void testCharacterReaderWithMaxBufferLen() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string with maxBufferLen length"), CharacterReader.maxBufferLen);
        assertEquals("Test string with maxBufferLen length", reader.toString());
    }

    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('Test', reader.consumeLetterSequence());
        assertEquals("string", reader.consumeTo("string"));
        assertEquals("", reader.consumeTo("abc"));
        assertEquals("", reader.consumeToAny());
        assertEquals("", reader.consumeToAnySorted());
        assertEquals("", reader.consumeData());
        assertEquals("", reader.consumeTagName());
        assertEquals("", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertEquals(5, reader.nextIndexOf(' '));
        assertEquals(6, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals("string", reader.nextIndexOf("ring"));
        assertEquals("Test", reader.nextIndexOf("est"));
        assertEquals("string", reader.nextIndexOf("String"));
        assertEquals("Test", reader.nextIndexOf("test", 5));
        assertEquals("", reader.nextIndexOf("z"));
        assertEquals("", reader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertTrue(reader.matches(' '));
        assertTrue(reader.matches('t'));
        assertTrue(reader.matches("ing"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("Test123"));
        assertFalse(reader.matches("t2"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertTrue(reader.matchesIgnoreCase("test"));
        assertTrue(reader.matchesIgnoreCase("ing"));
        assertTrue(reader.matchesIgnoreCase("t"));
        assertTrue(reader.matchesIgnoreCase("s"));
        assertFalse(reader.matchesIgnoreCase("z"));
        assertFalse(reader.matchesIgnoreCase("Test123"));
        assertFalse(reader.matchesIgnoreCase("t2"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertTrue(reader.matchesAny(' '));
        assertTrue(reader.matchesAny('t'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny('s'));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertTrue(reader.matchesAnySorted(' '));
        assertTrue(reader.matchesAnySorted('t'));
        assertTrue(reader.matchesAnySorted('e'));
        assertTrue(reader.matchesAnySorted('s'));
        assertFalse(reader.matchesAnySorted('z'));
        assertFalse(reader.matchesAnySorted('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter(' '));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        assertFalse(reader.matchesDigit());
    }
}