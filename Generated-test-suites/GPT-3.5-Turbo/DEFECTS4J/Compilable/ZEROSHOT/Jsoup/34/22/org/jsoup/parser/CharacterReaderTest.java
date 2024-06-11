package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Test Input");
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
    public void testMarkRewindToMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        Assert.assertEquals("T", characterReader.consumeAsString());
        Assert.assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSequence() {
        Assert.assertEquals(1, characterReader.nextIndexOf("es"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("Test ", characterReader.consumeTo(' '));
        Assert.assertEquals("Input", characterReader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSequence() {
        Assert.assertEquals("Test ", characterReader.consumeTo("In"));
        Assert.assertEquals("put", characterReader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("Test ", characterReader.consumeToAny(' ', 'x'));
        Assert.assertEquals("In", characterReader.consumeToAny(' ', 'x'));
    }

    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("Test Input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader = new CharacterReader("Test Input123");
        Assert.assertEquals("Test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader = new CharacterReader("Test Input123");
        Assert.assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("Test 123ABC");
        Assert.assertEquals("123A", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("Test Input123");
        Assert.assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(characterReader.matches('T'));
        Assert.assertFalse(characterReader.isEmpty());
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesSequence() {
        Assert.assertTrue(characterReader.matches("Te"));
        Assert.assertTrue(characterReader.matches("st"));
        Assert.assertFalse(characterReader.matches("ex"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase("TE"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("EX"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('T', 'x'));
        Assert.assertFalse(characterReader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        characterReader = new CharacterReader("Test 123");
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        characterReader.consume();
        Assert.assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertTrue(characterReader.matchConsume("Te"));
        Assert.assertFalse(characterReader.matchConsume("x"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("TE"));
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("EX"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("In"));
        Assert.assertFalse(characterReader.containsIgnoreCase("ex"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Test Input", characterReader.toString());
    }
}