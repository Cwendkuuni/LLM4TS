package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("test input");
    }

    @Test
    public void testPos() {
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = characterReader.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        char expected = 't';
        char actual = characterReader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 't';
        char actual = characterReader.consume();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        int expected = 1;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        characterReader.mark();
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        characterReader.rewindToMark();
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "t";
        String actual = characterReader.consumeAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfChar() {
        int expected = -1;
        int actual = characterReader.nextIndexOf('a');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        int expected = -1;
        int actual = characterReader.nextIndexOf("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToChar() {
        String expected = "test ";
        String actual = characterReader.consumeTo(' ');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToString() {
        String expected = "test ";
        String actual = characterReader.consumeTo("input");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "test";
        String actual = characterReader.consumeToAny(' ', 'i', 'n', 'p', 'u', 't');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "test input";
        String actual = characterReader.consumeToEnd();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "test";
        String actual = characterReader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "test";
        String actual = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "test";
        String actual = characterReader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "test";
        String actual = characterReader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        boolean expected = false;
        boolean actual = characterReader.matches('a');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        boolean expected = false;
        boolean actual = characterReader.matches("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = false;
        boolean actual = characterReader.matchesIgnoreCase("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = false;
        boolean actual = characterReader.matchesAny(' ');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = characterReader.matchesLetter();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        boolean expected = false;
        boolean actual = characterReader.matchesDigit();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        boolean expected = false;
        boolean actual = characterReader.matchConsume("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = false;
        boolean actual = characterReader.matchConsumeIgnoreCase("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = false;
        boolean actual = characterReader.containsIgnoreCase("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "test input";
        String actual = characterReader.toString();
        Assert.assertEquals(expected, actual);
    }
}