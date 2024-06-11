package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSimpleString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanToCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String data = reader.consumeTo('l');
        Assert.assertEquals("He", data);
        Assert.assertEquals('l', reader.current());
        Assert.assertEquals('l', reader.consume());
        Assert.assertEquals('o', reader.current());
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReaderScanToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String data = reader.consumeToEnd();
        Assert.assertEquals("Hello World", data);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String data = reader.consumeTo("llo");
        Assert.assertEquals("Hello", data);
        Assert.assertEquals('W', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.mark(maxBufferLen);
        reader.reader.reset();
        reader.reader.skip(maxBufferLen + 1);
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.mark();
        String data = reader.consumeTo('l');
        Assert.assertEquals("He", data);
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
        Assert.assertEquals('H', reader.consume());
    }
}