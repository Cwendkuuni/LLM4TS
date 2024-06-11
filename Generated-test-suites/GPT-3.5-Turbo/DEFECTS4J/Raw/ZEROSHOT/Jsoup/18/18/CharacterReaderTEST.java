import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("test"); //Change input string here for different test cases
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
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        characterReader.consume();
        characterReader.advance();
        assertEquals('s', characterReader.current());
    }
    
    @Test
    public void testMark() {
        characterReader.consume();
        characterReader.mark();
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.consume();
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("t", characterReader.consumeTo('e'));
        assertEquals("s", characterReader.consumeTo('t'));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("t", characterReader.consumeToAny('e', 'r'));
        assertEquals("", characterReader.consumeToAny('x', 'y'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("test", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        characterReader.consume(); // Skip 't'
        assertEquals("est", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader.consume(); // Skip 't'
        assertEquals("est", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader.consume(); // Skip 't'
        assertEquals("est", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        characterReader.consume(); // Skip 't'
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('e'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        characterReader.consume(); // Skip 't'
        assertTrue(characterReader.matchesIgnoreCase("T"));
        assertFalse(characterReader.matchesIgnoreCase("E"));
    }
    
    @Test
    public void testMatchesAny() {
        characterReader.consume(); // Skip 't'
        assertTrue(characterReader.matchesAny('e', 's'));
        assertFalse(characterReader.matchesAny('x', 'y'));
    }
    
    @Test
    public void testMatchesLetter() {
        characterReader.consume(); // Skip 't'
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        characterReader.consume(); // Skip 't'
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        characterReader.consume(); // Skip 't'
        assertTrue(characterReader.matchConsume("e"));
        assertFalse(characterReader.matchConsume("T"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        characterReader.consume(); // Skip 't'
        assertTrue(characterReader.matchConsumeIgnoreCase("E"));
        assertFalse(characterReader.matchConsumeIgnoreCase("T"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("es"));
        assertFalse(characterReader.containsIgnoreCase("TE"));
    }
    
    @Test
    public void testToString() {
        characterReader.consume(); // Skip 't'
        assertEquals("est", characterReader.toString());
    }
}