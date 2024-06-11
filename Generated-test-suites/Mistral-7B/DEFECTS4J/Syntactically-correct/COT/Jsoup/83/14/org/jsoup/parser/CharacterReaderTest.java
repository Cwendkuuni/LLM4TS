package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        Assert.assertEquals("TestString", characterReader.toString());
        Assert.assertEquals(14, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("TestString");
        Assert.assertEquals("TestString", characterReader.toString());
        Assert.assertEquals(14, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        characterReader.bufferUp();
        Assert.assertEquals(22, characterReader.pos());
        Assert.assertEquals(17, characterReader.readerPos);
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertEquals('T', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('s', characterReader.current());
        characterReader.consumeToEnd();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        characterReader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertEquals('T', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('s', characterReader.current());
        characterReader.consumeToEnd();
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        String consumed = characterReader.consumeTo('s');
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("TestStringTestString", consumed);
        Assert.assertEquals(14, characterReader.pos());
        Assert.assertEquals(24, characterReader.readerPos);
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        String consumed = characterReader.consumeToAny(',');
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);

        consumed = characterReader.consumeToAny(new char[]{'s', 't'});
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);

        consumed = characterReader.consumeToAny(new char[]{'s', 't', 'r'});
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);

        consumed = characterReader.consumeToAny(new char[]{'}', ']'});
        Assert.assertEquals("TestStringTest", consumed);
        Assert.assertEquals(8, characterReader.pos());
        Assert.assertEquals(14, characterReader.readerPos);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        String consumed = characterReader.consumeToAnySorted(new char[]{'>', '<'});
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);

        consumed = characterReader.consumeToAnySorted(new char[]{'>', '<', 's'});
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);

        consumed = characterReader.consumeToAnySorted(new char[]{'s', '>', '<'});
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);

        consumed = characterReader.consumeToAnySorted(new char[]{'s', '>', '<', 't'});
        Assert.assertEquals("TestString", consumed);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertEquals(10, characterReader.readerPos);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;Test&amp;String&gt;TestString"));
        String consumed = characterReader.consumeData();
        Assert.assertEquals("&lt;Test&amp;String&gt;", consumed);
        Assert.assertEquals(16, characterReader.pos());
        Assert.assertEquals(26, characterReader.readerPos);
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Test</title>"));
        String consumed = characterReader.consumeTagName();
        Assert.assertEquals("title", consumed);
        Assert.assertEquals(6, characterReader.pos());
        Assert.assertEquals(11, characterReader.readerPos);
        Assert.assertEquals('/', characterReader.current());
    }

    @Test
    public void testConsumeToEndWithEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("", consumed);
        Assert.assertEquals(0, characterReader.pos());
        Assert.assertEquals(0, characterReader.readerPos);
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Test", consumed);
        Assert.assertEquals(4, characterReader.pos());
        Assert.assertEquals(9, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123StringTest123"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Test123", consumed);
        Assert.assertEquals(6, characterReader.pos());
        Assert.assertEquals(16, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test&#x123;StringTest&#x123;"));
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("&#x123;", consumed);
        Assert.assertEquals(7, characterReader.pos());
        Assert.assertEquals(13, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123StringTest123"));
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", consumed);
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertEquals(8, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matches('s'));
        Assert.assertTrue(characterReader.matches("es"));
        Assert.assertFalse(characterReader.matches('z'));
        Assert.assertFalse(characterReader.matches("/"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("Es"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("es"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Z"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("/"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matchesAny('t'));
        Assert.assertTrue(characterReader.matchesAny('s'));
        Assert.assertFalse(characterReader.matchesAny('z'));
        Assert.assertTrue(characterReader.matchesAny('/'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'s', 't'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'t', 's'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'}', ']'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123StringTest123"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matchConsume("es"));
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertEquals(8, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("Es"));
        Assert.assertEquals(3, characterReader.pos());
        Assert.assertEquals(8, characterReader.readerPos);
        Assert.assertEquals('s', characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body><p>Test</p></body></html>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/head"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/body"));
    }

    // other methods are not testable in isolation as they depend on the internal state of CharacterReader

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        Assert.assertEquals("TestStringTestString", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringTestString"));
        String cached = characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, characterReader.pos, characterReader.pos + 5);
        Assert.assertEquals("TestString", cached);

        cached = characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, characterReader.pos + 5, characterReader.pos + 10);
        Assert.assertEquals("TestStringTest", cached);

        cached = characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, characterReader.pos + 10, characterReader.pos + 15);
        Assert.assertEquals("TestStringTestString", cached);
    }
}