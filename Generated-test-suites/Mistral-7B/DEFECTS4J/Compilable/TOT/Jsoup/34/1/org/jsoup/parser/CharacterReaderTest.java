package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testMultipleCharacterReader() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("abc", reader.consumeToEnd());
        Assert.assertEquals("abc", reader.toString());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testNextIndexOfLetter() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(2, reader.nextIndexOf('c'));
        Assert.assertEquals(3, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(2, reader.nextIndexOf("bc"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeToLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertEquals("a", reader.consumeTo('b'));
        Assert.assertEquals("b", reader.consume());
        Assert.assertEquals("cd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals("abc", reader.consumeTo("bc"));
        Assert.assertEquals("d", reader.consume());
        Assert.assertEquals("e", reader.consume());
        Assert.assertEquals("f", reader.consume());
        Assert.assertEquals("g", reader.consume());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234abcd");
        Assert.assertEquals("123", reader.consumeToAny('1', '2'));
        Assert.assertEquals("34", reader.consumeToAny('3', '4'));
        Assert.assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("123456");
        Assert.assertTrue(reader.matchesAny('1', '2', '3', '4', '5', '6'));
        Assert.assertFalse(reader.matchesAny('7'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals(0, reader.pos());
    }
}