package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(1, reader.consume());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertEquals('B', reader.current());
        Assert.assertEquals('B', reader.consume());
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals(EOF, reader.current());
        reader.advance();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testReaderWithIOException() throws IOException {
        StringReader reader = new StringReader("");
        reader.mark(10);
        reader.read();
        CharacterReader characterReader = new CharacterReader(reader, maxBufferLen);
        characterReader.bufferUp();
        characterReader.reader.close();
        characterReader.consume();
    }

    @Test
    public void testReaderWithMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        reader.mark();
        reader.advance();
        reader.advance();
        Assert.assertEquals('C', reader.current());
        Assert.assertEquals(3, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testReaderWithLongString() {
        CharacterReader reader = new CharacterReader(new StringReader("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque habitant morphologyma ac bibendum. Ut commodo, dolor eget placerat vulputate, est tellus mollis nunc, sed imperdiet urna tempus orci. Maecenas lacinia eros vel est dictum bibendum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curabitur et ligula. Vestibulum at lacus et arcu interdum mattis. Sed consectetur, diam quis bibendum rhoncus, sem quam semper risus, a pellentesque elit lacus quis turpis."));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('L', reader.current());
        Assert.assertEquals(11, reader.pos());
        Assert.assertEquals(EOF, reader.consume());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testReaderWithNextIndexOfChar() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals(9, reader.nextIndexOf('i'));
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testReaderWithNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals(10, reader.nextIndexOf("ef"));
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testReaderWithConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals("ef", reader.consumeTo('e'
));
        Assert.assertEquals("fghijklmnopqrstuvwxyz", reader.consumeToEnd());
    }

    @Test
    public void testReaderWithConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz<>&"));
        Assert.assertEquals("&", reader.consumeToAny(new char[]{'<', '>', '&'}));
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz<>&", reader.consumeToAnySorted(new char[]{'<', '>', '&'}));
    }

    @Test
    public void testReaderWithConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("<data>abcd</data>"));
        Assert.assertEquals("abcd", reader.consumeData());
    }

    @Test
    public void testReaderWithConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tagName>abcd</tagName>"));
        Assert.assertEquals("tagName", reader.consumeTagName());
    }

    @Test
    public void testReaderWithConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("<tagName>abcd</tagName>"));
        Assert.assertEquals("<tagName>abcd</tagName>", reader.consumeToEnd());
    }

    @Test
    public void testReaderWithConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd1234efghijklmnopqrstuvwxyz"));
        Assert.assertEquals("cd", reader.consumeLetterSequence());
    }

    @Test
    public void testReaderWithConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd1234efghijklmnopqrstuvwxyz"));
        Assert.assertEquals("cd1234", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testReaderWithMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertFalse(reader.matches('q'));
    }

    @Test
    public void testReaderWithMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(reader.matchesIgnoreCase("e"));
        Assert.assertTrue(reader.matchesIgnoreCase("E"));
        Assert.assertFalse(reader.matchesIgnoreCase("q"));
    }

    @Test
    public void testReaderWithMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(reader.matchesAny('e'));
        Assert.assertTrue(reader.matchesAny('E'));
        Assert.assertFalse(reader.matchesAny('q'));
    }

    @Test
    public void testReaderWithMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(reader.matchesAnySorted('e'));
        Assert.assertTrue(reader.matchesAnySorted('E'));
        Assert.assertFalse(reader.matchesAnySorted('q'));
    }

    @Test
    public void testReaderWithMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter('a'));
        Assert.assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testReaderWithMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit('5'));
        Assert.assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testReaderWithMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals("ef", reader.matchConsume("ef"));
    }

    @Test
    public void testReaderWithMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertEquals("ef", reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testReaderWithContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body><p>Test</p></body></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("test"));
        Assert.assertTrue(reader.containsIgnoreCase("TEST"));
        Assert.assertFalse(reader.containsIgnoreCase("hello"));
    }
}