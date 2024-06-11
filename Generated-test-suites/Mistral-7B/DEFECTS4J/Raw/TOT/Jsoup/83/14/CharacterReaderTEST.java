package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testBasicReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals(4, reader.pos());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(5, reader.pos());
        Assert.assertEquals('f', reader.current());
        Assert.assertEquals('f', reader.consume());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.advance();
        reader.advance();
        reader.advance();
        reader.bufferUp();
        Assert.assertEquals(3, reader.bufPos);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('d');
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('e', reader.current());

        consumed = reader.consumeTo('f');
        Assert.assertEquals("abcdef", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdef", consumed);
        Assert.assertEquals(11, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("helloWorld"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("hello", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("hello12345"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("hello12345", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x123ABC"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("123ABC", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed);
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test exception");
            }
        });
        reader.advance();
    }
}