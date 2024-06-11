package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        int expected = 0;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        boolean expected = true;
        boolean actual = reader.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        char expected = 't';
        char actual = reader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char expected = 't';
        char actual = reader.consume();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        char expected = 't';
        char actual = reader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        char expected = 'e';
        char actual = reader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        int expected = 0;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        char expected = 't';
        char actual = reader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "t";
        String actual = reader.consumeAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("test");
        int expected = 2;
        int actual = reader.nextIndexOf('s');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("test");
        int expected = 2;
        int actual = reader.nextIndexOf("st");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "te";
        String actual = reader.consumeTo('s');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "te";
        String actual = reader.consumeToAny('s', 't');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeToEnd();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test1");
        String expected = "test1";
        String actual = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test1");
        String expected = "";
        String actual = reader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test1");
        String expected = "";
        String actual = reader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matches('t');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matches("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("TEST");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchesAny('t', 'e');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = false;
        boolean actual = reader.matchesDigit();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchConsume("te");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("TE");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("TE");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.toString();
        Assert.assertEquals(expected, actual);
    }
}