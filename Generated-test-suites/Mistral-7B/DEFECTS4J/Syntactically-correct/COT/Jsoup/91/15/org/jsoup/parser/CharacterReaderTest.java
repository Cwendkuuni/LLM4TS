package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderConstructorWithNullReader() {
        new CharacterReader(null, 1024);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("", 1);
        assertEquals(0, reader.pos());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testBufferUpMethod() throws IOException, UncheckedIOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.bufferUp();
        assertEquals(4, reader.bufLength);
        assertEquals(0, reader.bufPos);
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());

        reader.mark();
        reader.rewindToMark();
        assertEquals('b', reader.current());

        reader.isEmpty(); // no need to check return value since it doesn't throw an exception when empty

        reader.isEmptyNoBufferUp();
        assertEquals(true, reader.isEmptyNoBufferUp());

        reader.bufferUp();
        assertEquals(false, reader.isEmptyNoBufferUp());

        reader.bufferUp();
        reader.bufferUp();
        assertEquals(4, reader.bufLength);
        assertEquals(0, reader.bufPos);

        reader.rewindToMark();
        assertEquals(3, reader.bufPos);

        reader.consumeTo('a');
        assertEquals("bcd", reader.consumeToEnd());

        reader.consumeTo("ab");
        assertEquals("cd", reader.consumeToEnd());

        reader.consumeToAny('a');
        assertEquals("a", reader.consumeToAny('a'));
        assertEquals("abcd", reader.consumeToAny('a'));
        assertEquals("abcd", reader.consumeToAnySorted('a', 'b'));

        reader.consumeToAnySorted(new char[]{'a', 'b'});
        assertEquals("abcd", reader.consumeToAnySorted(new char[]{'a', 'b'}));

        reader.consumeData();
        assertEquals("&<null", reader.consumeData());

        reader.consumeTagName();
        assertEquals("", reader.consumeTagName());

        reader.consumeToEnd();
        assertEquals("abcd", reader.consumeToEnd());

        reader.consumeLetterSequence();
        assertEquals("ab", reader.consumeLetterSequence());

        reader.consumeLetterThenDigitSequence();
        assertEquals("ab1", reader.consumeLetterThenDigitSequence());

        reader.consumeHexSequence();
        assertEquals("12", reader.consumeHexSequence());

        reader.consumeDigitSequence();
        assertEquals("123", reader.consumeDigitSequence());

        reader.matches('a');
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));

        reader.matches("ab");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));

        reader.matchesIgnoreCase("Ab");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("Cd"));

        reader.matchesAny('a');
        assertTrue(reader.matchesAny('a'));
        assertFalse(reader.matchesAny('b'));

        reader.matchesAnySorted(new char[]{'a', 'b'});
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'b', 'a'}));

        reader.matchesLetter();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        reader.matchConsume("ab");
        assertEquals("cd", reader.consumeToEnd());

        reader.matchConsumeIgnoreCase("Ab");
        assertEquals("cd", reader.consumeToEnd());

        reader.containsIgnoreCase("ab");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertFalse(reader.containsIgnoreCase("cd"));
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        CharacterReader reader = new CharacterReader(new StringReader(""), 1024);
        assertEquals(0, reader.pos());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReaderConstructorWithNullString() {
        try {
            new CharacterReader(null, 1024);
        } catch (IllegalArgumentException e) {
            assertEquals("Input must not be null", e.getMessage());
        }
    }
}