package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader cr = new CharacterReader(new StringReader(""));
        assertTrue(cr.isEmpty());
        assertEquals("", cr.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader cr = new CharacterReader(new StringReader("abc"));
        assertEquals("abc", cr.consumeToEnd());
        assertEquals('a', cr.current());
        assertEquals('a', cr.consume());
        assertEquals("bc", cr.consumeTo('c'));
        assertEquals("b", cr.consumeToAny(new char[]{'b'}));
        assertEquals("b", cr.consumeToAnySorted(new char[]{'b'}));
        assertEquals("a", cr.consumeData());
        assertEquals("a", cr.consumeTagName());
    }

    @Test
    public void testCharacterReaderScanStrings() {
        CharacterReader cr = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("abcdef", cr.consumeLetterSequence());
        assertEquals("abcdefghijklmnopqrstuvwxyz", cr.consumeLetterThenDigitSequence());
        assertEquals("abcdefghijklmnopqrstuvwxyz", cr.consumeHexSequence());
        assertEquals("1234567890", cr.consumeDigitSequence());
        assertEquals("abc", cr.consumeTo("abc"));
        assertEquals("def", cr.consumeTo("def"));
        assertEquals("defghijklmnopqrstuvwxyz", cr.consumeToEnd());
        assertEquals("ghijklmnopqrstuvwxyz", cr.consumeToAny(new char[]{'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}));
        assertEquals("ghijklmnopqrstuvwxyz", cr.consumeToAnySorted(new char[]{'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader cr = new CharacterReader(new StringReader("a") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        cr.consume();
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader cr = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(cr.matches('a'));
        assertTrue(cr.matches("ab"));
        assertTrue(cr.matchesIgnoreCase("AB"));
        assertTrue(cr.matchesAny(new char[]{'a', 'b'}));
        assertTrue(cr.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(cr.matchesLetter());
        assertTrue(cr.matchesDigit());
        assertFalse(cr.matchesAny(new char[]{'z'}));
        assertFalse(cr.matchesAnySorted(new char[]{'z'}));
        assertFalse(cr.matchesLetter());
        assertFalse(cr.matchesDigit());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader cr = new CharacterReader(new StringReader("abc"));
        cr.mark();
        cr.consumeTo('a');
        cr.rewindToMark();
        assertEquals("a", cr.consumeToEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderInvalidInput() {
        CharacterReader cr = new CharacterReader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderInvalidSizeInput() {
        CharacterReader cr = new CharacterReader(new StringReader(""), -1);
    }
}