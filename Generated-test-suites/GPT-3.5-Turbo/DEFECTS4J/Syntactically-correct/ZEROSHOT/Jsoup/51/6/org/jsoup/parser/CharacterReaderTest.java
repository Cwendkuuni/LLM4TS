import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    CharacterReader characterReader;

    @Test
    public void testPos() {
        characterReader = new CharacterReader("test");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        characterReader = new CharacterReader("test");
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        characterReader = new CharacterReader("test");
        assertEquals('t', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        characterReader = new CharacterReader("test");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        characterReader = new CharacterReader("test");
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }
    
    @Test
    public void testMark() {
        characterReader = new CharacterReader("test");
        characterReader.mark();
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }
   
    @Test
    public void testConsumeAsString() {
        characterReader = new CharacterReader("test");
        assertEquals("t", characterReader.consumeAsString());
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testNextIndexOf() {
        characterReader = new CharacterReader("test");
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(3, characterReader.nextIndexOf('t'));
    }
    
    @Test
    public void testNextIndexOfCharSequence() {
        characterReader = new CharacterReader("test");
        assertEquals(1, characterReader.nextIndexOf("es"));
        assertEquals(0, characterReader.nextIndexOf("te"));
    }
    
    @Test
    public void testConsumeTo() {
        characterReader = new CharacterReader("test");
        assertEquals("", characterReader.consumeTo('t'));
        assertEquals("es", characterReader.consumeTo('t'));
    }
    
    @Test
    public void testConsumeToAny() {
        characterReader = new CharacterReader("test");
        assertEquals("", characterReader.consumeToAny('e', 't'));
        assertEquals("st", characterReader.consumeToAny('e', 't'));
    }
    
    @Test
    public void testConsumeToAnySorted() {
        characterReader = new CharacterReader("test");
        assertEquals("", characterReader.consumeToAnySorted('e', 't'));
        assertEquals("st", characterReader.consumeToAnySorted('e', 't'));
    }
    
    @Test
    public void testConsumeData() {
        characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.consumeData());
    }
    
    @Test
    public void testConsumeTagName() {
        characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.consumeTagName());
    }
    
    @Test
    public void testConsumeToEnd() {
        characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        characterReader = new CharacterReader("test");
        assertEquals("test", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader = new CharacterReader("test123");
        assertEquals("test", characterReader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("test123");
        assertEquals("", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("test123");
        assertEquals("", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matches('e'));
        assertTrue(characterReader.matches('t'));
    }
    
    @Test
    public void testMatchesString() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matches("es"));
        assertTrue(characterReader.matches("test"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchesIgnoreCase("Es"));
        assertTrue(characterReader.matchesIgnoreCase("TeSt"));
    }
    
    @Test
    public void testMatchesAny() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchesAny('e', 'a'));
        assertTrue(characterReader.matchesAny('e', 't'));
    }
    
    @Test
    public void testMatchesAnySorted() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchesAnySorted('e', 'a'));
        assertTrue(characterReader.matchesAnySorted('e', 't'));
    }
    
    @Test
    public void testMatchesLetter() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchConsume("es"));
        assertTrue(characterReader.matchConsume("test"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.matchConsumeIgnoreCase("Es"));
        assertTrue(characterReader.matchConsumeIgnoreCase("TeSt"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        characterReader = new CharacterReader("test");
        assertFalse(characterReader.containsIgnoreCase("Es"));
        assertTrue(characterReader.containsIgnoreCase("TeSt"));
    }

}