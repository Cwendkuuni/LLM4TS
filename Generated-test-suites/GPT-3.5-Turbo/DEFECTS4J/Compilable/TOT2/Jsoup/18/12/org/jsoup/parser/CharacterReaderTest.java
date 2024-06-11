package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    // Add more test cases for other methods...

}