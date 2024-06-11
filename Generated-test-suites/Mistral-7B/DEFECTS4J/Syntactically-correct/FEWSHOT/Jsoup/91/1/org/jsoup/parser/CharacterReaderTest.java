package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    private final static int maxBufferLen = 1024 * 32;
    private static final int readAheadLimit = (int) (maxBufferLen * 0.75);

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());
        reader.advance();
        assertEquals(1, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consumeTo('b');
        assertEquals("a", reader.consumeToEnd());
        assertEquals("ab", reader.consumeTo("bc"));
        assertEquals("abcdefg", reader.consumeToEnd());
        reader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("abcdefg", reader.consumeToAny(new char[]{'b', 'a'}));
        assertEquals("defg", reader.consumeData());
        assertEquals("", reader.consumeTagName());
        reader.close();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(6, reader.nextIndexOf('f'));
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.nextIndexOf('c'));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        reader.consume();
        assertEquals(5, reader.nextIndexOf('e'));
        reader.consumeTo('f');
        assertEquals(5, reader.nextIndexOf('e'));
        reader.consumeTo("de");
        assertEquals(7, reader.nextIndexOf("de"));
        reader.consumeToAnySorted(new char[]{'d', 'e'});
        assertEquals(7, reader.nextIndexOf("de"));
    }

    @Test
    public void testBuffering() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        reader.bufferUp();
        assertEquals(maxBufferLen, reader.bufLength);
        reader.bufferUp();
        assertEquals(maxBufferLen, reader.bufLength);
        reader.close();
    }

    @Test
    public void testUncheckedIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        reader.reader.mark(0);
        reader.reader.close();
        reader.reader = null;
        reader.bufferUp();
        try {
            reader.consume();
            fail("Expected UncheckedIOException");
        } catch (UncheckedIOException e) {
            assertEquals("IOException closing input stream", e.getMessage());
        }
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String sequence = reader.consumeLetterSequence();
        assertEquals("abcdef", sequence);
        reader.consumeLetterThenDigitSequence();
        sequence = reader.consumeLetterSequence();
        assertEquals("ghijk", sequence);
        reader.consumeLetterThenDigitSequence();
        sequence = reader.consumeLetterThenDigitSequence();
        assertEquals("lmnopqrst", sequence);
        reader.consumeLetterSequence();
        assertEquals("uvwxyz", reader.consumeLetterSequence());
        reader.consume();
        assertEquals(0, reader.pos());
        reader.consumeLetterSequence();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEF"));
        String sequence = reader.consumeHexSequence();
        assertEquals("abcdef", sequence);
        reader.consumeHexSequence();
        sequence = reader.consumeHexSequence();
        assertEquals("ghijklm", sequence);
        reader.consume();
        assertEquals(0, reader.pos());
        reader.consumeHexSequence();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345678901234567890"));
        String sequence = reader.consumeDigitSequence();
        assertEquals("12345", sequence);
        reader.consumeDigitSequence();
        sequence = reader.consumeDigitSequence();
        assertEquals("67890", sequence);
        reader.consume();
        assertEquals(0, reader.pos());
        reader.consumeDigitSequence();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('g'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("g"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('f'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        assertTrue(reader.matchesAnySorted('a', 'b', 'f', 'e', 'c'));
        assertFalse(reader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345678901234567890"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
}