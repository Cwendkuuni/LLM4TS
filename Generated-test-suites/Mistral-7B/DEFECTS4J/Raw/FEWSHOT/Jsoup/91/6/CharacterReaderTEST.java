package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithReaderInput() {
        String testInput = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        // Test initial state
        assertEquals(-1, characterReader.current());
        assertEquals(-1, characterReader.consume());
        assertEquals(0, characterReader.pos());
        assertTrue(characterReader.isEmpty());

        // Test reading characters
        characterReader.advance();
        assertEquals('H', characterReader.current());
        assertEquals('H', characterReader.consume());
        assertEquals(1, characterReader.pos());
        assertFalse(characterReader.isEmpty());

        characterReader.advance();
        assertEquals('e', characterReader.current());
        assertEquals('e', characterReader.consume());
        assertEquals(2, characterReader.pos());

        characterReader.unconsume();
        assertEquals(2, characterReader.pos());
        assertEquals('e', characterReader.current());

        // Test reading to end
        characterReader.consumeToEnd();
        assertEquals(11, characterReader.pos());
        assertEquals("Hello World!", characterReader.toString());
        assertTrue(characterReader.isEmpty());

        // Test mark and rewind
        characterReader.mark();
        characterReader.consumeTo('l');
        characterReader.rewindToMark();
        assertEquals(2, characterReader.pos());
        assertEquals('e', characterReader.current());

        // Test reading substrings
        assertEquals("Hello", characterReader.consumeTo('l' + 1));
        assertEquals("World!", characterReader.consumeToEnd());

        // Test reading with delimiters
        assertEquals("llo", characterReader.consumeTo('l' + 1));
        assertEquals("World!", characterReader.consumeToEnd());

        // Test reading with multiple delimiters
        assertEquals("llo Wor", characterReader.consumeToAny(new char[]{'l', 'd'}));
        assertEquals("ld!", characterReader.consumeToAny(new char[]{'l', 'd'}));
        assertEquals("World!", characterReader.consumeToEnd());

        // Test reading with sorted delimiters
        assertEquals("llo World!", characterReader.consumeToAnySorted(new char[]{'l', 'd'}));

        // Test reading data
        assertEquals("Hello World!", characterReader.consumeData());
        assertEquals("World!", characterReader.consumeTagName());

        // Test reading with various input characters
        CharacterReader readerWithNull = new CharacterReader(new StringReader(new String(new char[]{'&', '\u0000'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'<', '\u0000'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'&', '<', '\u0000'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'&', '<', '\u0000', '&'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'&', '<', '\u0000', '&', '<'})));

        // Test caching
        CharacterReader readerWithCachedString = new CharacterReader(new StringReader("repeat string"));
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());

        // Test empty input
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        assertEquals(0, emptyReader.pos());
        assertTrue(emptyReader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithStringInput() {
        CharacterReader characterReader = new CharacterReader("Hello World!");

        // Test initial state
        assertEquals(-1, characterReader.current());
        assertEquals(-1, characterReader.consume());
        assertEquals(0, characterReader.pos());
        assertTrue(characterReader.isEmpty());

        // Test reading characters
        characterReader.advance();
        assertEquals('H', characterReader.current());
        assertEquals('H', characterReader.consume());
        assertEquals(1, characterReader.pos());
        assertFalse(characterReader.isEmpty());

        characterReader.advance();
        assertEquals('e', characterReader.current());
        assertEquals('e', characterReader.consume());
        assertEquals(2, characterReader.pos());

        characterReader.unconsume();
        assertEquals(2, characterReader.pos());
        assertEquals('e', characterReader.current());

        // Test reading to end
        characterReader.consumeToEnd();
        assertEquals(11, characterReader.pos());
        assertEquals("Hello World!", characterReader.toString());
        assertTrue(characterReader.isEmpty());

        // Test mark and rewind
        characterReader.mark();
        characterReader.consumeTo('l');
        characterReader.rewindToMark();
        assertEquals(2, characterReader.pos());
        assertEquals('e', characterReader.current());

        // Test reading substrings
        assertEquals("Hello", characterReader.consumeTo('l' + 1));
        assertEquals("World!", characterReader.consumeToEnd());

        // Test reading with delimiters
        assertEquals("llo", characterReader.consumeTo('l' + 1));
        assertEquals("World!", characterReader.consumeToEnd());

        // Test reading with multiple delimiters
        assertEquals("llo Wor", characterReader.consumeToAny(new char[]{'l', 'd'}));
        assertEquals("ld!", characterReader.consumeToAny(new char[]{'l', 'd'}));
        assertEquals("World!", characterReader.consumeToEnd());

        // Test reading with sorted delimiters
        assertEquals("llo World!", characterReader.consumeToAnySorted(new char[]{'l', 'd'}));

        // Test reading data
        assertEquals("Hello World!", characterReader.consumeData());
        assertEquals("World!", characterReader.consumeTagName());

        // Test reading with various input characters
        CharacterReader readerWithNull = new CharacterReader(new StringReader(new String(new char[]{'&'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'<'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'&', '<'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'&', '<', '&'})));
        characterReader = new CharacterReader(new StringReader(new String(new char[]{'&', '<', '&', '<'})));

        // Test caching
        CharacterReader readerWithCachedString = new CharacterReader("repeat string");
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());
        assertEquals("repeat string", readerWithCachedString.consumeToEnd());

        // Test empty input
        CharacterReader emptyReader = new CharacterReader("");
        assertEquals(0, emptyReader.pos());
        assertTrue(emptyReader.isEmpty());
    }

    @Test
    public void testCharacterReaderExceptionHandling() {
        String testInput = "Hello\u0000World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(testInput));

        try {
            characterReader.consumeToEnd();
        } catch (IOException e) {
            assertEquals("Unexpected IO exception", e.getMessage());
        }

        try {
            characterReader.consumeToEnd();
        } catch (Throwable e) {
            // expected UncheckedIOException, not a Throwable
            fail("Unexpected exception type");
        }

        try {
            characterReader.consumeToEnd();
        } catch (UncheckedIOException e) {
            // expected UncheckedIOException, not null
            assertNotNull(e.getCause());
        }

        try {
            characterReader.consumeToEnd();
        } catch (Exception e) {
            // expected UncheckedIOException, not Exception
            fail("Unexpected exception type");
        }

        try {
            characterReader.consumeToEnd();
        } catch (RuntimeException e) {
            // expected UncheckedIOException, not RuntimeException
            fail("Unexpected exception type");
        }

        try {
            characterReader.consumeToEnd();
        } catch (Error e) {
            // expected UncheckedIOException, not Error
            fail("Unexpected exception type");
        }
    }
}