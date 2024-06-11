import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() throws Exception {
        characterReader = new CharacterReader("This is a test");
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        for (int i = 0; i < "This is a test".length(); i++) {
            characterReader.consume();
        }
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("h", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This ", characterReader.consumeTo(' '));
        assertEquals("is", characterReader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' ', 'i'));
        assertEquals("", characterReader.consumeToAny(' ', 'i'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals("", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        characterReader.consume();
        assertEquals("", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.consume();
        assertEquals("", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('X'));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'i'));
        assertFalse(characterReader.matchesAny('W', 'x'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Th"));
        assertFalse(characterReader.matchConsume("is"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("th"));
        assertFalse(characterReader.matchConsumeIgnoreCase("IS"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("Th"));
        assertFalse(characterReader.containsIgnoreCase("IS"));
    }
    
    @Test
    public void testToString() {
        assertEquals("This is a test", characterReader.toString());
        characterReader.consume();
        assertEquals("his is a test", characterReader.toString());
    }
    
}