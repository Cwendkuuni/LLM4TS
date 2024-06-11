import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Hello World!");
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
        assertEquals(0, characterReader.mark);
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
        assertEquals("H", characterReader.consumeAsString());
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(6, characterReader.nextIndexOf('W'));
    }
    
    @Test
    public void testNextIndexOfWithInvalidChar() {
        assertEquals(-1, characterReader.nextIndexOf('X'));
    }
    
    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(6, characterReader.nextIndexOf("World"));
    }
    
    @Test
    public void testNextIndexOfWithInvalidSequence() {
        assertEquals(-1, characterReader.nextIndexOf("Test"));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("Hello ", characterReader.consumeTo('W'));
        assertEquals("World", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToWithInvalidChar() {
        assertEquals("Hello World!", characterReader.consumeTo('X'));
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeToWithSequence() {
        assertEquals("Hello ", characterReader.consumeTo("World"));
        assertEquals("World", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToWithInvalidSequence() {
        assertEquals("Hello World!", characterReader.consumeTo("Test"));
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny(' '));
        assertEquals(" ", characterReader.consumeToAny('!'));
        assertEquals("World!", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Hello World!!", characterReader.consumeToAnySorted(' ', '!'));
    }
    
    @Test
    public void testConsumeData() {
        assertEquals("Hello ", characterReader.consumeData());
        assertEquals("World!", characterReader.consumeData());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("Hello", characterReader.consumeTagName());
        assertEquals(" ", characterReader.consumeTagName());
        assertEquals("World!", characterReader.consumeToEnd());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World!", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
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
        assertEquals("Hell", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('W'));
    }
    
    @Test
    public void testMatchesWithSequence() {
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches("World"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("world"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'W'));
        assertFalse(characterReader.matchesAny('A', 'B'));
    }
    
    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('H', 'W'));
        assertFalse(characterReader.matchesAnySorted('A', 'B'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("World"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("world"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertFalse(characterReader.containsIgnoreCase("Test"));
        assertTrue(characterReader.containsIgnoreCase("Hello"));
    }
    
    @Test
    public void testToString() {
        assertEquals("Hello World!", characterReader.toString());
    }
}