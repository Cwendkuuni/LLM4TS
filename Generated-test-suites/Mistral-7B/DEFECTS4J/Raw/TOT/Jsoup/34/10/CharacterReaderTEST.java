package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals("abc", reader.consumeTo('c'));
        Assert.assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testScanString() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertEquals(-1, reader.nextIndexOf("zello"));
        Assert.assertEquals(5, reader.nextIndexOf(" world"));
        Assert.assertEquals("hello", reader.consumeTo('l'));
        Assert.assertEquals(" world", reader.consumeToEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testScanCharSequence() {
        CharacterReader reader = new CharacterReader("123abcd123efg");
        Assert.assertEquals(4, reader.nextIndexOf("123"));
        Assert.assertEquals(11, reader.nextIndexOf("efg"));
        Assert.assertEquals("123", reader.consumeTo('3'));
        Assert.assertEquals("efg", reader.consumeToEnd());
    }

    @Test
    public void testScanAny() {
        CharacterReader reader = new CharacterReader("ab12cde3FG");
        Assert.assertEquals(1, reader.nextIndexOf((char) 1));
        Assert.assertEquals(5, reader.nextIndexOf((char) 'd'));
        Assert.assertEquals("ab", reader.consumeToAny((char) 1, (char) 'd'));
        Assert.assertEquals("3FG", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ello"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("zello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertTrue(reader.matchesIgnoreCase("ello"));
        Assert.assertFalse(reader.matchesIgnoreCase("zello"));
        Assert.assertFalse(reader.matchesIgnoreCase("Xello"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("123abcd123efg");
        Assert.assertTrue(reader.matchesAny((char) 1, 'd'));
        Assert.assertFalse(reader.matchesAny((char) 4, 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("hello world");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abcd123efg");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
    }
}