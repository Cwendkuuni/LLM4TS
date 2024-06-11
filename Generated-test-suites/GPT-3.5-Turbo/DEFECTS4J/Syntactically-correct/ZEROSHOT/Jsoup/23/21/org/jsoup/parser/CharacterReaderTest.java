package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

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
    public void testCurrentEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals((char) -1, reader.current());
    }
    
    @Test
    public void testCurrentNotEmpty() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals((char) -1, reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeTo('s'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeToAny('x', 'y', 'z'));
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
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matches('x'));
        Assert.assertTrue(reader.matches('t'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesIgnoreCase("TEST"));
        Assert.assertTrue(reader.matchesIgnoreCase("TES"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
        Assert.assertTrue(reader.matchesAny('t', 'e'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchConsume("abc"));
        Assert.assertTrue(reader.matchConsume("tes"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchConsumeIgnoreCase("ABC"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("TES"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.containsIgnoreCase("ABC"));
        Assert.assertTrue(reader.containsIgnoreCase("TES"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.toString());
    }
}