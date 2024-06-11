package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test string");
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
        char expected = 'T';
        char actual = characterReader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'T';
        char actual = characterReader.consume();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        char expected = 'T';
        char actual = characterReader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        char expected = 'e';
        char actual = characterReader.current();
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
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        char expected = 'e';
        char actual = characterReader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "T";
        String actual = characterReader.consumeAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "Test";
        String actual = characterReader.consumeTo(' ');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToWhenCharNotPresent() {
        String expected = "Test string";
        String actual = characterReader.consumeTo('/');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Test";
        String actual = characterReader.consumeToAny(' ', 's');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Test string";
        String actual = characterReader.consumeToEnd();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Test";
        String actual = characterReader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "Test";
        String actual = characterReader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "";
        String actual = characterReader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        boolean expected = true;
        boolean actual = characterReader.matches('T');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.matches('/');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCaseWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.matchesIgnoreCase("hello");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = characterReader.matchesAny('T', 't');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAnyWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.matchesAny('/', 't');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = characterReader.matchesLetter();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetterWhenNotMatched() {
        boolean expected = false;
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
    public void testMatchesDigitWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.matchesDigit();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        boolean expected = true;
        boolean actual = characterReader.matchConsume("Test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.matchConsume("hello");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("TeSt");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCaseWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.matchConsumeIgnoreCase("hello");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.containsIgnoreCase("stRi");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCaseWhenNotMatched() {
        boolean expected = false;
        boolean actual = characterReader.containsIgnoreCase("hello");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "Test string";
        String actual = characterReader.toString();
        Assert.assertEquals(expected, actual);
    }
}