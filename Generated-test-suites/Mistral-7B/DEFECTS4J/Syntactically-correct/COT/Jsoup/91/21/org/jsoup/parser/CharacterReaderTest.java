package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testConstructorWithString() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertNotNull(reader);
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.bufferUp();
        Assert.assertFalse(reader.isEmptyNoBufferUp());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionInBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"), 1) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        reader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertEquals(0, reader.pos());
        reader.bufferUp();
        Assert.assertEquals(1, reader.pos());
        reader.consume();
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertFalse(reader.isEmpty());
        reader.bufferUp();
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertEquals('T', reader.current());
        reader.bufferUp();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('s', reader.current());
        reader.advance();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        char c = reader.current();
        reader.consume();
        Assert.assertEquals(c, reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('s', reader.current());
    }

    // Add more test cases here for other methods in the CharacterReader class

}