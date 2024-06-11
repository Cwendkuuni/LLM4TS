import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello World");
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
        assertEquals('H', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('l', characterReader.consume());
        assertEquals(3, characterReader.pos());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf_char() {
        assertEquals(2, characterReader.nextIndexOf('l'));
    }
    
    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(2, characterReader.nextIndexOf("ll"));
    }
    
    @Test
    public void testConsumeTo_char() {
        assertEquals("He", characterReader.consumeTo('o'));
    }
    
    @Test
    public void testConsumeTo_String() {
        assertEquals("He", characterReader.consumeTo("lo"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("H", characterReader.consumeToAny(' ', 'o'));
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Hello", characterReader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("Hello", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches_char() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('e'));
    }
    
    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("World"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hElLo"));
        assertFalse(characterReader.matchesIgnoreCase("WORLD"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'e', 'l'));
        assertFalse(characterReader.matchesAny('o', 'W', '_'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("World"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hElLo"));
        assertFalse(characterReader.matchConsumeIgnoreCase("WORLD"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("he"));
        assertFalse(characterReader.containsIgnoreCase("WORLD"));
    }
    
    @Test
    public void testToString() {
        assertEquals("Hello World", characterReader.toString());
    }   

}