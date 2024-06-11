package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        String input = "test input";
        characterReader = new CharacterReader(input);
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
        int actual = characterReader.mark();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
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
    public void testNextIndexOf() {
        int expected = 1;
        int actual = characterReader.nextIndexOf('e');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        int expected = -1;
        int actual = characterReader.nextIndexOf("xyz");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "test in";
        String actual = characterReader.consumeTo('p');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "te";
        String actual = characterReader.consumeToAny('s', 't');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "";
        String actual = characterReader.consumeToAnySorted('t', 'a', 'b');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        String expected = "test input";
        String actual = characterReader.consumeData();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "test";
        String actual = characterReader.consumeTagName();
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
        boolean expected = true;
        boolean actual = characterReader.matches('t');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        boolean expected = true;
        boolean actual = characterReader.matches("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("TEST");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = characterReader.matchesAny('t', 'e');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAnySorted() {
        boolean expected = false;
        boolean actual = characterReader.matchesAnySorted('z', 'y', 'x');
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
        boolean expected = true;
        boolean actual = characterReader.matchConsume("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("TEST");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = false;
        boolean actual = characterReader.containsIgnoreCase("XYZ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "test input";
        String actual = characterReader.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRangeEquals() {
        boolean expected = true;
        boolean actual = characterReader.rangeEquals(0, 4, "test");
        Assert.assertEquals(expected, actual);
    }
}