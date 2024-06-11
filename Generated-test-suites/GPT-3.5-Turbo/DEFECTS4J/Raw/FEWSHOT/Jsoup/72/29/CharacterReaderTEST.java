package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        Reader input = new StringReader("test string");
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(0, reader.pos());

        char c = reader.consume();
        Assert.assertEquals(1, reader.pos());

        reader.advance();
        Assert.assertEquals(2, reader.pos());

        reader.unconsume();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("");
        CharacterReader reader = new CharacterReader(input);
        Assert.assertTrue(reader.isEmpty());

        reader.consume();
        Assert.assertTrue(reader.isEmpty());

        input = new StringReader("test string");
        reader = new CharacterReader(input);
        Assert.assertFalse(reader.isEmpty());

        while (!reader.isEmpty()) {
            reader.consume();
        }
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader input = new StringReader("test string");
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals('t', reader.current());

        reader.advance();
        Assert.assertEquals('e', reader.current());

        reader.consume();
        Assert.assertEquals('s', reader.current());

        while (!reader.isEmpty()) {
            reader.consume();
        }
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    // More test cases for the remaining methods of the CharacterReader class can be added here

}