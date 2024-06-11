package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Test string");
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('T', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        Assert.assertEquals(0, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        Assert.assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("Test ", characterReader.consumeTo('s'));
        Assert.assertEquals("string", characterReader.consumeTo('g'));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("Test ", characterReader.consumeToAny('s', 'r', 'n'));
        Assert.assertEquals("st", characterReader.consumeToAny('r', 'i'));
    }

    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("Test string", characterReader.consumeToEnd());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        Assert.assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        Assert.assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        Assert.assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('T'));
        Assert.assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('s', 'T'));
        Assert.assertFalse(characterReader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertTrue(characterReader.matchConsume("Test"));
        Assert.assertFalse(characterReader.matchConsume("string"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertFalse(characterReader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Test string", characterReader.toString());
        characterReader.consume();
        Assert.assertEquals("est string", characterReader.toString());
    }
}