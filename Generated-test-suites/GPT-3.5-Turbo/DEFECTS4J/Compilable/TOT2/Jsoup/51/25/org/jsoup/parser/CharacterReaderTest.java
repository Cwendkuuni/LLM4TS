package org.jsoup.parser;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string.");
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('h', characterReader.current());
    }
    
    // continue with test cases for other methods
    
    @Test
    public void testNextIndexOf() {
        assertEquals(4, characterReader.nextIndexOf('i'));
        assertEquals(5, characterReader.nextIndexOf(" is"));
        assertEquals(-1, characterReader.nextIndexOf('z'));
        assertEquals(-1, characterReader.nextIndexOf("test"));
    }
    
    // define and test for other edge cases and error scenarios
    
    @Test
    public void testRangeEquals() {
        assertTrue(characterReader.rangeEquals(0, 4, "This"));
        assertFalse(characterReader.rangeEquals(0, 4, "that"));
    }
}