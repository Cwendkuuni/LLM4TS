package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertEquals(input, characterReader.toString());
        Assert.assertEquals(5, characterReader.length());
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testCharacterReader_isEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReader_pos() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consume() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReader_unconsumedCharacter() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        characterReader.unconsume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume(); // This should consume the last character
        characterReader.consume(); // This should throw IllegalStateException
    }

    @Test
    public void testCharacterReader_advance() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        characterReader.advance();
        Assert.assertEquals('H', characterReader.current());
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCharacterReader_mark() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("Hello", characterReader.consumeAsString());
        Assert.assertEquals('W', characterReader.current());
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testCharacterReader_nextIndexOf() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals(5, characterReader.nextIndexOf('l'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReader_consumeTo() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("Hello", characterReader.consumeTo(' '));
        Assert.assertEquals('W', characterReader.current());
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeTo_EmptyString() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("", characterReader.consumeTo(""));
        Assert.assertEquals('H', characterReader.current());
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeTo_Null() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("Hello World!", characterReader.consumeTo(null));
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(12, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeToAny() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("World!", characterReader.consumeToAny('r', 'l', 'o', 'W'));
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("World!", characterReader.consumeToAnySorted('l', 'r', 'o', 'W'));
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeData() {
        CharacterReader characterReader = new CharacterReader("&<null>");
        Assert.assertEquals("&<", characterReader.consumeData());
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeTagName() {
        CharacterReader characterReader = new CharacterReader("<title>Hello</title>");
        Assert.assertEquals("<title>", characterReader.consumeTagName());
        Assert.assertEquals('/', characterReader.current());
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader characterReader = new CharacterReader("<title>Hello</title>");
        Assert.assertEquals("<title>Hello</title>", characterReader.consumeToEnd());
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(15, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertEquals("Hello", characterReader.consumeLetterSequence());
        Assert.assertEquals('W', characterReader.current());
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123World!");
        Assert.assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
        Assert.assertEquals('W', characterReader.current());
        Assert.assertEquals(10, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("&#x61;#x62;#x63;");
        Assert.assertEquals("abc", characterReader.consumeHexSequence());
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("12345");
        Assert.assertEquals("12345", characterReader.consumeDigitSequence());
        Assert.assertEquals('\0', characterReader.current());
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testCharacterReader_matches() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matches('o'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("o"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReader_matchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matchesAny('h', 'e', 'l', 'o'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testCharacterReader_matchesAnySorted() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matchesAnySorted('l', 'o', 'h'));
        Assert.assertFalse(characterReader.matchesAnySorted('z'));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter()); // Consume a letter, test again
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader characterReader = new CharacterReader("12345");
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit()); // Consume a digit, test again
    }

    @Test
    public void testCharacterReader_matchConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matchConsume("ll"));
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World!");
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
        Assert.assertEquals(3, characterReader.pos());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<html><head><title>Hello World</title></head><body>Hello World</body></html>");
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/body"));
    }
}