package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test initial position
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals(0, reader.pos());

        // Test position after consuming some characters
        reader.consume();
        reader.consume();
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test empty input
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        // Test non-empty input
        reader = new CharacterReader("input");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test current character when input is empty
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals((char) -1, reader.current());

        // Test current character when input is not empty
        reader = new CharacterReader("input");
        Assert.assertEquals('i', reader.current());
    }

    @Test
    public void testConsume() {
        // Test consuming character
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals('i', reader.consume());
        Assert.assertEquals('n', reader.consume());
        Assert.assertEquals('p', reader.consume());
        Assert.assertEquals('u', reader.consume());
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals((char) -1, reader.consume());
    }

    @Test
    public void testUnconsume() {
        // Test unconsuming character
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals('i', reader.consume());
        Assert.assertEquals('n', reader.consume());
        reader.unconsume();
        Assert.assertEquals('n', reader.consume());
    }

    @Test
    public void testAdvance() {
        // Test advancing position
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testMark() {
        // Test marking position
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        Assert.assertEquals(0, reader.pos());

        // Test position after advancing
        reader.advance();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        // Test rewinding to marked position
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        // Test consuming string
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals("i", reader.consumeAsString());
        Assert.assertEquals("n", reader.consumeAsString());
        Assert.assertEquals("p", reader.consumeAsString());
        Assert.assertEquals("u", reader.consumeAsString());
        Assert.assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        // Test consuming up to a character
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals("in", reader.consumeTo('p'));
        Assert.assertEquals("t", reader.consumeTo('t'));

        // Test consuming to end of input
        reader = new CharacterReader("input");
        Assert.assertEquals("input", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToAny() {
        // Test consuming up to any character in sequence
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals("i", reader.consumeToAny('n', 'p', 'u'));
        Assert.assertEquals("n", reader.consumeToAny('p', 'u', 't'));

        // Test consuming to end of input
        reader = new CharacterReader("input");
        Assert.assertEquals("input", reader.consumeToAny('x', 'y', 'z'));
    }

    @Test
    public void testConsumeToEnd() {
        // Test consuming to end of input
        CharacterReader reader = new CharacterReader("input");
        Assert.assertEquals("input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        // Test consuming letter sequence
        CharacterReader reader = new CharacterReader("input123");
        Assert.assertEquals("input", reader.consumeLetterSequence());

        // Test consuming empty letter sequence
        reader = new CharacterReader("123");
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        // Test consuming hex sequence
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        Assert.assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());

        // Test consuming empty hex sequence
        reader = new CharacterReader("xyz");
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        // Test consuming digit sequence
        CharacterReader reader = new CharacterReader("0123456789");
        Assert.assertEquals("0123456789", reader.consumeDigitSequence());

        // Test consuming empty digit sequence
        reader = new CharacterReader("xyz");
        Assert.assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        // Test character matching
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.matches('i'));
        Assert.assertFalse(reader.matches('n'));

        // Test string matching
        reader = new CharacterReader("input");
        Assert.assertTrue(reader.matches("input"));
        Assert.assertFalse(reader.matches("putin"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Test case-insensitive string matching
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.matchesIgnoreCase("INPUT"));
        Assert.assertFalse(reader.matchesIgnoreCase("PUTIN"));
    }

    @Test
    public void testMatchesAny() {
        // Test matching any character in sequence
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.matchesAny('i', 'n', 'p'));
        Assert.assertFalse(reader.matchesAny('n', 'p', 'u', 't'));
    }

    @Test
    public void testMatchesLetter() {
        // Test matching letter
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());

        // Test matching empty input
        reader = new CharacterReader("");
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        // Test matching digit
        CharacterReader reader = new CharacterReader("0123456789");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());

        // Test matching empty input
        reader = new CharacterReader("");
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        // Test matching and consuming string
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.matchConsume("in"));
        Assert.assertFalse(reader.matchConsume("out"));

        // Test consuming empty string
        reader = new CharacterReader("input");
        Assert.assertFalse(reader.matchConsume("out"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test case-insensitive matching and consuming string
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("INPUT"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("OUT"));

        // Test consuming empty string
        reader = new CharacterReader("input");
        Assert.assertFalse(reader.matchConsumeIgnoreCase("OUT"));
    }

    @Test
    public void testContainsIgnoreCase() {
        // Test case-insensitive presence of string
        CharacterReader reader = new CharacterReader("input");
        Assert.assertTrue(reader.containsIgnoreCase("IN"));
        Assert.assertFalse(reader.containsIgnoreCase("OUT"));

        // Test case-insensitive empty input
        reader = new CharacterReader("");
        Assert.assertFalse(reader.containsIgnoreCase("IN"));
    }

    @Test
    public void testToString() {
        // Test converting remaining input to string
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        Assert.assertEquals("nput", reader.toString());

        // Test converting empty input to string
        reader = new CharacterReader("");
        Assert.assertEquals("", reader.toString());
    }
}