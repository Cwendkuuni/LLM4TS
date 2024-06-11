package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.consume();
    }

    @Test
    public void testCharacterReaderReadAhead() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.bufferUp();
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('b', reader.peek());
        Assert.assertEquals('c', reader.peek(2));
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        String consumed = reader.consumeTo('c');
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals('d', reader.current());

        consumed = reader.consumeTo('x');
        Assert.assertEquals("", consumed);
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("123456"));
        String consumed = reader.consumeToAny(',', '.');
        Assert.assertEquals("123", consumed);

        consumed = reader.consumeToAny(new char[]{'5', '6'});
        Assert.assertEquals("56", consumed);

        consumed = reader.consumeToAnySorted(new char[]{'6', '5'});
        Assert.assertEquals("56", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&lt;", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("<tag>", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello12345"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", consumed);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x1A"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("0x1A", consumed);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("def"));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesIgnoreCase("def"));
        Assert.assertTrue(reader.matchesIgnoreCase("ABC"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('f'));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'f', 'a'}));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('a'));
        Assert.assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('5'));
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchConsume("def"));
        Assert.assertEquals("def", reader.toString());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("DEF"));
        Assert.assertEquals("DEF", reader.toString());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>My Title</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/HEAD"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }
}