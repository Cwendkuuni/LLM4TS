package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("test");
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        Assert.assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("tes", reader.consumeTo('t'));
        Assert.assertEquals("t", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("te", reader.consumeToAny('s', 't'));
        Assert.assertEquals("st", reader.consumeToAny('s', 't'));
    }

    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("test", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        reader = new CharacterReader("ab123");
        Assert.assertEquals("ab", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("ab123");
        Assert.assertEquals("ab", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("ab123");
        Assert.assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('t'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        reader = new CharacterReader("Test");
        Assert.assertTrue(reader.matchesIgnoreCase("teSt"));
        Assert.assertFalse(reader.matchesIgnoreCase("abcd"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny('t', 'e'));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        reader = new CharacterReader("test1");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        reader = new CharacterReader("test1");
        reader.consumeToEnd();
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertTrue(reader.matchConsume("te"));
        Assert.assertFalse(reader.matchConsume("ab"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader = new CharacterReader("Test");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("teSt"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("abcd"));
    }

    @Test
    public void testContainsIgnoreCase() {
        reader = new CharacterReader("test</TitLe>");
        Assert.assertTrue(reader.containsIgnoreCase("</title>"));
        Assert.assertFalse(reader.containsIgnoreCase("abcd"));
    }

    @Test
    public void testToString() {
        reader.consumeToEnd();
        Assert.assertEquals("", reader.toString());
    }

    //TODO: Add additional test cases for potential edge cases and exceptions

}

