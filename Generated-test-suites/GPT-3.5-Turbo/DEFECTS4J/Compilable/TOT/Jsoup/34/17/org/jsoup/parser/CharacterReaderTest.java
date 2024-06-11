package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World");
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
        char expected = 'H';

        char actual = characterReader.current();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'H';

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
        String expected = "H";

        String actual = characterReader.consumeAsString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf() {
        int expected = 7;

        int actual = characterReader.nextIndexOf('W');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        int expected = 0;

        int actual = characterReader.nextIndexOf("Hello World");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "Hello ";

        String actual = characterReader.consumeTo('W');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Hello ";

        String actual = characterReader.consumeToAny('W', 'o');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Hello World";

        String actual = characterReader.consumeToEnd();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Hello";

        String actual = characterReader.consumeLetterSequence();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "Hello";

        String actual = characterReader.consumeLetterThenDigitSequence();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "";

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

        boolean actual = characterReader.matches('H');

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = false;

        boolean actual = characterReader.matchesAny('W', 'o');

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

        boolean actual = characterReader.matchConsume("Hello");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;

        boolean actual = characterReader.matchConsumeIgnoreCase("hello");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;

        boolean actual = characterReader.containsIgnoreCase("World");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "Hello World";

        String actual = characterReader.toString();

        Assert.assertEquals(expected, actual);
    }
}