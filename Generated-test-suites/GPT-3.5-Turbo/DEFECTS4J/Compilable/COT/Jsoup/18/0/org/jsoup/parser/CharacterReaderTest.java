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
    }
    
    @Test
    public void testUnconsume() {
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
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("es", characterReader.consumeTo('s'));
    }
    
    @Test
    public void testConsumeToWithMissingChar() {
        assertEquals("test string", characterReader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToWithSeq() {
        assertEquals("test", characterReader.consumeTo(" "));
    }
    
    @Test
    public void testConsumeToWithMissingSeq() {
        assertEquals("test string", characterReader.consumeTo("xyz"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("test", characterReader.consumeToAny(' '));
    }
    
    @Test
    public void testConsumeToAnyWithMultipleChars() {
        assertEquals("test", characterReader.consumeToAny(' ', 't'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("test string", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("0xA12F");
        assertEquals("0xA12F", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("12345");
        assertEquals("12345", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
    }
    
    @Test
    public void testMatchesWithMissingChar() {
        assertFalse(characterReader.matches('z'));
    }
    
    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("test"));
    }
    
    @Test
    public void testMatchesWithMissingSequence() {
        assertFalse(characterReader.matches("xyz"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("TEST"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 's'));
    }
    
    @Test
    public void testMatchesAnyWithMissingChar() {
        assertFalse(characterReader.matchesAny('z', 'x'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesLetterWithDigit() {
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchesDigitWithLetter() {
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("test"));
        assertEquals(4, characterReader.pos());
    }
    
    @Test
    public void testMatchConsumeWithMissingSeq() {
        assertFalse(characterReader.matchConsume("xyz"));
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Test"));
        assertEquals(4, characterReader.pos());
    }
    
    @Test
    public void testMatchConsumeIgnoreCaseWithMissingSeq() {
        assertFalse(characterReader.matchConsumeIgnoreCase("xyz"));
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("stR"));
    }
    
    @Test
    public void testContainsIgnoreCaseWithMissingSeq() {
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }
    
    @Test
    public void testToString() {
        assertEquals("test string", characterReader.toString());
    }
}