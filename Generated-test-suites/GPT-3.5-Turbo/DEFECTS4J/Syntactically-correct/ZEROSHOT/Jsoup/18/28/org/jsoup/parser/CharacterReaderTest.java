package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setup() {
        characterReader = new CharacterReader("inputString");
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('i', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('i', characterReader.consume());
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.mark());
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("i", characterReader.consumeAsString());
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("nputStr", characterReader.consumeTo('i'));
        assertEquals(6, characterReader.pos());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("input", characterReader.consumeToAny('t', 'S'));
        assertEquals(5, characterReader.pos());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("ing", characterReader.consumeToEnd());
        assertEquals(10, characterReader.pos());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("inputStri", characterReader.consumeLetterSequence());
        assertEquals(8, characterReader.pos());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("input", characterReader.consumeHexSequence());
        assertEquals(5, characterReader.pos());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('i'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("in"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 'S'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("input"));
        assertEquals(5, characterReader.pos());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("InPuT"));
        assertEquals(5, characterReader.pos());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("output"));
    }
    
    @Test
    public void testToString() {
        assertEquals("inputString", characterReader.toString());
    }
}