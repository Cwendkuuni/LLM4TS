package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test pos() method
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test isEmpty() method
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test current() method
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        // Test consume() method
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    // Add test methods for other public methods

    @Test
    public void testConsumeToEnd() {
        // Test consumeToEnd() method
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("est", reader.consumeToEnd());
    }

    // Add test cases for edge cases and exception handling scenarios

}