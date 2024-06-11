package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setup() {
        String input = "This is a test string";
        reader = new CharacterReader(input);
    }
    
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }
    
    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('h', reader.current());
    }
    
    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("h", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This", reader.consumeTo(' '));
        assertEquals("is", reader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This", reader.consumeToAny(' ', 'i'));
        assertEquals(" is a ", reader.consumeToAny('s', 't'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
        assertEquals("is", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("F123G");
        assertEquals("F123", reader.consumeHexSequence());
        assertEquals("G", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("abc", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('h'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("t"));
        assertFalse(reader.matchesIgnoreCase("H"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T','h'));
        assertFalse(reader.matchesAny('i','s'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        assertFalse(reader.matchConsume("is"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("tHIS"));
        assertFalse(reader.matchConsumeIgnoreCase("IS"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("is a"));
        assertFalse(reader.containsIgnoreCase("IS A"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test string", reader.toString());
    }
}