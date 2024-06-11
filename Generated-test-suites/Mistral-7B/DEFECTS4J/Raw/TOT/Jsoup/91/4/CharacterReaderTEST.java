package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testSingleCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testMultipleCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.advance();
        Assert.assertEquals('B', reader.current());
        reader.advance();
        Assert.assertEquals('C', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        throw new IOException("Test IOException");
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testConsumerAndUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        char c = reader.consume();
        Assert.assertEquals('A', c);
        reader.unconsume();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testConsumeToCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeTo('B');
        Assert.assertEquals("A", result);
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeTo("BC");
        Assert.assertEquals("AB", result);
    }

    @Test
    public void testConsumeToAnySingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeToAny('B');
        Assert.assertEquals("AC", result);
    }

    @Test
    public void testConsumeToAnyMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String result = reader.consumeToAny(new char[]{'B', 'C'});
        Assert.assertEquals("AC", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;"));
        String result = reader.consumeData();
        Assert.assertEquals("&lt;", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("html", result);
    }

    @Test
    public void testNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        int index = reader.nextIndexOf('B');
        Assert.assertEquals(1, index);
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        int index = reader.nextIndexOf("EF");
        Assert.assertEquals(5, index);
    }

    @Test
    public void testNextIndexOfEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        int index = reader.nextIndexOf("");
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testMatchesSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches('A'));
        Assert.assertFalse(reader.matches('B'));
        Assert.assertFalse(reader.matches('C'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        Assert.assertTrue(reader.matches("EF"));
        Assert.assertFalse(reader.matches("AB"));
    }

    @Test
    public void testMatchesIgnoreCaseSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcDEf"));
        Assert.assertTrue(reader.matchesIgnoreCase('D'));
        Assert.assertFalse(reader.matchesIgnoreCase('A'));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcDEf"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("Ab"));
    }

    @Test
    public void testMatchesAnySingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAny('A'));
        Assert.assertTrue(reader.matchesAny('B'));
        Assert.assertTrue(reader.matchesAny('C'));
    }

    @Test
    public void testMatchesAnyMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAny(new char[]{'A', 'B'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'B', 'C'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'A', 'C'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
    }
}