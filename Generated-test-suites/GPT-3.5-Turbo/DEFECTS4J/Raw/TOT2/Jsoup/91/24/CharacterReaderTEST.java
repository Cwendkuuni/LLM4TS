package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader input = new StringReader("Test String");
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("");
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader input = new StringReader("Test");
        CharacterReader reader = new CharacterReader(input);
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }
    // Add more test cases as needed

}