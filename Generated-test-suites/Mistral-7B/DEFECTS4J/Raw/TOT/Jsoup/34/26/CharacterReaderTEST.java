package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest class contains JUnit test cases for CharacterReader class.
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyString() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testSingleCharInput() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals("b", reader.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals("def", reader.toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", consumed);
        Assert.assertEquals("def", reader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc#ffedf");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("abc#ff", consumed);
        Assert.assertEquals("edf", reader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("123", consumed);
        Assert.assertEquals("abcdef", reader.toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matches('H'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('X'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
        Assert.assertFalse(reader.matchesIgnoreCase("XXX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'e'));
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertEquals(7, reader.nextIndexOf('W'));
        Assert.assertEquals(1, reader.nextIndexOf('l'));
        Assert.assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        Assert.assertEquals(5, reader.nextIndexOf("ll"));
        Assert.assertEquals(-1, reader.nextIndexOf("XXX"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String consumed = reader.consumeTo('l');
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals("World", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("World", consumed);
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeToAny('a', 'b', 'c');
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consumeLetterSequence();
        reader.rewindToMark();
        Assert.assertEquals("abc", reader.toString());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        Assert.assertEquals("bcddef", reader.toString());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeAsString();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals("def", reader.toString());
    }
}