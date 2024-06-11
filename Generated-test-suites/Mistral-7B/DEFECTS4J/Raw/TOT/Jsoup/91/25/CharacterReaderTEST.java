package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.reader.mark(1);
        reader.reader.skip(1); // throw IOException
        reader.bufferUp();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcd"));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(2, reader.nextIndexOf('b'));
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(6, reader.nextIndexOf('a'));
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ababcdabcd"));
        Assert.assertEquals(-1, reader.nextIndexOf("efg"));
        Assert.assertEquals(0, reader.nextIndexOf("ab"));
        Assert.assertEquals(4, reader.nextIndexOf("cd"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        String result = reader.consumeTo('c');
        Assert.assertEquals("ab", result);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abcde", result);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefabcdef"));
        String result = reader.consumeToAny('e');
        Assert.assertEquals("abcd", result);

        reader.mark();
        reader.advance();
        reader.rewindToMark();

        result = reader.consumeToAny('e');
        Assert.assertEquals("bcdefabcdef", reader.toString());
        Assert.assertEquals("b", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefabcdef"));
        String result = reader.consumeToAnySorted('d');
        Assert.assertEquals("abce", result);

        reader.mark();
        reader.advance();
        reader.rewindToMark();

        result = reader.consumeToAnySorted('d');
        Assert.assertEquals("bcdefabcdef", reader.toString());
        Assert.assertEquals("b", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;abcde&gt;"));
        String result = reader.consumeData();
        Assert.assertEquals("&lt;", result);
        Assert.assertEquals('<', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader(" &lt;tag&gt; "));
        String result = reader.consumeTagName();
        Assert.assertEquals("tag", result);
        Assert.assertEquals(EOF, reader.current());
    }
}