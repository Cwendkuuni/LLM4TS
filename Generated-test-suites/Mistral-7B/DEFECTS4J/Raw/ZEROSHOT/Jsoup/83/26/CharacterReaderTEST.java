package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
        reader.consume();
        Assert.assertEquals('d', reader.current());
        reader.consume();
        Assert.assertEquals(-1, reader.current());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.mark();
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.consume();
        Assert.assertEquals('c', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(new byte[]{-1})));
        reader.consume();
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(4, reader.nextIndexOf('e'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(2, reader.nextIndexOf("bc"));
        Assert.assertEquals(-1, reader.nextIndexOf("ef"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeTo('c'));
        Assert.assertEquals("d", reader.consume());
        Assert.assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeTo("bc"));
        Assert.assertEquals("d", reader.consume());
        Assert.assertEquals("", reader.consumeTo("z"));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("cd", reader.consumeToAny(new char[]{'c', 'd'}));
        Assert.assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("cd", reader.consumeToAnySorted(new char[]{'c', 'd'}));
        Assert.assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;script&gt;alert('Hello World')&lt;/script&gt;"));
        Assert.assertEquals("&lt;script&gt;alert('Hello World')&lt;/script&gt;", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertEquals("html", reader.consumeTagName());
        Assert.assertEquals("head", reader.consumeTagName());
        Assert.assertEquals("title", reader.consumeTagName());
        Assert.assertEquals("body", reader.consumeTagName());
        Assert.assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("abcd", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("efg", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab123cd45ef"));
        Assert.assertEquals("ab123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("cd45ef", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab%61%63%61%6d%65"));
        Assert.assertEquals("abacde", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertEquals("12345", reader.consumeDigitSequence());
        Assert.assertEquals("67890", reader.consumeDigitSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertTrue(reader.matches("cd"));
        Assert.assertFalse(reader.matches("ef"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("cd"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertFalse(reader.matchesAny('e', 'f'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e', 'f'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("ab", reader.matchConsume("ab"));
        Assert.assertEquals("cd", reader.matchConsume("cd"));
        Assert.assertEquals("", reader.matchConsume("ef"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("ab", reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals("cd", reader.matchConsumeIgnoreCase("cd"));
        Assert.assertEquals("", reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/body"));
        Assert.assertFalse(reader.containsIgnoreCase("/header"));
    }
}