package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CharacterReaderTest {
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcde");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("b", reader.consumeAsString());
        Assert.assertEquals("c", reader.consumeAsString());
        Assert.assertEquals("d", reader.consumeAsString());
        Assert.assertEquals("e", reader.consumeAsString());
        Assert.assertEquals("f", reader.consumeAsString());
        Assert.assertEquals(CharacterReader.EOF, reader.consumeAsString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOfSingleChar() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('g'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("123abcde123");;
        Assert.assertEquals(4, reader.nextIndexOf("de"));
        Assert.assertEquals(-1, reader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertEquals("d", reader.consumeTo('d').toString());
        Assert.assertEquals("eabcde", reader.consumeTo('e').toString());
        Assert.assertEquals("abcde", reader.consumeTo('a').toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertEquals("abcdeabcde", reader.consumeToEnd().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123abcd123");;
        Assert.assertEquals("1", reader.consumeToAny('1').toString());
        Assert.assertEquals("d", reader.consumeToAny('d').toString());
        Assert.assertEquals("", reader.consumeToAny('z').toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcd123abcd123");;
        Assert.assertEquals("1", reader.consumeToAnySorted(new char[]{'1', 'd'}).toString());
        Assert.assertEquals("d", reader.consumeToAnySorted(new char[]{'d', '1'}).toString());
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'z', '1'}).toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        Assert.assertEquals("&", reader.consumeData().toString());
        Assert.assertEquals("<", reader.consumeData().toString());
        Assert.assertEquals("null", reader.consumeData().toString());
        Assert.assertEquals(CharacterReader.EOF, reader.consumeData().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(" <title>Test</title> ");
        Assert.assertEquals("title", reader.consumeTagName().toString());
        Assert.assertEquals("", reader.consumeTagName().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", reader.consumeLetterSequence().toString());
        Assert.assertEquals("", reader.consumeLetterSequence().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence().toString());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&#x61;&#x62;&#x63;");
        Assert.assertEquals("abc", reader.consumeHexSequence().toString());
        Assert.assertEquals("", reader.consumeHexSequence().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertEquals("12345", reader.consumeDigitSequence().toString());
        Assert.assertEquals("", reader.consumeDigitSequence().toString());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("D"));
        Assert.assertFalse(reader.matchesIgnoreCase("g"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertTrue(reader.matchesAny('d'));
        Assert.assertTrue(reader.matchesAny('e'));
        Assert.assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'e'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'g', 'd'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }
}