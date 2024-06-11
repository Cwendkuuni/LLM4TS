package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals(-1, reader.nextIndexOf('b'));
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals(-1, reader.nextIndexOf('d'));
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("ac", reader.consumeTo('c'));
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderWithSurrogateCharacters() {
        // This test case is complex and might require handling surrogate characters properly
        // and testing nextIndexOf and consumeTo methods accordingly.
        String input = "\uD800\uDC00";
        CharacterReader reader = new CharacterReader(input);
        // Add appropriate assertions and test cases here
    }

    @Test
    public void testCharacterReaderMethods() {
        CharacterReader reader = new CharacterReader("123abcdEFG456");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('1', reader.current());
        Assert.assertEquals('1', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('2', reader.current());
        // Test other methods and their combinations as needed
    }

    @Test
    public void testCharacterReaderWithConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderWithConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", consumed);
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testCharacterReaderWithConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234abcd56ef");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("1234", consumed);
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReaderWithConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("123456", consumed);
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testCharacterReaderWithMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches('e'));
        Assert.assertTrue(reader.matches("ef"));
        Assert.assertFalse(reader.matches('g'));
        Assert.assertFalse(reader.matches("gh"));
    }

    @Test
    public void testCharacterReaderWithMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertTrue(reader.matchesIgnoreCase("AB"));
        Assert.assertFalse(reader.matchesIgnoreCase("gh"));
    }

    @Test
    public void testCharacterReaderWithMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'e'));
        Assert.assertFalse(reader.matchesAny('g', 'h'));
    }

    @Test
    public void testCharacterReaderWithContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Page</title></head></html>");
        Assert.assertTrue(reader.containsIgnoreCase("/title/"));
        Assert.assertTrue(reader.containsIgnoreCase("/head/"));
        Assert.assertFalse(reader.containsIgnoreCase("/body/"));
    }

    @Test
    public void testCharacterReaderWithMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.advance();
        String before = reader.consumeAsString();
        reader.rewindToMark();
        String after = reader.consumeAsString();
        Assert.assertEquals(before, after);
    }
}