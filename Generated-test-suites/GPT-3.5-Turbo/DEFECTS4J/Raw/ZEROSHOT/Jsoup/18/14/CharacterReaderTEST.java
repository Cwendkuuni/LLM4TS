package org.jsoup.parser;
import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToWithExistingCharacter() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToWithSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeTo("st"));
    }

    @Test
    public void testConsumeToWithSequenceNotFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeToAny('s', 'z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234");
        Assert.assertEquals("1234", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesWithNonMatch() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesWithSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches("te"));
    }

    @Test
    public void testMatchesWithNonMatchingSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matches("tz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        Assert.assertTrue(reader.matchesIgnoreCase("TeSt"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesAny('t', 'z'));
    }

    @Test
    public void testMatchesAnyWithNonMatch() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetterWithNonLetter() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigitWithNonDigit() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeWithNonMatch() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchConsume("tz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("TeSt"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseWithNonMatch() {
        CharacterReader reader = new CharacterReader("tEsT");
        Assert.assertFalse(reader.matchConsumeIgnoreCase("TeSz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.containsIgnoreCase("te"));
    }

    @Test
    public void testContainsIgnoreCaseWithNonMatch() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.containsIgnoreCase("tz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.toString());
    }

}