package org.jsoup.parser; // make sure the test package matches the class package

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CharacterReaderTest class
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(input, characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals(input, characterReader.toString());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderIsNotEmpty() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("Hello World!".getBytes())) {
            @Override
            public void mark(int readlimit) throws IOException {
                throw new IOException("Simulated mark exception.");
            }
        });
    }

    @Test
    public void testCharacterReaderCurrent() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.unconsume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        String input = "abababcdababcde";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(1, characterReader.nextIndexOf('a'));
        Assert.assertEquals(5, characterReader.nextIndexOf('b'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithCharSequence() {
        String input = "abababcdababcde";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(1, characterReader.nextIndexOf("ab"));
        Assert.assertEquals(5, characterReader.nextIndexOf("ba"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("cd"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        String expected = "Hello ";
        String result = characterReader.consumeTo(' ');
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeToWithLongInput() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        String expected = "Hello World!";
        String result = characterReader.consumeToEnd();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String input = "abcdabcdabcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals("abcd", characterReader.consumeToAny('a', 'b', 'c', 'd'));
        Assert.assertEquals("cdabcd", characterReader.consumeToAny('c', 'd'));
        Assert.assertEquals("", characterReader.consumeToAny('z'));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        String input = "abcdabcdabcdabcdabcd";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals("abcd", characterReader.consumeToAnySorted('a', 'b', 'c', 'd'));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        String input = "&<null";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        String expected = "&<null";
        String result = characterReader.consumeData();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        String input = "<title>Hello World!</title>";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        String expected = "<title>";
        String result = characterReader.consumeTagName();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCharacterReaderToString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        Assert.assertEquals(input, characterReader.toString());
    }
}