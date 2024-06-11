package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testConstructor() {
        // Test constructor
        CharacterReader reader = new CharacterReader("Hello World!");
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        CharacterReader reader2 = new CharacterReader("Hello World!");

        assertTrue(reader.isEmpty());
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        reader.advance();
        reader.advance();

        assertEquals(4, reader.pos());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World!");

        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());

        reader.consume();
        reader.advance();

        assertEquals('l', reader.current());
    }

    // Add other test methods here...

    // 
}