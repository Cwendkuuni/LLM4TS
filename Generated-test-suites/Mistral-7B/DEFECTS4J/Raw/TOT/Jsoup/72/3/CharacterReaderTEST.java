package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf("ab"));
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals("a", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals("abc", reader.consumeToEnd());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abc") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        reader.bufferUp();
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.bufferUp();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(2, reader.nextIndexOf('c'));
        Assert.assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abab"));
        Assert.assertEquals(0, reader.nextIndexOf("ab"));
        Assert.assertEquals(4, reader.nextIndexOf("ba"));
        Assert.assertEquals(-1, reader.nextIndexOf("cd"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("cd", reader.consumeTo('d'));
        Assert.assertEquals("", reader.consumeTo('e'));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeToAny(new char[]{'b', 'c'}));
        Assert.assertEquals("abcd", reader.consumeToAny(new char[]{'e'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("bc", reader.consumeToAnySorted(new char[]{'b', 'c'}));
        Assert.assertEquals("abcd", reader.consumeToAnySorted(new char[]{'a', 'd'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        Assert.assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("   <title>Hello World</title>"));
        Assert.assertEquals("title", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&#x61;&#x62;&#x63;"));
        Assert.assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234"));
        Assert.assertEquals("1234", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('d'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("ABC"));
        Assert.assertFalse(reader.matchesIgnoreCase("def"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertFalse(reader.matchesAny('d', 'e', 'f'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'d', 'e', 'f'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("ab"));
        Assert.assertTrue(reader.matchConsume("ab"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Ab"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }
}