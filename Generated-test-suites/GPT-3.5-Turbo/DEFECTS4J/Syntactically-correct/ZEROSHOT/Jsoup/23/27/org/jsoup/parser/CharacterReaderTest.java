package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test Input");
    }
    
    @Test
    public void testPos() {
        int pos = characterReader.pos();
        Assert.assertEquals(0, pos);
    }
    
    @Test
    public void testIsEmpty() {
        boolean isEmpty = characterReader.isEmpty();
        Assert.assertFalse(isEmpty);
    }
    
    @Test
    public void testCurrent() {
        char current = characterReader.current();
        Assert.assertEquals('T', current);
    }
    
    @Test
    public void testConsume() {
        char consumed = characterReader.consume();
        Assert.assertEquals('T', consumed);
    }
    
    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        char current = characterReader.current();
        Assert.assertEquals('T', current);
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        char current = characterReader.current();
        Assert.assertEquals('e', current);
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        int pos = characterReader.pos();
        Assert.assertEquals(0, pos);
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        int pos = characterReader.pos();
        Assert.assertEquals(0, pos);
    }
    
    @Test
    public void testConsumeAsString() {
        String consumed = characterReader.consumeAsString();
        Assert.assertEquals("T", consumed);
    }
    
    @Test
    public void testConsumeToChar() {
        String consumed = characterReader.consumeTo(' ');
        Assert.assertEquals("Test", consumed);
    }
    
    @Test
    public void testConsumeToString() {
        String consumed = characterReader.consumeTo(" Input");
        Assert.assertEquals("Test", consumed);
    }
    
    @Test
    public void testConsumeToAny() {
        String consumed = characterReader.consumeToAny('I', 'n');
        Assert.assertEquals("Test ", consumed);
    }
    
    @Test
    public void testConsumeToEnd() {
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Test Input", consumed);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("Test", consumed);
    }
    
    @Test
    public void testConsumeHexSequence() {
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("", consumed);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("", consumed);
    }
    
    @Test
    public void testMatchesChar() {
        boolean matches = characterReader.matches('T');
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesString() {
        boolean matches = characterReader.matches("Test");
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        boolean matches = characterReader.matchesIgnoreCase("test");
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesAny() {
        boolean matches = characterReader.matchesAny('t', 'T');
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesLetter() {
        boolean matches = characterReader.matchesLetter();
        Assert.assertTrue(matches);
    }
    
    @Test
    public void testMatchesDigit() {
        boolean matches = characterReader.matchesDigit();
        Assert.assertFalse(matches);
    }
    
    @Test
    public void testMatchConsume() {
        boolean matchConsume = characterReader.matchConsume("Test");
        Assert.assertTrue(matchConsume);
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean matchConsumeIgnoreCase = characterReader.matchConsumeIgnoreCase("test");
        Assert.assertTrue(matchConsumeIgnoreCase);
    }
    
    @Test
    public void testContainsIgnoreCase() {
        boolean containsIgnoreCase = characterReader.containsIgnoreCase("Test");
        Assert.assertTrue(containsIgnoreCase);
    }
    
    @Test
    public void testToString() {
        String toString = characterReader.toString();
        Assert.assertEquals("Test Input", toString);
    }
    
}