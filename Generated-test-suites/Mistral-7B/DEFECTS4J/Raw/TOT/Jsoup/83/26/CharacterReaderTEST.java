package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals('C', reader.consume());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testConsumeToCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo('F');
        Assert.assertEquals("ABCDE", consumed);
        Assert.assertEquals('F', reader.current());
    }

    @Test
    public void testConsumeToCharacterWithEndOfStream() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo('Z');
        Assert.assertEquals("ABCDEFG", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo("DE");
        Assert.assertEquals("AB", consumed);
        Assert.assertEquals('F', reader.current());
    }

    @Test
    public void testConsumeToStringWithEndOfStream() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeTo("GH");
        Assert.assertEquals("ABCDEFG", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAnyCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAny('A', 'B', 'C');
        Assert.assertEquals("", consumed);
        Assert.assertEquals('E', reader.current());
    }

    @Test
    public void testConsumeToAnyCharacterWithEndOfStream() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAny('Z');
        Assert.assertEquals("ABCDEFG", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToAnySortedCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAnySorted(new char[] {'A', 'B', 'C'});
        Assert.assertEquals("", consumed);
        Assert.assertEquals('E', reader.current());
    }

    @Test
    public void testConsumeToAnySortedCharacterWithEndOfStream() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAnySorted(new char[] {'Z'});
        Assert.assertEquals("ABCDEFG", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&lt;", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals('/', reader.current());
    }

    @Test
    public void testConsumeTagNameWithEndOfStream() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("html", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("ABCDEFG", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcABC123"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
        Assert.assertTrue(reader.matches('A'));
        Assert.assertTrue(reader.matches('B'));
        Assert.assertTrue(reader.matches('C'));
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("a1b2c3"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("a1", consumed);
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('2'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('3'));
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&x0F"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("xF", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("123", consumed);
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matches('A'));
        Assert.assertTrue(reader.matches('B'));
        Assert.assertTrue(reader.matches('C'));
        Assert.assertTrue(reader.matches('D')); // should be false
        Assert.assertFalse(reader.matches('E'));
        Assert.assertFalse(reader.matches('F'));
        Assert.assertFalse(reader.matches('G'));
        Assert.assertFalse(reader.matches('H'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matches("AB"));
        Assert.assertTrue(reader.matches("CD")); // should be false
        Assert.assertFalse(reader.matches("EFG"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCdefG"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("cd")); // should be false
        Assert.assertTrue(reader.matchesIgnoreCase("efg"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matchesAny('A'));
        Assert.assertTrue(reader.matchesAny('B'));
        Assert.assertTrue(reader.matchesAny('C'));
        Assert.assertTrue(reader.matchesAny('D')); // should be false
        Assert.assertTrue(reader.matchesAny('E'));
        Assert.assertTrue(reader.matchesAny('F'));
        Assert.assertTrue(reader.matchesAny('G'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matchesAnySorted('A'));
        Assert.assertTrue(reader.matchesAnySorted('B'));
        Assert.assertTrue(reader.matchesAnySorted('C'));
        Assert.assertTrue(reader.matchesAnySorted('D')); // should be false
        Assert.assertTrue(reader.matchesAnySorted('E'));
        Assert.assertTrue(reader.matchesAnySorted('F'));
        Assert.assertTrue(reader.matchesAnySorted('G'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter()); // should be false
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit()); // should be false
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        reader.matchConsume("AB");
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testMatchConsumeStringIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abCdEfG"));
        reader.matchConsumeIgnoreCase("Ab");
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/body"));
    }
}