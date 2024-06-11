package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertEquals("Hello World!", characterReader.toString());
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderConstructorWithReaderThrowsIOException() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader) {
            @Override
            void bufferUp() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"), "Hello World!".length());
        Assert.assertEquals("Hello World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderMethods() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals(true, characterReader.isEmpty());

        characterReader.advance();
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals('H', characterReader.current());
        Assert.assertEquals(false, characterReader.isEmpty());

        characterReader.consume();
        Assert.assertEquals(1, characterReader.pos());
        Assert.assertEquals('H', characterReader.current());
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals(2, characterReader.pos());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertEquals(2, characterReader.pos());
        Assert.assertEquals(false, characterReader.isEmpty());

        characterReader.rewindToMark();
        Assert.assertEquals(2, characterReader.pos());
        Assert.assertEquals('H', characterReader.current());

        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());

        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
        characterReader.advance();
        Assert.assertEquals(0, characterReader.nextIndexOf('H'));
        Assert.assertEquals(0, characterReader.nextIndexOf("Hel"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Helo"));
        Assert.assertEquals("Hello", characterReader.consumeTo("llo"));
        Assert.assertEquals("World!", characterReader.consumeToEnd());

        char[] chars = {' ', '/', '>', TokeniserState.nullChar};
        Assert.assertEquals("", characterReader.consumeToAny(chars));
        Assert.assertEquals(" ", characterReader.consumeToAny(new char[]{' '}));
        Assert.assertEquals("/", characterReader.consumeToAny(new char[]{'/'}));
        Assert.assertEquals(">", characterReader.consumeToAny(new char[]{'>'}));
        Assert.assertEquals("", characterReader.consumeToAny(new char[0]));

        Assert.assertEquals("", characterReader.consumeToAnySorted(new char[0]));
        Assert.assertEquals("", characterReader.consumeToAnySorted(new char[]{' '}));
        Assert.assertEquals("", characterReader.consumeToAnySorted(new char[]{'/'}));
        Assert.assertEquals("/", characterReader.consumeToAnySorted(new char[]{'/', '>'}));
        Assert.assertEquals("", characterReader.consumeToAnySorted(new char[]{'>', '/'}));
        Assert.assertEquals("", characterReader.consumeToAnySorted(new char[]{' ', '/', '>', '>'}));

        Assert.assertEquals("", characterReader.consumeData());
        Assert.assertEquals("<", characterReader.consumeTagName());
        Assert.assertEquals("Hello World!", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals(true, characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testCharacterReaderNullString() {
        CharacterReader characterReader = new CharacterReader(new StringReader(null));
        Assert.assertEquals("", characterReader.toString());
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals(true, characterReader.isEmpty());
        Assert.assertEquals(EOF, characterReader.current());
    }
}