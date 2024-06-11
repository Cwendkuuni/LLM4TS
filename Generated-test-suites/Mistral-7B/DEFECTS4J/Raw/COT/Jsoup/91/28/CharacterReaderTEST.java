package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World");
        CharacterReader charReader = new CharacterReader(reader);
        Assert.assertNotNull(charReader);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader charReader = new CharacterReader("Hello World");
        Assert.assertNotNull(charReader);
    }

    @Test
    public void testBufferUp() throws IOException {
        String input = "Hello World";
        CharacterReader charReader = new CharacterReader(new StringReader(input));
        char expected = 'H';
        char actual = charReader.current();
        charReader.consume();
        Assert.assertEquals(expected, actual);
        charReader.bufferUp();
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() throws IOException {
        String input = "Hello World";
        CharacterReader charReader = new CharacterReader(new StringReader(input)) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        charReader.consume();
    }

    @Test
    public void testPos() {
        String input = "Hello World";
        CharacterReader charReader = new CharacterReader(new StringReader(input));
        int expected = 0;
        int actual = charReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        String input = "Hello World";
        CharacterReader charReader = new CharacterReader(new StringReader(input));
        Assert.assertFalse(charReader.isEmpty());

        charReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(charReader.isEmpty());

        charReader = new CharacterReader(new StringReader(null));
        Assert.assertTrue(charReader.isEmpty());
    }

    @Test
    public void testConsume() {
        String input = "Hello World";
        CharacterReader charReader = new CharacterReader(new StringReader(input));
        char expected = 'H';
        char actual = charReader.consume();
        Assert.assertEquals(expected, actual);

        charReader = new CharacterReader(new StringReader(""));
        char actual2 = charReader.consume();
        Assert.assertEquals(Character.MIN_VALUE, actual2);
    }

    // Add more test cases for the remaining methods in CharacterReader class
}