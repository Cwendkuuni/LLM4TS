import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        
        characterReader.consume();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(characterReader.isEmpty());
        
        characterReader.advance();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals(CharacterReader.EOF, characterReader.current());
        
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.current());
        
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals(CharacterReader.EOF, characterReader.consume());
        
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.consume());
        
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        assertEquals(CharacterReader.EOF, characterReader.consume());
        
        characterReader.advance();
        characterReader.unconsume();
        assertEquals(CharacterReader.EOF, characterReader.consume());
        
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals(CharacterReader.EOF, characterReader.advance());
        
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.advance());
        
        characterReader.consume();
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.advance());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(CharacterReader.EOF, characterReader.consume());
        
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(CharacterReader.EOF, characterReader.consume());
        
        characterReader.consume();
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("", characterReader.consumeAsString());
        
        characterReader.advance();
        assertEquals("", characterReader.consumeAsString());
        
        characterReader.consume();
        assertEquals("", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("", characterReader.consumeTo('a'));
        
        characterReader.advance();
        assertEquals("", characterReader.consumeTo('a'));
        
        characterReader.consume();
        assertEquals("", characterReader.consumeTo('a'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("", characterReader.consumeTo("abc"));
        
        characterReader.advance();
        assertEquals("", characterReader.consumeTo("abc"));
        
        characterReader.consume();
        assertEquals("", characterReader.consumeTo("abc"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", characterReader.consumeToAny('a', 'b'));
        
        characterReader.advance();
        assertEquals("", characterReader.consumeToAny('a', 'b'));
        
        characterReader.consume();
        assertEquals("", characterReader.consumeToAny('a', 'b'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("", characterReader.consumeToEnd());
        
        characterReader.advance();
        assertEquals("", characterReader.consumeToEnd());
        
        characterReader.consume();
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("", characterReader.consumeLetterSequence());
        
        characterReader.advance();
        assertEquals("", characterReader.consumeLetterSequence());
        
        characterReader.consume();
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        
        characterReader.advance();
        assertEquals("", characterReader.consumeHexSequence());
        
        characterReader.consume();
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        
        characterReader.advance();
        assertEquals("", characterReader.consumeDigitSequence());
        
        characterReader.consume();
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertFalse(characterReader.matches('a'));
        
        characterReader.advance();
        assertFalse(characterReader.matches('a'));
        
        characterReader.consume();
        assertFalse(characterReader.matches('a'));
    }

    @Test
    public void testMatchesString() {
        assertFalse(characterReader.matches("abc"));
        
        characterReader.advance();
        assertFalse(characterReader.matches("abc"));
        
        characterReader.consume();
        assertFalse(characterReader.matches("abc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(characterReader.matchesIgnoreCase("ABC"));
        
        characterReader.advance();
        assertFalse(characterReader.matchesIgnoreCase("ABC"));
        
        characterReader.consume();
        assertFalse(characterReader.matchesIgnoreCase("ABC"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(characterReader.matchesAny('a', 'b'));
        
        characterReader.advance();
        assertFalse(characterReader.matchesAny('a', 'b'));
        
        characterReader.consume();
        assertFalse(characterReader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        assertFalse(characterReader.matchesLetter());
        
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
        
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        
        characterReader.advance();
        assertFalse(characterReader.matchesDigit());
        
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(characterReader.matchConsume("abc"));
        
        characterReader.advance();
        assertFalse(characterReader.matchConsume("abc"));
        
        characterReader.consume();
        assertFalse(characterReader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(characterReader.matchConsumeIgnoreCase("ABC"));
        
        characterReader.advance();
        assertFalse(characterReader.matchConsumeIgnoreCase("ABC"));
        
        characterReader.consume();
        assertFalse(characterReader.matchConsumeIgnoreCase("ABC"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("abc"));
        
        characterReader.advance();
        assertFalse(characterReader.containsIgnoreCase("abc"));
        
        characterReader.consume();
        assertFalse(characterReader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        assertEquals("", characterReader.toString());
        
        characterReader.advance();
        assertEquals("", characterReader.toString());
        
        characterReader.consume();
        assertEquals("", characterReader.toString());
    }
}