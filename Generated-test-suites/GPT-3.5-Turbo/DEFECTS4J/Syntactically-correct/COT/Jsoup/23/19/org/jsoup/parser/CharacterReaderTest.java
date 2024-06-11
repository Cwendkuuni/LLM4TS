
import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello World");
    }
    
    // Test for the pos() method
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }
    
    // Test for the isEmpty() method
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }
    
    // Test for the current() method
    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
    }
    
    // Test for the consume() method
    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals(1, reader.pos());
    }
    
    // Test for the unconsume() method
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals(0, reader.pos());
    }
    
    // Test for the advance() method
    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(1, reader.pos());
    }
    
    // Test for the mark() method
    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.mark);
    }
    
    // Test for the rewindToMark() method
    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }
    
    // Test for the consumeAsString() method
    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
        assertEquals(1, reader.pos());
    }
    
    // Test for the consumeTo(char c) method
    @Test
    public void testConsumeToChar() {
        assertEquals("Hello ", reader.consumeTo(' '));
        assertEquals(6, reader.pos());
    }
    
    // Test for the consumeTo(String seq) method
    @Test
    public void testConsumeToString() {
        assertEquals("Hello", reader.consumeTo(" "));
        assertEquals(5, reader.pos());
    }
    
    // Test for the consumeToAny(char... seq) method
    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", reader.consumeToAny(' ', 'W'));
        assertEquals(5, reader.pos());
    }
    
    // Test for the consumeToEnd() method
    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", reader.consumeToEnd());
        assertEquals(11, reader.pos());
    }
    
    // Test for the consumeLetterSequence() method
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals(5, reader.pos());
    }
    
    // Test for the consumeHexSequence() method
    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("ABCD1234");
        assertEquals("ABCD", reader.consumeHexSequence());
        assertEquals(4, reader.pos());
    }
    
    // Test for the consumeDigitSequence() method
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1234", reader.consumeDigitSequence());
        assertEquals(4, reader.pos());
    }
    
    // Test for the matches(char c) method
    @Test
    public void testMatchesChar() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('W'));
    }
    
    // Test for the matches(String seq) method
    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches("World"));
    }
    
    // Test for the matchesIgnoreCase(String seq) method
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        assertFalse(reader.matchesIgnoreCase("WORLD"));
    }
    
    // Test for the matchesAny(char... seq) method
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'W'));
        assertFalse(reader.matchesAny('W', 'R'));
    }
    
    // Test for the matchesLetter() method
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }
    
    // Test for the matchesDigit() method
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }
    
    // Test for the matchConsume(String seq) method
    @Test
    public void testMatchConsumeString() {
        assertFalse(reader.matchConsume("World"));
        assertTrue(reader.matchConsume("Hello"));
        assertEquals(5, reader.pos());
    }
    
    // Test for the matchConsumeIgnoreCase(String seq) method
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("World"));
        assertTrue(reader.matchConsumeIgnoreCase("HELLO"));
        assertEquals(5, reader.pos());
    }
    
    // Test for the containsIgnoreCase(String seq) method
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("HELLO"));
        assertFalse(reader.containsIgnoreCase("WORLD"));
    }
    
    // Test for the toString() method
    @Test
    public void testToString() {
        reader.consume();
        assertEquals("ello World", reader.toString());
    }
}

