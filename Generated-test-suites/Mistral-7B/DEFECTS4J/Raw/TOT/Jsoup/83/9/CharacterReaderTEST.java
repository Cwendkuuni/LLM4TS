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
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeTo('c');
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('d', reader.current());
        consumed = reader.consumeTo('e');
        Assert.assertEquals("d", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testCharacterReaderScanToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeToAny('2', '4');
        Assert.assertEquals("1", consumed);
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('2', reader.current());

        consumed = reader.consumeToAny('2', '4');
        Assert.assertEquals("2", consumed);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('3', reader.current());

        consumed = reader.consumeToAny('2', '4');
        Assert.assertEquals("3", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('4', reader.current());

        consumed = reader.consumeToAny('2', '4');
        Assert.assertEquals("4", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeToAnySorted('3', '5');
        Assert.assertEquals("12", consumed);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('3', reader.current());

        consumed = reader.consumeToAnySorted('3', '5');
        Assert.assertEquals("3", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('4', reader.current());

        consumed = reader.consumeToAnySorted('3', '5');
        Assert.assertEquals("4", consumed);
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('5', reader.current());

        consumed = reader.consumeToAnySorted('3', '5');
        Assert.assertEquals("5", consumed);
        Assert.assertEquals(5, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("12345", consumed);
        Assert.assertEquals(5, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('1', reader.current());
    }

    @Test
    public void testCharacterReaderScanLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", consumed);
        Assert.assertEquals(6, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;#x123;"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("x123", consumed);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('&', reader.current());
    }

    @Test
    public void testCharacterReaderScanDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed);
        Assert.assertEquals(5, reader.pos());
        Assert.assertTrue(reader.isEmpty());
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
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCd"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("Cd"));
        Assert.assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'e'));
        Assert.assertFalse(reader.matchesAny('f', 'g', 'h', 'i'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted('a', 'b', 'c', 'd'));
        Assert.assertTrue(reader.matchesAnySorted('a', 'd', 'c', 'b'));
        Assert.assertFalse(reader.matchesAnySorted('e', 'f', 'g', 'h'));
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
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }
}