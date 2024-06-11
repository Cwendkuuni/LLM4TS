package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        Assert.assertEquals("abcdef", reader.toString());

        reader.bufferUp();
        Assert.assertEquals("def", reader.consumeToEnd());
        Assert.assertEquals("abc", reader.toString());

        reader.bufferUp();
        Assert.assertEquals("c", reader.consume());
        Assert.assertEquals("ab", reader.toString());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());

        reader.advance();
        Assert.assertEquals(1, reader.pos());

        reader.consume();
        Assert.assertEquals(2, reader.pos());

        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(EOF, reader.consume());

        reader.rewindToMark();
        reader.mark();
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('a', reader.current());

        reader.consume();
        reader.consume();
        reader.unconsume();
        reader.unconsume();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.advance();
        Assert.assertEquals('b', reader.current());

        reader.advance();
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(2, reader.nextIndexOf('c'));
        Assert.assertEquals(3, reader.nextIndexOf('d'));
        Assert.assertEquals(6, reader.nextIndexOf('h'));
        Assert.assertEquals(7, reader.nextIndexOf('i'));
        Assert.assertEquals(8, reader.nextIndexOf('j'));
        Assert.assertEquals(9, reader.nextIndexOf('k'));
        Assert.assertEquals(10, reader.nextIndexOf('k'));
        Assert.assertEquals(11, reader.nextIndexOf('k'));
        Assert.assertEquals(11, reader.nextIndexOf('k', 11));
        Assert.assertEquals(EOF, reader.nextIndexOf('k', 12));

        reader = new CharacterReader(new StringReader("abcdefghijk\tk"));
        Assert.assertEquals(12, reader.nextIndexOf('\t'));
        Assert.assertEquals(EOF, reader.nextIndexOf('\t', 13));
    }

    @Test
    public void testNextIndexOfWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
        Assert.assertEquals(1, reader.nextIndexOf("bc"));
        Assert.assertEquals(2, reader.nextIndexOf("cd"));
        Assert.assertEquals(3, reader.nextIndexOf("ef"));
        Assert.assertEquals(6, reader.nextIndexOf("hijk"));
        Assert.assertEquals(7, reader.nextIndexOf("hik"));
        Assert.assertEquals(8, reader.nextIndexOf("ijk"));
        Assert.assertEquals(9, reader.nextIndexOf("jk"));
        Assert.assertEquals(10, reader.nextIndexOf("jk"));
        Assert.assertEquals(10, reader.nextIndexOf("jk", 10));
        Assert.assertEquals(EOF, reader.nextIndexOf("k", 12));

        reader = new CharacterReader(new StringReader("abcdefghijk\tk"));
        Assert.assertEquals(12, reader.nextIndexOf("\t"));
        Assert.assertEquals(EOF, reader.nextIndexOf("\t", 13));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals("bcd", reader.consumeTo('c'));
        Assert.assertEquals("def", reader.consumeTo('e'));
        Assert.assertEquals("hijk", reader.consumeTo('k'));
        Assert.assertEquals("", reader.consumeTo('z'));
        Assert.assertEquals("", reader.consumeToAny('z'));
        Assert.assertEquals("hik", reader.consumeToAnySorted('h', 'i', 'k'));
        Assert.assertEquals("gk", reader.consumeToAnySorted('g', 'k'));
        Assert.assertEquals("g", reader.consumeData());
        Assert.assertEquals(" \r\n<title>\n", reader.consumeTagName());
        Assert.assertEquals("abcdefghijk\tk", reader.consumeToEnd());

        reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals("", reader.consumeTo('z'));
        Assert.assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        String cached = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
        Assert.assertEquals("hijk", cached);
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        Assert.assertEquals("abcdefghijk", reader.toString());
    }
}