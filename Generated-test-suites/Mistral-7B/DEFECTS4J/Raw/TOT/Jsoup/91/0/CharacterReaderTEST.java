package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testStringReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("def", reader.consumeToEnd());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testReaderWithBytes() {
        CharacterReader reader = new CharacterReader(new StringReader("\u00A0\u00FF"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('\u00A0', reader.current());
        Assert.assertEquals(1, reader.nextIndexOf('\u00A0'));
        Assert.assertEquals("\u00A0\u00FF", reader.consumeToEnd());
        reader.rewindToMark();
        Assert.assertEquals('\u00A0', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Thrown for test");
            }
        });
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testMarkAndRewindMultipleTimes() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.rewindToMark();
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.rewindToMark();
        reader.advance();
        Assert.assertEquals('c', reader.current());
        reader.rewindToMark();
        reader.advance();
        Assert.assertEquals('c', reader.current());
        reader.rewindToMark();
        reader.advance(2);
        Assert.assertEquals('e', reader.current());
        reader.rewindToMark();
        reader.consumeToEnd();
        Assert.assertEquals(6, reader.pos());
        reader.rewindToMark();
        reader.consumeTo('f');
        Assert.assertEquals(4, reader.pos());
        reader.rewindToMark();
        reader.consumeTo('e');
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        Assert.assertEquals("", reader.consumeToAny(new char[0]));
        Assert.assertEquals("123", reader.consumeToAny(new char[]{'1', '2', '3'}));
        Assert.assertEquals("23def", reader.consumeToAny(new char[]{'2', '3'}));
        Assert.assertEquals("def", reader.consumeToAny(new char[]{'d', 'e', 'f'}));
        Assert.assertEquals("def", reader.consumeToAnySorted(new char[]{'d', 'e', 'f'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><script src=\"script.js\"></script></body></html>"));
        Assert.assertEquals("script.js", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><script src=\"script.js\"></script></body></html>"));
        Assert.assertEquals("script", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><body><script src=\"script.js\"></script></body></html>"));
        Assert.assertEquals("<html><body><script src=\"script.js\"></script></body></html>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        Assert.assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        Assert.assertEquals("ab123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab\u0061cd\u0062ef"));
        Assert.assertEquals("abcd", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        Assert.assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("eF"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'b', 'c'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x', 'y'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'b', 'c', 'a'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x', 'y'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals("ab", reader.matchConsume("ab"));
        Assert.assertEquals("ef", reader.matchConsume("ef"));
        Assert.assertEquals("", reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertEquals("ab", reader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals("ef", reader.matchConsumeIgnoreCase("ef"));
        Assert.assertEquals("", reader.matchConsumeIgnoreCase("XYZ"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
        Assert.assertTrue(reader.containsIgnoreCase("/p"));
        Assert.assertTrue(reader.containsIgnoreCase("/P"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }
}