package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("A");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('A', reader.current());
        Assert.assertEquals('A', reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultiCharacterInput() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderMarkPosition() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeAsString();
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo('c');
        Assert.assertEquals("ab", consumed);
        Assert.assertEquals('c', reader.current());
        consumed = reader.consumeTo('d');
        Assert.assertEquals("bc", consumed);
        Assert.assertEquals('d', reader.current());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcd123e");
        String consumed = reader.consumeToAny('1', '2', '3');
        Assert.assertEquals("123", consumed);
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcd", consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x12ABCDEF");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("12ABCDEF", consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("1234567890", consumed);
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("AbCd"));
        Assert.assertFalse(reader.matchesIgnoreCase("EF"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('a', 'c'));
        Assert.assertFalse(reader.matchesAny('e', 'f'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        Assert.assertTrue(reader.containsIgnoreCase("ab"));
        Assert.assertTrue(reader.containsIgnoreCase("ABC"));
        Assert.assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("bc", reader.toString());
    }
}