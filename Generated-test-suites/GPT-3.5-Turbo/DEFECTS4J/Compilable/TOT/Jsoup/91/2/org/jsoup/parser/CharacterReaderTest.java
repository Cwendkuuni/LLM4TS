package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;

    @Before
    public void setup() {
        Reader reader = new StringReader("Hello Test");
        characterReader = new CharacterReader(reader, 1024);
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        
        // Test when reader is empty
        Reader emptyReader = new StringReader("");
        characterReader = new CharacterReader(emptyReader, 1024);
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        
        // Test when reader is empty
        Reader emptyReader = new StringReader("");
        characterReader = new CharacterReader(emptyReader, 1024);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
        
        characterReader.advance();
        assertEquals('e', characterReader.current());
        
        // Test when reader is empty
        Reader emptyReader = new StringReader("");
        characterReader = new CharacterReader(emptyReader, 1024);
        assertEquals((char)-1, characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());

        assertEquals('e', characterReader.consume());

        // Test when reader is empty
        Reader emptyReader = new StringReader("");
        characterReader = new CharacterReader(emptyReader, 1024);
        assertEquals((char)-1, characterReader.consume());
    }
    
    // test cases for other methods...
    
}