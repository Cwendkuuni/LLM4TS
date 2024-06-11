package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest class for testing CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testMultipleCharactersReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
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
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("")) {
            @Override
            public void bufferUp() {
                throw new IOException("IOException occurred");
            }
        };
        reader.consume();
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        Assert.assertEquals(3, reader.nextIndexOf('b'));
        Assert.assertEquals(6, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        Assert.assertEquals(3, reader.nextIndexOf("ba"));
        Assert.assertEquals(6, reader.nextIndexOf("ab"));
        Assert.assertEquals(-1, reader.nextIndexOf("cd"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        String expected = "ba";
        Assert.assertEquals(expected, reader.consumeTo('b'));
        Assert.assertEquals("bc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ababc"));
        String expected = "ab";
        Assert.assertEquals(expected, reader.consumeTo("ab"));
        Assert.assertEquals("bc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeToAny(new char[]{'b', 'c'}));
        Assert.assertEquals("d", reader.consumeToAny(new char[]{'d'}));
        Assert.assertEquals("", reader.consumeToAny(new char[]{'x'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeToAnySorted(new char[]{'b', 'c'}));
        Assert.assertEquals("d", reader.consumeToAnySorted(new char[]{'d'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'x'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd&efgh"));
        String expected = "&efgh";
        Assert.assertEquals(expected, reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name=\"attribute\"/>"));
        String expected = "tag";
        Assert.assertEquals(expected, reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String expected = "def";
        Assert.assertEquals(expected, reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        String expected = "def";
        Assert.assertEquals(expected, reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc#x123def"));
        String expected = "def";
        Assert.assertEquals(expected, reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        String expected = "3";
        Assert.assertEquals(expected, reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches("d"));
        Assert.assertFalse(reader.matches('e'));
        Assert.assertFalse(reader.matches("e"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("D"));
        Assert.assertFalse(reader.matchesIgnoreCase("e"));
        Assert.assertFalse(reader.matchesIgnoreCase("E"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('b', 'a'));
        Assert.assertFalse(reader.matchesAny('e', 'a'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String expected = "cd";
        Assert.assertEquals(expected, reader.matchConsume("cd"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String expected = "cd";
        Assert.assertEquals(expected, reader.matchConsumeIgnoreCase("CD"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test Title</title></head><body></body></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
        Assert.assertFalse(reader.containsIgnoreCase("/BODY"));
    }
}