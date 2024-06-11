package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("test string");
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
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        characterReader.consume(); // Advances position to 1
        characterReader.mark(); // Sets mark to 1
        characterReader.consume(); // Advances position to 2
        characterReader.rewindToMark(); // Sets position back to 1
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }
    
    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(2, characterReader.nextIndexOf("st"));
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("t", characterReader.consumeTo('e'));
    }
    
    @Test
    public void testConsumeToWithSequence() {
        assertEquals("t", characterReader.consumeTo("e"));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("st string", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        characterReader.advance();
        assertEquals("st", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader.advance();
        assertEquals("st", characterReader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader.advance();
        assertEquals("st", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader.advance();
        assertEquals("st", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('x'));
    }
    
    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("te"));
        assertFalse(characterReader.matches("xyz"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("TE"));
        assertFalse(characterReader.matchesIgnoreCase("XYZ"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 'x'));
        assertFalse(characterReader.matchesAny('x', 'y'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("te"));
        assertFalse(characterReader.matchConsume("xyz"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("TE"));
        assertFalse(characterReader.matchConsumeIgnoreCase("XYZ"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("te"));
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }
    
    @Test
    public void testToString() {
        assertEquals("test string", characterReader.toString());
    }
}

