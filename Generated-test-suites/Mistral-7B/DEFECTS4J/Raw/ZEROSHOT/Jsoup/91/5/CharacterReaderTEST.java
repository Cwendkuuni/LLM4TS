package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals(7, reader.charBuf.length);
        reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(maxBufferLen, reader.charBuf.length);
        reader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertEquals(10, reader.bufSplitPoint);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        reader.advance();
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        reader.consume();
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals('c', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("ab") {
            @Override
            public int read(char[] cbuf, int offset, int length) throws IOException {
                throw new IOException("IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcde"));
        String result = reader.consumeTo('e');
        Assert.assertEquals("abcde", result);
        reader.consume();
        reader.consume();
        reader.mark();
        reader.rewindToMark();
        result = reader.consumeTo('e');
        Assert.assertEquals("abcde", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcde"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("abcdeabcde", result);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        String result = reader.consumeToAny(new char[]{'1', '2', '3'});
        Assert.assertEquals("123", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("543210"));
        String result = reader.consumeToAnySorted(new char[]{'1', '2', '3', '4', '5'});
        Assert.assertEquals("12345", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String result = reader.consumeData();
        Assert.assertEquals("&<null", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<Tag name=\"Value\"/>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("Tag", result);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("ABC"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'd'));
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("543210"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '3', '4', '5'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '5', '3', '4'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'y', 'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('a'));
        Assert.assertTrue(reader.matchesLetter('z'));
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('0'));
        Assert.assertTrue(reader.matchesDigit('9'));
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcde"));
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals("cdabcde", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEabcde"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals("cDEabcde", reader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcde"));
        String result = reader.consumeTo('e');
        String cachedResult = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, result.length());
        Assert.assertEquals(result, cachedResult);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcde"));
        Assert.assertTrue(reader.rangeEquals(reader.bufPos, reader.bufLength - reader.bufPos, reader.consumeToEnd()));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdeabcde"));
        String result = reader.consumeToEnd();
        Assert.assertEquals(result, reader.toString());
    }
}