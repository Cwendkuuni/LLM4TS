package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.input);
        Assert.assertEquals(11, characterReader.length);
        Assert.assertEquals(0, characterReader.pos);
        Assert.assertEquals(0, characterReader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        Assert.assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader("Hello World");
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        Assert.assertEquals('H', characterReader.current());
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("He", characterReader.consumeAsString());
        characterReader.advance();
        Assert.assertEquals("ll", characterReader.consumeAsString());
        Assert.assertEquals(EOF, characterReader.current());
        Assert.assertEquals("Hello", characterReader.consumeTo('l'));
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(6, characterReader.nextIndexOf('o'));
        characterReader.consume();
        Assert.assertEquals(7, characterReader.nextIndexOf('o'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("ello", characterReader.consumeTo('l'));
        Assert.assertEquals("ld", characterReader.consumeTo('d'));
        Assert.assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("World", characterReader.consumeToEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToEndWithEmptyString() {
        CharacterReader characterReader = new CharacterReader("");
        characterReader.consumeToEnd();
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("World", characterReader.consumeToAny('w', 'r', 'l', 'd'));
        Assert.assertEquals("Hello", characterReader.consumeToAny('h'));
        Assert.assertEquals("", characterReader.consumeToAny('z'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("World", characterReader.consumeToAnySorted('r', 'l', 'd', 'w'));
        Assert.assertEquals("Hello", characterReader.consumeToAnySorted('h'));
        Assert.assertEquals("", characterReader.consumeToAnySorted('z'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader("&<null>");
        Assert.assertEquals("&<", characterReader.consumeData());
        Assert.assertEquals("null", characterReader.consumeData());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader("<title>Hello World</title>");
        Assert.assertEquals("title", characterReader.consumeTagName());
        Assert.assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        Assert.assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        Assert.assertTrue(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches("xx"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("world"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("xx"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesAny('l', 'o', 'r', 'd'));
        Assert.assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesAnySorted('l', 'r', 'd', 'o'));
        Assert.assertFalse(characterReader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchConsume("ll"));
        Assert.assertEquals(6, characterReader.pos);
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("World"));
        Assert.assertEquals(11, characterReader.pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<html><head><title>Hello World</title></head><body>");
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/body"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/header"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheStringWithNegativeLength() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.cacheString(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheStringWithZeroLength() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.cacheString(0, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCacheStringWithHashCollision() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.cacheString(0, "Hello".length());
        characterReader.cacheString(0, "World".length());
    }
}