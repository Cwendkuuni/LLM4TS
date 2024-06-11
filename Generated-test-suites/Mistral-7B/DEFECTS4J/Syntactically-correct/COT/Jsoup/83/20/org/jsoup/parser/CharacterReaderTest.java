package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for CharacterReader.
 */
public final class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("abcdefg".getBytes()));
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        reader.advance();
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals("abcdefg", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderWithEmptyInput() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(new byte[0]));
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("ab".getBytes()), "invalid-encoding")) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("abcdabcdabcd".getBytes()));
        String s = reader.consumeTo('d');
        Assert.assertEquals("ab", s);
        s = reader.consumeTo('d');
        Assert.assertEquals("cd", s);
        s = reader.consumeTo('d');
        Assert.assertEquals("abcd", s);
        s = reader.consumeToEnd();
        Assert.assertEquals("abcdabcdabcd", s);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("abcdefg".getBytes()));
        String s = reader.consumeToAny(',', 'a', 'b', 'c');
        Assert.assertEquals("", s);
        s = reader.consumeToAny(',');
        Assert.assertEquals("a", s);
        s = reader.consumeToAny('g');
        Assert.assertEquals("g", s);
        s = reader.consumeToAnySorted(new char[]{'b', 'a', 'c'});
        Assert.assertEquals("ab", s);
        s = reader.consumeToAnySorted(new char[]{'g', 'f', 'e'});
        Assert.assertEquals("gfe", s);
    }

    @Test
    public void testCharacterReaderConsumeToTagName() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("  <title>Test</title>  ".getBytes()));
        String s = reader.consumeToTagName();
        Assert.assertEquals("<title>", s);
        reader.advance();
        s = reader.consumeToTagName();
        Assert.assertEquals("Test", s);
        reader.advance();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("abcdef&<null>&".getBytes()));
        String s = reader.consumeData();
        Assert.assertEquals("abcdef&<null>", s);
    }
}