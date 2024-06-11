package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
    }

    @Test
    public void testSimpleCharacterReader() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdefg".getBytes())));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.advance();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ThrowingInputStream("IOException")));
        reader.advance();
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertEquals("abcd", reader.consumeTo('d'));
        Assert.assertEquals("abcdabcd", reader.toString());
        Assert.assertEquals("abcd", reader.consumeTo('d'));
        Assert.assertEquals("abcdabcd", reader.toString());
        Assert.assertEquals("", reader.consumeTo('z'));
        Assert.assertEquals("abcdabcd", reader.toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abababcdabcdabcd".getBytes())));
        Assert.assertEquals("ab", reader.consumeToAny(new char[]{'a','b'}));
        Assert.assertEquals("abababcdabcdabcd", reader.toString());
        Assert.assertEquals("bcdabcd", reader.consumeToAny(new char[]{'c','d'}));
        Assert.assertEquals("", reader.consumeToAny(new char[]{'x','y'}));
        Assert.assertEquals("abababcdabcdabcd", reader.toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abababcdabcdabcd".getBytes())));
        Assert.assertEquals("ab", reader.consumeToAnySorted(new char[]{'a','b'}));
        Assert.assertEquals("abababcdabcdabcd", reader.toString());
        Assert.assertEquals("bcd", reader.consumeToAnySorted(new char[]{'c','d'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'x','y'}));
        Assert.assertEquals("abababcdabcdabcd", reader.toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("&<null".getBytes())));
        Assert.assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("<tag name=\"John\".\r\n/>".getBytes())));
        Assert.assertEquals("<tag", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertEquals("abcdabcdabcdabcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdABCeFg".getBytes())));
        Assert.assertEquals("abcd", reader.consumeLetterSequence());
        Assert.assertEquals("ABCeFg", reader.toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("ab123cd123efg456".getBytes())));
        Assert.assertEquals("ab123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("cd123efg456", reader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcd\u00E5fghij".getBytes())));
        Assert.assertEquals("abcd\u00E5", reader.consumeHexSequence());
        Assert.assertEquals("fghij", reader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("12345678901234567890123456".getBytes())));
        Assert.assertEquals("123456", reader.consumeDigitSequence());
        Assert.assertEquals("78901234567890123456", reader.toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches("cd"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("ef"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("ABCDabcdeFGhIj".getBytes())));
        Assert.assertTrue(reader.matchesIgnoreCase("CD"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("XY"));
        Assert.assertFalse(reader.matchesIgnoreCase("AB"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertTrue(reader.matchesAny('d'));
        Assert.assertTrue(reader.matchesAny("cd"));
        Assert.assertFalse(reader.matchesAny('z'));
        Assert.assertFalse(reader.matchesAny("ef"));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertTrue(reader.matchesAnySorted('d'));
        Assert.assertTrue(reader.matchesAnySorted("cd"));
        Assert.assertTrue(reader.matchesAnySorted('c'));
        Assert.assertFalse(reader.matchesAnySorted('z'));
        Assert.assertFalse(reader.matchesAnySorted('e'));
        Assert.assertFalse(reader.matchesAnySorted('x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("12345678901234567890123456".getBytes())));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("abcdabcdabcdabcd".getBytes())));
        Assert.assertTrue(reader.matchConsume("cd"));
        Assert.assertEquals("abcdabcdabcdabcd", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("ABCDabcdeFGhIj".getBytes())));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("EF"));
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>".getBytes())));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/h1"));
        Assert.assertFalse(reader.containsIgnoreCase("/footer"));
    }

}