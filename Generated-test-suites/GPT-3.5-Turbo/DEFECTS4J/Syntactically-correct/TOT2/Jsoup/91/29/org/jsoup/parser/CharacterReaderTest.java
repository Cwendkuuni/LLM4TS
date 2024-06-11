package org.jsoup.parser;

import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {
  
    @Test
    public void testConstructorWithReaderAndSize() {
        StringReader input = new StringReader("Test input");
        CharacterReader reader = new CharacterReader(input, 10);
        assertNotNull(reader);
    }
  
    @Test
    public void testConstructorWithReader() {
        StringReader input = new StringReader("Test input");
        CharacterReader reader = new CharacterReader(input);
        assertNotNull(reader);
    }
  
    @Test
    public void testConstructorWithString() {
        CharacterReader reader = new CharacterReader("Test input");
        assertNotNull(reader);
    }

    // Add more test cases for each public method in the CharacterReader class
  
}