package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertEquals(EOF, characterReader.consume());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals('b', characterReader.consume());
        Assert.assertEquals(2, characterReader.pos());
        Assert.assertEquals('c', characterReader.current());
        Assert.assertEquals('c', characterReader.consume());
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals('b', characterReader.consume());
        Assert.assertEquals(2, characterReader.pos());
        Assert.assertEquals('c', characterReader.current());
        Assert.assertEquals('c', characterReader.consume());
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderWithNullReader() {
        CharacterReader characterReader = new CharacterReader(null, maxBufferLen);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderWithNegativeSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"), -1);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IO exception");
            }
        });
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
    }
}