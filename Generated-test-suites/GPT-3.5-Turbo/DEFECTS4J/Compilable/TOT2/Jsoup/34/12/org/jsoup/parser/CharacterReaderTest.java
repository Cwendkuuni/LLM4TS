package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader("Example input");
        assertNotNull(reader);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Example input");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Example input");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Example input");
        assertEquals('E', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Example input");
        char val = reader.consume();
        assertEquals('E', val);
        assertEquals(1, reader.pos());
    }

    // Additional test cases for other public methods should be added here

}