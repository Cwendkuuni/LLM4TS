package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("ABC"));
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        reader.consume();
        Assert.assertEquals('B', reader.current());
        reader.consume();
        Assert.assertEquals('C', reader.current());
        reader.consume();
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        throw new RuntimeException("Forcing IOException");
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderMarkInvalid() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.bufPos = 1;
        reader.rewindToMark();
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abABC"));
        Assert.assertEquals(-1, reader.nextIndexOf('D'));
        Assert.assertEquals(1, reader.nextIndexOf('A'));
        Assert.assertEquals(4, reader.nextIndexOf('B'));
        reader.consume();
        reader.consume();
        Assert.assertEquals(5, reader.nextIndexOf('B'));
        reader.consume();
        reader.consume();
        Assert.assertEquals(-1, reader.nextIndexOf('B'));
    }

    @Test
    public void testCharacterReaderNextIndexOfEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(-1, reader.nextIndexOf(""));
        reader.consume();
        Assert.assertEquals(-1, reader.nextIndexOf(""));
        reader.consume();
        Assert.assertEquals(0, reader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReaderNextIndexOfNull() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(-1, reader.nextIndexOf(null));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abABC"));
        String expected = "ab";
        String read = reader.consumeTo('B');
        Assert.assertEquals(expected, read);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abABC"));
        String expected = "abABC";
        String read = reader.consumeToEnd();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(9, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abABC"));
        String expected = "ab";
        String read = reader.consumeToAny('B');
        Assert.assertEquals(expected, read);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abABC"));
        String expected = "ab";
        String read = reader.consumeToAnySorted(new char[]{'B', 'A'});
        Assert.assertEquals(expected, read);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String expected = "&<null";
        String read = reader.consumeData();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        String expected = "<tag>";
        String read = reader.consumeTagName();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(6, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        String expected = "abc";
        String read = reader.consumeLetterSequence();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(6, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123"));
        String expected = "abc123";
        String read = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(9, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("\\u0061\\u0062\\u0063"));
        String expected = "ab";
        String read = reader.consumeHexSequence();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(6, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        String expected = "123";
        String read = reader.consumeDigitSequence();
        Assert.assertEquals(expected, read);
        Assert.assertEquals(3, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("a"));
        Assert.assertFalse(reader.matches('b'));
        Assert.assertFalse(reader.matches("b"));
        reader.consume();
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("a"));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches("b"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertFalse(reader.matchesIgnoreCase("b"));
        Assert.assertFalse(reader.matchesIgnoreCase("B"));
        reader.consume();
        Assert.assertFalse(reader.matchesIgnoreCase("a"));
        Assert.assertFalse(reader.matchesIgnoreCase("A"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("B"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertTrue(reader.matchesAny('A'));
        Assert.assertTrue(reader.matchesAny('B'));
        Assert.assertTrue(reader.matchesAny('C'));
        reader.consume();
        Assert.assertFalse(reader.matchesAny('a'));
        Assert.assertFalse(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertTrue(reader.matchesAny('A'));
        Assert.assertTrue(reader.matchesAny('B'));
        Assert.assertTrue(reader.matchesAny('C'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchesAnySorted('a'));
        Assert.assertTrue(reader.matchesAnySorted('b'));
        Assert.assertTrue(reader.matchesAnySorted('c'));
        Assert.assertTrue(reader.matchesAnySorted('A'));
        Assert.assertTrue(reader.matchesAnySorted('B'));
        Assert.assertTrue(reader.matchesAnySorted('C'));
        reader.consume();
        Assert.assertFalse(reader.matchesAnySorted('a'));
        Assert.assertFalse(reader.matchesAnySorted('b'));
        Assert.assertTrue(reader.matchesAnySorted('c'));
        Assert.assertTrue(reader.matchesAnySorted('A'));
        Assert.assertTrue(reader.matchesAnySorted('B'));
        Assert.assertTrue(reader.matchesAnySorted('C'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
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
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }
}