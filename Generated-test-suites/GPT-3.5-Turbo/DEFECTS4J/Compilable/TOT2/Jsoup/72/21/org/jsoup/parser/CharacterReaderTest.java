package org.jsoup.parser;
import org.jsoup.parser.CharacterReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        StringReader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals(0, charReader.pos());
    }

    @Test
    public void testIsEmpty() {
        StringReader reader = new StringReader("");
        CharacterReader charReader = new CharacterReader(reader);
        assertTrue(charReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        StringReader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals('t', charReader.current());
        charReader.consume();
        assertEquals('e', charReader.current());
    }

    @Test
    public void testConsume() {
        StringReader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals('t', charReader.consume());
        assertEquals('e', charReader.consume());
    }

    @Test
    public void testAdvance() {
        StringReader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);
        charReader.advance();
        assertEquals('e', charReader.current());
    }

    @Test
    public void testNextIndexOf() {
        StringReader reader = new StringReader("testing");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals(2, charReader.nextIndexOf('s'));
        assertEquals(-1, charReader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeTo() {
        StringReader reader = new StringReader("teststring");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals("test", charReader.consumeTo('s'));
    }

    // Add more test cases for other methods as needed

}