package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("test string");
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
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('t', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('s', characterReader.consume());
        Assert.assertEquals('t', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        Assert.assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        Assert.assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        Assert.assertEquals(1, characterReader.nextIndexOf('e'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        Assert.assertEquals(2, characterReader.nextIndexOf("st"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeToChar() {
        Assert.assertEquals("test ", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToCharSequence() {
        Assert.assertEquals("test ", characterReader.consumeTo("r "));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("test", characterReader.consumeToAny('r', 'i'));
        Assert.assertEquals(" st", characterReader.consumeToAny('n', 'g'));
    }

    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("test string", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        Assert.assertEquals("test", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Assert.assertEquals("test", characterReader.consumeLetterThenDigitSequence());
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
        Assert.assertTrue(characterReader.matches('t'));
        Assert.assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(characterReader.matchesIgnoreCase("TEsT"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("TESTING"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(characterReader.matchesAny('t', 'e'));
        Assert.assertFalse(characterReader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertTrue(characterReader.matchConsume("test"));
        Assert.assertFalse(characterReader.matchConsume("testing"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertTrue(characterReader.matchConsumeIgnoreCase("TesT"));
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("TESTING"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(characterReader.containsIgnoreCase("es"));
        Assert.assertFalse(characterReader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("test string", characterReader.toString());
    }
}