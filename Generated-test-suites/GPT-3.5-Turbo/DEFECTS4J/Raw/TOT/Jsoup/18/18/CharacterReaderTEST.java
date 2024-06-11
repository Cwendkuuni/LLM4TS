package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(0, reader.pos());

        reader.consume();
        Assert.assertEquals(1, reader.pos());

        reader.mark();
        Assert.assertEquals(1, reader.pos());

        reader.advance();
        Assert.assertEquals(2, reader.pos());

        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader readerEmpty = new CharacterReader("");
        Assert.assertTrue(readerEmpty.isEmpty());

        CharacterReader readerNotEmpty = new CharacterReader("test");
        Assert.assertFalse(readerNotEmpty.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader readerEmpty = new CharacterReader("");
        Assert.assertEquals((char) -1, readerEmpty.current());

        CharacterReader readerNotEmpty = new CharacterReader("test");
        Assert.assertEquals('t', readerNotEmpty.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        reader.unconsume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeAsString());
        Assert.assertEquals("e", reader.consumeAsString());
        Assert.assertEquals("s", reader.consumeAsString());
        Assert.assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeTo('t'));
        Assert.assertEquals("t", reader.consumeTo('e'));
        Assert.assertEquals("", reader.consumeTo('s'));
        Assert.assertEquals("", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeToAny('t'));
        Assert.assertEquals("t", reader.consumeToAny('e'));
        Assert.assertEquals("", reader.consumeToAny('s'));
        Assert.assertEquals("", reader.consumeToAny('t'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("a1b2c");
        Assert.assertEquals("a", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("a1b2c");
        Assert.assertEquals("", reader.consumeHexSequence());
        Assert.assertEquals("1", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("a1b2c");
        Assert.assertEquals("", reader.consumeDigitSequence());
        Assert.assertEquals("1", reader.consumeDigitSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches('t'));
        Assert.assertTrue(reader.matches("te"));
        Assert.assertFalse(reader.matches('e'));
        Assert.assertFalse(reader.matches("ex"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        Assert.assertTrue(reader.matchesIgnoreCase('t'));
        Assert.assertTrue(reader.matchesIgnoreCase("Te"));
        Assert.assertFalse(reader.matchesIgnoreCase('e'));
        Assert.assertFalse(reader.matchesIgnoreCase("TeX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesAny('t', 'e', 's'));
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("a1b2c");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("a1b2c");
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchConsume("ex"));
        Assert.assertTrue(reader.matchConsume("te"));
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        Assert.assertFalse(reader.matchConsumeIgnoreCase("TeX"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Te"));
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        Assert.assertTrue(reader.containsIgnoreCase("E"));
        Assert.assertFalse(reader.containsIgnoreCase("X"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        Assert.assertEquals("est", reader.toString());
    }

}