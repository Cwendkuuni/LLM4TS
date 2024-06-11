package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        // pos() should be 0 initially
        assertEquals(0, reader.pos());
        // consume should return EOF initially
        assertEquals(CharacterReader.EOF, reader.current());
        // advance should move the pointer to the next character
        reader.advance();
        // current should now return 'H'
        assertEquals('H', reader.current());
        // consume should now return 'H' and move the pointer to the next character
        assertEquals('H', reader.consume());
        assertEquals('e', reader.current());
        // consume should return 'e' and move the pointer to the next character
        assertEquals('e', reader.consume());
        // isEmpty should return false since there is still a character in the reader
        assertFalse(reader.isEmpty());
        // consume should return 'l' and move the pointer to the next character
        assertEquals('l', reader.consume());
        // consume should return 'o' and move the pointer to the next character
        assertEquals('o', reader.consume());
        // consume should return ' ' and move the pointer to the next character
        assertEquals(' ', reader.consume());
        // consume should return 'W' and move the pointer to the next character
        assertEquals('W', reader.consume());
        // consume should return 'r' and move the pointer to the next character
        assertEquals('r', reader.consume());
        // consume should return 'd' and move the pointer to the next character
        assertEquals('d', reader.consume());
        // isEmpty should now return true since all characters have been consumed
        assertTrue(reader.isEmpty());
        // mark and rewindToMark should restore the reader to its previous state
        reader.mark();
        reader.rewindToMark();
        assertEquals('d', reader.current());
        // consumeTo should return the substring between the current position and the specified char
        assertEquals("World", reader.consumeTo(' '));
        // consumeTo should return an empty string when the specified char is not present in the reader
        assertEquals("", reader.consumeTo('z'));
        // consumeToAny should return the substring between the current position and the first occurrence of any of the specified chars
        assertEquals("Hello", reader.consumeToAny(new char[]{'l', 'o'}));
        // consumeToAny should return the substring between the current position and the end of the reader when none of the specified chars are present
        assertEquals("World", reader.consumeToAny(new char[]{'a', 'e'}));
        // consumeData should return the substring between the current position and the first occurrence of any of the specified chars
        assertEquals("Hello Wo&rd", reader.consumeData());
        // consumeTagName should return the tag name between the current position and the first occurrence of a tag-ending character
        reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        assertEquals("title", reader.consumeTagName());
        // consumeToEnd should return the remaining string in the reader
        assertEquals("<head><title>Test</title></head><body>", reader.consumeToEnd());
    }
}