
package org.junit.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        CharacterReader reader2 = new CharacterReader("abcdef");

        // Check if reader initialized with empty string is empty
        assertTrue(reader1.isEmpty());

        // Check if reader at the end of non-empty input string is empty
        reader2.advance();
        reader2.advance();
        reader2.advance();

        // Check if reader is no longer empty after consuming some characters
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        // Test with valid characters
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());

        // Test with end-of-file character (EOF)
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");

        // Test with single character sequence
        assertEquals("a", reader.consumeTo('a'));
        assertEquals(CharacterReader.EOF, reader.consume());

        // Test with multiple character sequence
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals('c', reader.consume());

        // Test with empty sequence
        assertEquals("", reader.consumeTo(""));
        assertEquals(CharacterReader.EOF, reader.consume());

        // Test with sequence longer than the input string
        assertEquals("abcdef", reader.consumeTo("xyz"));
        assertEquals(CharacterReader.EOF, reader.consume());
    }
}