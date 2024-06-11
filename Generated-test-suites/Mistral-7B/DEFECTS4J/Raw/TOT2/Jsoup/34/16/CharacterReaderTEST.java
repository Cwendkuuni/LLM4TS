package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharactersInput() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }

    @Test
    public void testNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader("abcabcabc");
        Assert.assertEquals(3, reader.nextIndexOf('c'));
    }

    @Test
    public void testNextIndexOfEmptySequence() {
        CharacterReader reader = new CharacterReader("abcabcabc");
        Assert.assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testNextIndexOfMultiCharacterSequence() {
        CharacterReader reader = new CharacterReader("abcabcabcabc");
        Assert.assertEquals(5, reader.nextIndexOf("bc"));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("mnoPQRST");
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("mno", sequence);
        reader.consumeLetter();
        reader.consumeLetter();
        Assert.assertEquals("PQRST", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("mno1234PQRST");
        String sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("mno123", sequence);
        reader.consumeLetter();
        reader.consumeDigit();
        reader.consumeDigit();
        Assert.assertEquals("RST", reader.consumeLetterThenDigitSequence());
    }
}