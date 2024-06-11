package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setup() {
        String input = "This is a test input";
        Reader reader = new StringReader(input);
        characterReader = new CharacterReader(reader);
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        assertEquals('T', characterReader.consume());
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }
    
    // Add more test cases for the remaining methods in CharacterReader
    
}