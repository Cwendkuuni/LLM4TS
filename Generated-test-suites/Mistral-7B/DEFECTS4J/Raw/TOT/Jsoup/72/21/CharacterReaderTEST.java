package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        reader.mark();
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        reader.unconsume();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abc") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Testing IOException");
            }
        });
        reader.bufferUp();
        reader.consume();
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String expected = "abcd";
        String result = reader.consumeTo('d');
        Assert.assertEquals(expected, result);
        Assert.assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String expected = "abcdabcdabcd";
        String result = reader.consumeToEnd();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(17, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("aabbbccccddd"));
        String expected = "bbcc";
        String result = reader.consumeToAny(new char[]{'b', 'c'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("aabbbccccddd"));
        String expected = "ccc";
        String result = reader.consumeToAnySorted(new char[]{'c', 'c', 'c'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(9, reader.pos());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name='attr' />"));
        String expected = "tag";
        String result = reader.consumeTagName();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;script src='script.js' type='text/javascript'&gt;"));
        String expected = "&lt;script src='script.js' type='text/javascript'&gt;";
        String result = reader.consumeData();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(25, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('e'));
        Assert.assertFalse(reader.matches("xyz"));
        reader.advance();
        Assert.assertFalse(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdABcdABCD"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
        reader.advance();
        Assert.assertFalse(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'b', 'd'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'e', 'x'}));
        reader.advance();
        Assert.assertFalse(reader.matchesAny(new char[]{'a'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'d'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'd'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'd', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e', 'x'}));
        reader.advance();
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'a'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d'}));
    }
}