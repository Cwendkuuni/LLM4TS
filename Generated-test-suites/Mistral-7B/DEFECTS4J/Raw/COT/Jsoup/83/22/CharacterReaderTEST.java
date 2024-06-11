package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader charReader = new CharacterReader(reader);
        Assert.assertEquals("Hello World!", charReader.toString());
    }

    @Test
    public void testCharacterReaderConsume() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader charReader = new CharacterReader(reader);

        char current = charReader.current();
        Assert.assertEquals('H', current);

        char consumed = charReader.consume();
        Assert.assertEquals('H', consumed);

        current = charReader.current();
        Assert.assertEquals('H', current);
    }

    @Test
    public void testCharacterReaderBufferUp() throws IOException {
        Reader reader = new StringReader("Hello World!");
        CharacterReader charReader = new CharacterReader(reader);

        char current = charReader.current();
        Assert.assertEquals('H', current);

        char consumed = charReader.consume();
        Assert.assertEquals('H', consumed);

        char next = charReader.current();
        Assert.assertEquals('H', next);

        charReader.bufferUp();

        current = charReader.current();
        Assert.assertEquals('E', current);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        Reader reader = new StringReader("");
        CharacterReader charReader = new CharacterReader(reader);
        Assert.assertTrue(charReader.isEmpty());
    }

    @Test
    public void testCharacterReaderPos() {
        String input = "Hello World!";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        int pos = charReader.pos();
        Assert.assertEquals(0, pos);

        for (int i = 0; i < input.length(); i++) {
            char current = charReader.current();
            char consumed = charReader.consume();
            int newPos = charReader.pos();
            Assert.assertEquals(i + 1, newPos);
            Assert.assertEquals(current, consumed);
        }

        int endPos = charReader.pos();
        Assert.assertEquals(input.length(), endPos);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        String input = "abcdefababcdef";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        int indexOfC = charReader.nextIndexOf('c');
        Assert.assertEquals(2, indexOfC);

        int indexOfAb = charReader.nextIndexOf("ab");
        Assert.assertEquals(5, indexOfAb);

        int indexOfA = charReader.nextIndexOf("A", 5);
        Assert.assertEquals(6, indexOfA);

        char[] chars = {'a', 'b', 'c'};
        int indexOfCharSequence = charReader.nextIndexOf(chars);
        Assert.assertEquals(2, indexOfCharSequence);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "abcdefababcdef";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        String consumedToC = charReader.consumeTo('c');
        Assert.assertEquals("a", consumedToC);

        String consumedToAb = charReader.consumeTo("ab");
        Assert.assertEquals("abc", consumedToAb);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        String consumedToAny = charReader.consumeToAny(new char[]{'a', 'e'});
        Assert.assertEquals("aeb", consumedToAny);

        String consumedToAnySorted = charReader.consumeToAnySorted(new char[]{'z', 'y', 'x'});
        Assert.assertEquals("xyz", consumedToAnySorted);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        String input = "abcdef";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        String consumedToEnd = charReader.consumeToEnd();
        Assert.assertEquals("abcdef", consumedToEnd);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        String input = "&<null";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        String consumedData = charReader.consumeData();
        Assert.assertEquals("&<", consumedData);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        String input = "<html>\t<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>";
        Reader reader = new StringReader(input);
        CharacterReader charReader = new CharacterReader(reader);

        String consumedTagName = charReader.consumeTagName();
        Assert.assertEquals("<html>", consumedTagName);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        Reader reader = new StringReader("") {
            @Override
            public void mark(int readAheadLimit) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        CharacterReader charReader = new CharacterReader(reader);
        charReader.consume();
    }
}