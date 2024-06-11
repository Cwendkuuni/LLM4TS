package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        int pos = reader.pos();
        Assert.assertEquals(0, pos);
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test");
        boolean isEmpty = reader.isEmpty();
        Assert.assertFalse(isEmpty);
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        char current = reader.current();
        Assert.assertEquals('t', current);
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char consumed = reader.consume();
        Assert.assertEquals('t', consumed);
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        char current = reader.current();
        Assert.assertEquals('t', current);
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        char current = reader.current();
        Assert.assertEquals('e', current);
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        int pos = reader.pos();
        Assert.assertEquals(1, pos);
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        int pos = reader.pos();
        Assert.assertEquals(1, pos);
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeAsString();
        Assert.assertEquals("t", consumed);
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeTo('s');
        Assert.assertEquals("te", consumed);
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeToAny('e', 's');
        Assert.assertEquals("t", consumed);
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("t", consumed);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("test", consumed);
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("", consumed);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("", consumed);
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matches('t');
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesIgnoreCase("TEST");
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesAny('t', 'e', 's', 't');
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesLetter();
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesDigit();
        Assert.assertFalse(matches);
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        boolean matchConsume = reader.matchConsume("te");
        Assert.assertTrue(matchConsume);
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean matchConsume = reader.matchConsumeIgnoreCase("TES");
        Assert.assertTrue(matchConsume);
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean containsIgnoreCase = reader.containsIgnoreCase("TES");
        Assert.assertTrue(containsIgnoreCase);
    }
}