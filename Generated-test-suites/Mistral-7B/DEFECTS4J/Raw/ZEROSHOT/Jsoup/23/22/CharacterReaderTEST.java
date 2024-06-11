package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        Assert.assertTrue(characterReader.isEmpty());
        Assert.assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderNonEmpty() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        String consumed = characterReader.consumeTo('l');
        Assert.assertEquals("He", consumed);
        Assert.assertEquals('l', characterReader.current());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderConsumeToWithNegativeOffset() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.consumeTo(-1);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", input);
        Assert.assertEquals("Hello World", consumed);
        characterReader.consumeToEnd();
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String input = "1abcdab1efg1hij";
        CharacterReader characterReader = new CharacterReader(input);
        String consumed = characterReader.consumeToAny('1');
        Assert.assertEquals("1abcd", consumed);
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals('W', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        String input = "Hello %#ff00%&";
        CharacterReader characterReader = new CharacterReader(input);
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("#ff00", consumed);
        Assert.assertEquals('&', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        String input = "Hello 12345 World";
        CharacterReader characterReader = new CharacterReader(input);
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed);
        Assert.assertEquals('W', characterReader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesSequence() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matches("Hello"));
        Assert.assertFalse(characterReader.matches("WorldHello"));
    }

    @Test
    public void testCharacterReaderMatchesSequenceIgnoreCase() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesIgnoreCase("hello"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("HeLLO"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("WorldHello"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        String input = "Hello World 123";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesAny('1', '3'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        String input = "Hello 123 World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMark() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.mark();
        Assert.assertEquals(input.charAt(0), characterReader.current());
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals(input.charAt(0), characterReader.current());
    }

    @Test
    public void testCharacterReaderToString() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals("World", characterReader.toString());
    }
}