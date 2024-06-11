package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals('t', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.mark();
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals(1, characterReader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("t", characterReader.consumeTo('e'));
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("t", characterReader.consumeTo("es"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("te", characterReader.consumeToAny('s'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("test123");
        Assert.assertEquals("test123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("test123");
        Assert.assertEquals("123", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("test123");
        Assert.assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matches('t'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matchesIgnoreCase("Test"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matchesAny('t', 'e', 's', 't'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("123");
        Assert.assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matchConsume("test"));
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("Test"));
        Assert.assertEquals(4, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertFalse(characterReader.containsIgnoreCase("Test"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("test");
        Assert.assertEquals("test", characterReader.toString());
    }
}