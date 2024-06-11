package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test string.");
    }
    
    @Test
    public void testPos() {
        int expectedPos = 0;
        Assert.assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        boolean expectedEmpty = false;
        Assert.assertEquals(expectedEmpty, reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        char expectedCurrent = 'T';
        Assert.assertEquals(expectedCurrent, reader.current());
    }
    
    @Test
    public void testConsume() {
        char expectedVal = 'T';
        Assert.assertEquals(expectedVal, reader.consume());
        
        expectedVal = 'h';
        Assert.assertEquals(expectedVal, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        char expectedVal = 'h';
        reader.unconsume();
        Assert.assertEquals(expectedVal, reader.current());
    }
    
    @Test
    public void testAdvance() {
        char expectedVal = 'i';
        reader.advance();
        Assert.assertEquals(expectedVal, reader.current());
    }
    
    @Test
    public void testMark() {
        int expectedMark = 0;
        reader.mark();
        Assert.assertEquals(expectedMark, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        
        int expectedPos = 0;
        Assert.assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        String expectedString = "T";
        Assert.assertEquals(expectedString, reader.consumeAsString());
        
        expectedString = "h";
        Assert.assertEquals(expectedString, reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOfCharacter() {
        int expectedOffset = 2;
        char target = 'i';
        Assert.assertEquals(expectedOffset, reader.nextIndexOf(target));
        
        target = 'z';
        expectedOffset = -1;
        Assert.assertEquals(expectedOffset, reader.nextIndexOf(target));
    }
    
    @Test
    public void testNextIndexOfCharSequence() {
        int expectedOffset = 2;
        CharSequence seq = "is";
        Assert.assertEquals(expectedOffset, reader.nextIndexOf(seq));
        
        seq = "string";
        expectedOffset = 13;
        Assert.assertEquals(expectedOffset, reader.nextIndexOf(seq));
    }
    
    @Test
    public void testConsumeToCharacter() {
        String expectedConsumed = "This ";
        char target = 'i';
        Assert.assertEquals(expectedConsumed, reader.consumeTo(target));
        
        expectedConsumed = "This is a test string.";
        target = 'z';
        Assert.assertEquals(expectedConsumed, reader.consumeTo(target));
    }
    
    @Test
    public void testConsumeToCharSequence() {
        String expectedConsumed = "This ";
        CharSequence seq = "is";
        Assert.assertEquals(expectedConsumed, reader.consumeTo(seq));
        
        expectedConsumed = "This is a test string.";
        seq = "test";
        Assert.assertEquals(expectedConsumed, reader.consumeTo(seq));
    }
    
    @Test
    public void testConsumeToAny() {
        String expectedConsumed = "This ";
        char[] chars = {'i', 'z'};
        Assert.assertEquals(expectedConsumed, reader.consumeToAny(chars));
        
        expectedConsumed = "This is a test string.";
        chars[1] = 't';
        Assert.assertEquals(expectedConsumed, reader.consumeToAny(chars));
    }
    
    @Test
    public void testConsumeToEnd() {
        String expectedConsumed = "This is a test string.";
        Assert.assertEquals(expectedConsumed, reader.consumeToEnd());
        
        int expectedPos = expectedConsumed.length();
        Assert.assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String expectedConsumed = "This";
        Assert.assertEquals(expectedConsumed, reader.consumeLetterSequence());
        
        char[] chars = {' ', 'i'};
        Assert.assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expectedConsumed = "This";
        Assert.assertEquals(expectedConsumed, reader.consumeLetterThenDigitSequence());
        
        expectedConsumed = "is2";
        Assert.assertEquals(expectedConsumed, reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        String expectedConsumed = "This";
        Assert.assertEquals(expectedConsumed, reader.consumeHexSequence());
        
        expectedConsumed = "is2";
        Assert.assertEquals(expectedConsumed, reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String expectedConsumed = "";
        Assert.assertEquals(expectedConsumed, reader.consumeDigitSequence());
        
        expectedConsumed = "1";
        Assert.assertEquals(expectedConsumed, reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatchesCharacter() {
        boolean expectedMatch = false;
        char target = 'i';
        Assert.assertEquals(expectedMatch, reader.matches(target));
        
        target = 'T';
        expectedMatch = true;
        Assert.assertEquals(expectedMatch, reader.matches(target));
    }
    
    @Test
    public void testMatchesCharSequence() {
        boolean expectedMatch = true;
        CharSequence seq = "This";
        Assert.assertEquals(expectedMatch, reader.matches(seq));
        
        seq = "invalid";
        expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matches(seq));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        boolean expectedMatch = true;
        CharSequence seq = "This";
        Assert.assertEquals(expectedMatch, reader.matchesIgnoreCase(seq));
        
        seq = "INVALID";
        expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matchesIgnoreCase(seq));
    }
    
    @Test
    public void testMatchesAny() {
        boolean expectedMatch = true;
        char[] chars = {'T', 'h'};
        Assert.assertEquals(expectedMatch, reader.matchesAny(chars));
        
        chars[0] = 'I';
        expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matchesAny(chars));
    }
    
    @Test
    public void testMatchesLetter() {
        boolean expectedMatch = true;
        Assert.assertEquals(expectedMatch, reader.matchesLetter());
        
        reader.unconsume();
        expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        boolean expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matchesDigit());
        
        reader.consume();
        expectedMatch = true;
        Assert.assertEquals(expectedMatch, reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        boolean expectedMatch = true;
        String seq = "This";
        Assert.assertEquals(expectedMatch, reader.matchConsume(seq));
        
        seq = "invalid";
        expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matchConsume(seq));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expectedMatch = true;
        String seq = "This";
        Assert.assertEquals(expectedMatch, reader.matchConsumeIgnoreCase(seq));
        
        seq = "invalid";
        expectedMatch = false;
        Assert.assertEquals(expectedMatch, reader.matchConsumeIgnoreCase(seq));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        boolean expectedContains = true;
        String seq = "this";
        Assert.assertEquals(expectedContains, reader.containsIgnoreCase(seq));
        
        seq = "INVALID";
        expectedContains = false;
        Assert.assertEquals(expectedContains, reader.containsIgnoreCase(seq));
    }
    
    @Test
    public void testToString() {
        String expectedString = "This is a test string.";
        Assert.assertEquals(expectedString, reader.toString());
    }
}