package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader class.
 */
public final class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
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
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(5, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('f', reader.current());
        Assert.assertEquals('f', reader.consume());
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(6, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abc")) {
            @Override
            void bufferUp() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("12345abcdef"));
        String consumed = reader.consumeTo('5');
        Assert.assertEquals("123", consumed);
        Assert.assertEquals(5, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('5', reader.current());
        consumed = reader.consumeTo('x');
        Assert.assertEquals("5", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        consumed = reader.consumeTo("def");
        Assert.assertEquals("5abdef", consumed);
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        consumed = reader.consumeToEnd();
        Assert.assertEquals("5abdef", consumed);
        Assert.assertEquals(15, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("12345abcdef"));
        String consumed = reader.consumeToAny(new char[]{'5', 'a'});
        Assert.assertEquals("123", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        consumed = reader.consumeToAny(new char[]{'x', 'd'});
        Assert.assertEquals("", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        consumed = reader.consumeToAny(new char[]{EOF, 'x'});
        Assert.assertEquals("a", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        consumed = reader.consumeToAny(new char[]{'d', EOF});
        Assert.assertEquals("b", consumed);
        Assert.assertEquals(5, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
        consumed = reader.consumeToAny(new char[]{'e', 'f'});
        Assert.assertEquals("c", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        consumed = reader.consumeToAny(new char[]{EOF, 'g'});
        Assert.assertEquals("ef", consumed);
        Assert.assertEquals(8, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('f', reader.current());
        consumed = reader.consumeToAny(new char[]{EOF, EOF});
        Assert.assertEquals("ef", consumed);
        Assert.assertEquals(8, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("12345abcdef"));
        String consumed = reader.consumeToAnySorted(new char[]{'5', 'a'});
        Assert.assertEquals("123", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{'x', 'd'});
        Assert.assertEquals("", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{EOF, 'x'});
        Assert.assertEquals("a", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{'d', EOF});
        Assert.assertEquals("b", consumed);
        Assert.assertEquals(5, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{'e', 'f'});
        Assert.assertEquals("c", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('e', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{EOF, 'g'});
        Assert.assertEquals("ef", consumed);
        Assert.assertEquals(8, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('f', reader.current());
        consumed = reader.consumeToAnySorted(new char[]{EOF, EOF});
        Assert.assertEquals("ef", consumed);
        Assert.assertEquals(8, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;hello&amp;world"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&lt;hello&amp;", consumed);
        Assert.assertEquals(consumed.length(), reader.pos());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        consumed = reader.consumeTagName();
        Assert.assertEquals("head", consumed);
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        consumed = reader.consumeTagName();
        Assert.assertEquals("title", consumed);
        Assert.assertEquals(16, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        consumed = reader.consumeTagName();
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(3, reader.nextIndexOf('a'));
        Assert.assertEquals(7, reader.nextIndexOf('d'));
        Assert.assertEquals(10, reader.nextIndexOf('a'));
        Assert.assertEquals(12, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345abcdef"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
        Assert.assertEquals(0, reader.nextIndexOf("123"));
        Assert.assertEquals(5, reader.nextIndexOf("5"));
        Assert.assertEquals(11, reader.nextIndexOf("def"));
        Assert.assertEquals(15, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertFalse(reader.matchesIgnoreCase("Z"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertFalse(reader.matchesAny('z'));
        Assert.assertTrue(reader.matchesAny("xyz"));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesAnySorted('a'));
        Assert.assertTrue(reader.matchesAnySorted('b'));
        Assert.assertTrue(reader.matchesAnySorted('c'));
        Assert.assertFalse(reader.matchesAnySorted('z'));
        Assert.assertTrue(reader.matchesAnySorted("xyz"));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd12345"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd12345"));
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
    }
}