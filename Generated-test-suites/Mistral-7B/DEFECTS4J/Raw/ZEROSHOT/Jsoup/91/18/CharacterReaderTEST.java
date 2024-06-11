package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCurrentChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        // consume to EOF
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.current());
        Assert.assertEquals('l', characterReader.consume());
        // consume to EOF
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("Hello", consumed);
        characterReader.consume();
        consumed = characterReader.consumeTo('d');
        Assert.assertEquals("World", consumed);
        characterReader.consume();
        // consume to EOF
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", consumed);
        // consume to EOF
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("!!!Hello World!!!"));
        String consumed = characterReader.consumeToAny(',');
        Assert.assertEquals("!!!", consumed);
        consumed = characterReader.consumeToAny('l');
        Assert.assertEquals("Hello World", consumed);
        consumed = characterReader.consumeToEnd();
        Assert.assertEquals("!!!Hello World!!!", consumed);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("!!!Hello World!!!"));
        String consumed = characterReader.consumeToAnySorted(new char[]{'l', 'w'});
        Assert.assertEquals("Hello World", consumed);
        consumed = characterReader.consumeToEnd();
        Assert.assertEquals("!!!Hello World!!!", consumed);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;html&gt;"));
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&lt;html", consumed);
        consumed = characterReader.consumeData();
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html>"));
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("html", consumed);
        consumed = characterReader.consumeTagName();
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
    }
}