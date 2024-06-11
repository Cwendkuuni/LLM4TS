package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        
        reader.consume();
        assertEquals('b', reader.current());
    }
    
    // Add more test cases for remaining methods
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
    
    // Add test cases for remaining methods

}

