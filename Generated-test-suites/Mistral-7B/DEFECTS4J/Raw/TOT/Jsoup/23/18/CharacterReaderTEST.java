
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        // Expert 1: Test for empty string
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());

        reader.consume();
        assertFalse(reader.isEmpty());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        // Expert 2: Test for consuming each character in sequence
        CharacterReader reader = new CharacterReader("hello");
        assertEquals('h', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        // Expert 3: Test for consuming a specified sequence of characters
        CharacterReader reader = new CharacterReader("abcd123efg");

        assertEquals("abcd", reader.consumeTo('d'));
        assertEquals("123", reader.consumeTo('3'));
        assertEquals("efg", reader.consumeToEnd());

        reader = new CharacterReader("abcd123");
        assertEquals("abcd", reader.consumeTo('d'));
        assertEquals("123", reader.consumeToEnd());

        reader = new CharacterReader("abcd123");
        assertEquals("", reader.consumeTo('z'));
        assertEquals("abcd123", reader.consumeToEnd());
    }

    // Add other test cases for other methods as needed

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }
}
