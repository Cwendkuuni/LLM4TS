package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Simple() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        Assert.assertEquals('H', reader.current());
        reader.consume();

        Assert.assertEquals('e', reader.current());
        reader.consume();

        Assert.assertEquals('l', reader.current());
        reader.consume();

        Assert.assertEquals('l', reader.current());
        reader.consume();

        Assert.assertEquals('o', reader.current());
        reader.consume();

        Assert.assertEquals(' ', reader.current());
        reader.consume();

        Assert.assertEquals('W', reader.current());
        reader.consume();

        Assert.assertEquals('o', reader.current());
        reader.consume();

        Assert.assertEquals('r', reader.current());
        reader.consume();

        Assert.assertEquals('l', reader.current());
        reader.consume();

        Assert.assertEquals('d', reader.current());
        reader.consume();

        Assert.assertEquals('\n', reader.current());
        reader.consume(); // consume newline

        Assert.assertEquals(CharacterReader.EOF, reader.current());
        reader.consume();

        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_BufferUp() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        // Consume first 5 letters
        for (int i = 0; i < 5; i++) {
            reader.consume();
        }

        // Buffer should be updated, current position should be 6th letter
        Assert.assertEquals('l', reader.current());

        reader.bufferUp();

        // After bufferUp, current position should be reset to buffer start
        Assert.assertEquals('H', reader.current());

        // Buffer size should be updated
        Assert.assertEquals(input.length() + 1, reader.pos());
    }

    @Test
    public void testCharacterReader_Empty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));

        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReader_IOException() {
        CharacterReader reader = new CharacterReader(new StringReader("not a file"));

        reader.consume();
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String consumed = reader.consumeToEnd();

        Assert.assertEquals(input, consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        String input = "Hello World!<html>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String consumed = reader.consumeTo('!' );

        Assert.assertEquals("Hello World!", consumed);
        Assert.assertEquals('<', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        String input = "Hello World!<html><head><title>My Title</title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String consumed = reader.consumeToAny(new char[]{'<', 't', 'i', 't', 'l'} );

        Assert.assertEquals("Hello World!<html>", consumed);
        Assert.assertEquals('<', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToAnySorted() {
        String input = "Hello World!<html><head><title>My Title</title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String consumed = reader.consumeToAnySorted(new char[]{'t', 'i', 't', 'l', '<'} );

        Assert.assertEquals("Hello World!<html>", consumed);
        Assert.assertEquals('<', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeData() {
        String input = "&lt;Hello World!&gt;";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String consumed = reader.consumeData();

        Assert.assertEquals("<Hello World!>", consumed);
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeTagName() {
        String input = "<html><head><title>My Title</title>";
        CharacterReader reader = new CharacterReader(new StringReader(input));

        String consumed = reader.consumeTagName();

        Assert.assertEquals("html", consumed);
        Assert.assertEquals('/', reader.current());
    }
}