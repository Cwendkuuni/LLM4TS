package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(3, reader.nextIndexOf("cd"));
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] c, int off, int len) throws IOException {
                throw new IOException("Simulated IOException");
            }
        });
        reader.advance();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.mark();
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        reader.rewindToMark();
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('d');
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('e', reader.current());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToSubstring() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String consumed = reader.consumeTo("def");
        Assert.assertEquals("abcdef", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals('g', reader.current());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdefg", consumed);
        Assert.assertEquals(7, reader.pos());
        Assert.assertEquals('\u0000', reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String consumed = reader.consumeToAny(new char[]{'e', 'f'});
        Assert.assertEquals("abcdghijklmnopqrstuvwxyz", consumed);
        Assert.assertEquals(15, reader.pos());
        Assert.assertEquals('x', reader.current());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        String consumed = reader.consumeToAnySorted(new char[]{'m', 'n'});
        Assert.assertEquals("abcdghijklopqrstuvwxyz", consumed);
        Assert.assertEquals(14, reader.pos());
        Assert.assertEquals('w', reader.current());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<abcdefg"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&<", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tab>\n<title>My Title</title>\n"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("title", consumed);
        Assert.assertEquals(8, reader.pos());
        Assert.assertEquals('M', reader.current());
        Assert.assertFalse(reader.isEmpty());
    }
}