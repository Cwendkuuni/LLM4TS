package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
        Assert.assertEquals(-1, reader.consume());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('d');
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals(5, reader.pos());

        consumed = reader.consumeTo('f');
        Assert.assertEquals("abcdef", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(11, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        });
        Assert.assertTrue(reader.isEmpty());
        reader.consume();
    }
}