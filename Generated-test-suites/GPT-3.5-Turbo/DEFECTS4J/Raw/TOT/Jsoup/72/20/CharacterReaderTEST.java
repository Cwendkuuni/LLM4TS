
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
    public void setUp() {
        String input = "This is a test input string.";
        Reader reader = new StringReader(input);
        characterReader = new CharacterReader(reader);
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
        characterReader.advance();
        assertEquals(2, characterReader.pos());
        characterReader.consumeToAny(' ');
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
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

    // Additional test cases for other methods and scenarios

    @Test
    public void testBufferUp() throws IOException {
        // Test that bufferUp() successfully buffers up the characters from the reader
        Reader reader = new StringReader("Buffer test");
        characterReader = new CharacterReader(reader, 5);

        characterReader.bufferUp();
        assertEquals(0, characterReader.pos());
        assertEquals(11, characterReader.bufLength);
        assertEquals(0, characterReader.bufPos);
        assertEquals(6, characterReader.bufSplitPoint);
        assertEquals('B', characterReader.charBuf[0]);
        assertEquals('t', characterReader.charBuf[10]);
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
    }

    // More test cases for other methods and scenarios

}

