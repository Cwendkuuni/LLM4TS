package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReaderSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCD"));
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals('C', reader.consume());
        assertEquals('D', reader.current());
        assertEquals('D', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals(6, reader.nextIndexOf('F'));
        assertEquals(-1, reader.nextIndexOf('Z'));
        assertEquals(0, reader.nextIndexOf('A'));
        assertEquals(6, reader.nextIndexOf('F', 4));
        assertEquals(-1, reader.nextIndexOf('Z', 4));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertEquals(2, reader.nextIndexOf("BC"));
        assertEquals(-1, reader.nextIndexOf("XY"));
        assertEquals(0, reader.nextIndexOf("AB"));
        assertEquals(2, reader.nextIndexOf("BC", 4));
        assertEquals(-1, reader.nextIndexOf("XY", 4));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo('F');
        assertEquals("ABC", consumed);
        assertEquals('F', reader.current());
        consumed = reader.consumeTo('G');
        assertEquals("DEFG", consumed);
        assertEquals('G', reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo("CD");
        assertEquals("AB", consumed);
        assertEquals('C', reader.current());
        consumed = reader.consumeTo("EF");
        assertEquals("G", consumed);
        assertEquals('F', reader.current());
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAny('A', 'F');
        assertEquals("", consumed);
        assertEquals('A', reader.current());
        consumed = reader.consumeToAny('C', 'G');
        assertEquals("BDE", consumed);
        assertEquals('G', reader.current());
        consumed = reader.consumeToAny('H', 'Z');
        assertEquals("", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAnySorted('C', 'G');
        assertEquals("ABDE", consumed);
        assertEquals('G', reader.current());
        consumed = reader.consumeToAnySorted('A', 'F');
        assertEquals("", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG&<null"));
        String consumed = reader.consumeData();
        assertEquals("ABCDEF", consumed);
        assertEquals('&', reader.current());
        consumed = reader.consumeData();
        assertEquals("null", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("       <tag name='foo'></tag>"));
        String consumed = reader.consumeTagName();
        assertEquals("tag", consumed);
        assertEquals('/', reader.current());
        consumed = reader.consumeTagName();
        assertEquals("foo", consumed);
        assertEquals('>', reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToEnd();
        assertEquals("ABCDEFG", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String consumed = reader.consumeLetterSequence();
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC123DEF456GHI789"));
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("ABC123DEF456", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEF"));
        String consumed = reader.consumeHexSequence();
        assertEquals("ABCDEF", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345678901234567890123456"));
        String consumed = reader.consumeDigitSequence();
        assertEquals("012345678901234567890123456", consumed);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches('E'));
        assertFalse(reader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchesSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matches("AB"));
        assertFalse(reader.matches("XY"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("XY"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matchesAny('A', 'E'));
        assertFalse(reader.matchesAny('Z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matchesAnySorted('A', 'E'));
        assertFalse(reader.matchesAnySorted('Z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345678901234567890123456"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('A'));
    }

    @Test
    public void testCharacterReaderMatchConsumeSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.matchConsume("AB"));
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertFalse(reader.matchConsume("XY"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("</style>"));
        assertFalse(reader.containsIgnoreCase("XY"));
    }
}