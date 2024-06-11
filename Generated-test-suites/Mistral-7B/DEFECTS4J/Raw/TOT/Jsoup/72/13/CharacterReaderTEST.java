package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals(1, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacterInput() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
        reader.consume();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
        reader.consume();
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMarkerAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        reader.mark();
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
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
    public void testCharacterReaderStringInput() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        reader.consume();
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
        reader.consume();
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("ABC", consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String consumed = reader.consumeTo('B');
        Assert.assertEquals("A", consumed);
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('B', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        String consumed = reader.consumeTo("BC");
        Assert.assertEquals("AB", consumed);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAny(',');
        Assert.assertEquals("ABCDEFG", consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDEFG"));
        String consumed = reader.consumeToAnySorted(new char[]{'G', 'F', 'E', 'D', 'C', 'B', 'A'});
        Assert.assertEquals("", consumed);
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&<null", consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name=\"attribute\"/>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("tag", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('/', reader.current());
    }

    @Test
    public void testCharacterReaderMatchesCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches('A'));
        Assert.assertTrue(reader.matches('B'));
        Assert.assertTrue(reader.matches('C'));
        Assert.assertFalse(reader.matches('D'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matches("AB"));
        Assert.assertTrue(reader.matches("BC"));
        Assert.assertFalse(reader.matches("CD"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"));
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("bc"));
        Assert.assertTrue(reader.matchesIgnoreCase("cd"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAny('A', 'B', 'C'));
        Assert.assertTrue(reader.matchesAny('A', 'B', 'D'));
        Assert.assertFalse(reader.matchesAny('E', 'B', 'C'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'A', 'B', 'C'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'B', 'A', 'C'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'C', 'B', 'A'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"));
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }
}