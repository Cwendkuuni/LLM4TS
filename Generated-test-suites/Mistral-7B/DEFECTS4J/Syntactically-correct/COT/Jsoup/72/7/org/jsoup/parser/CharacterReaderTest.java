package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.consume();
        String expected = "ello Wor";
        Assert.assertEquals(expected, reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenBufferUpFails() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        reader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.unconsume();
        reader.unconsume();
        Assert.assertTrue(reader.isEmpty());
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        char expected = 'H';
        Assert.assertEquals(expected, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        char expected = 'H';
        Assert.assertEquals(expected, reader.consume());
        Assert.assertEquals("ello Wor", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "Hello World";
        String actual = reader.consumeToEnd();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        char c = 'o';
        String expected = "ello Wor";
        String actual = reader.consumeTo(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToWithEmptySequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "";
        String actual = reader.consumeTo("");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcd456efgh");
        char[] chars = {'a', 'b'};
        String expected = "bcde";
        String actual = reader.consumeToAny(chars);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("123abcd456efgh");
        char[] chars = {'d', 'c'};
        String expected = "efgh";
        String actual = reader.consumeToAnySorted(chars);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        String expected = "&<null";
        String actual = reader.consumeData();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Hello World</title>");
        String expected = "title";
        String actual = reader.consumeTagName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        char c = 'l';
        Assert.assertTrue(reader.matches(c));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String seq = "ll";
        Assert.assertTrue(reader.matches(seq));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        String seq = "HELLO";
        Assert.assertTrue(reader.matchesIgnoreCase(seq));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        char c = 'l';
        Assert.assertTrue(reader.matchesAny(c));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("Hello World");
        char[] chars = {'d', 'c'};
        Assert.assertTrue(reader.matchesAnySorted(chars));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        String seq = "ll";
        reader.matchConsume(seq);
        Assert.assertEquals("o Wo", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        String seq = "HELLO";
        reader.matchConsumeIgnoreCase(seq);
        Assert.assertEquals("World", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Hello World</title></head></html>");
        String seq = "</title>";
        Assert.assertTrue(reader.containsIgnoreCase(seq));
    }
}