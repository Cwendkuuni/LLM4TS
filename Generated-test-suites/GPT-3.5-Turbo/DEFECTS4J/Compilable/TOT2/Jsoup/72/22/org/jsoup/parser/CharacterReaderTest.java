package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
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
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }
    
    // Add more test cases for other methods
    
    // Edge case and exception handling test cases
    
    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(1, reader.nextIndexOf('e'));
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeTo('e'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeToAny('e', 's', 't'));
    }
    
    // Add more edge case and exception handling test cases
    
}