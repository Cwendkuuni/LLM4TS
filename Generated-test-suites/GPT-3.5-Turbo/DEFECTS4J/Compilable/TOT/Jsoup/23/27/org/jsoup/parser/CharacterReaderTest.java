package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test string.");
    }
    
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertEquals(5, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        
        // Consume the entire string
        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        
        // Move to position 10
        for (int i = 0; i < 10; i++) {
            reader.advance();
        }
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        
        // Consume the entire string
        StringBuilder sb = new StringBuilder();
        while (!reader.isEmpty()) {
            sb.append(reader.consume());
        }
        assertEquals("This is a test string.", sb.toString());
    }
    
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();
        reader.unconsume();
        
        assertEquals('T', reader.current());
    }
    
    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        
        assertEquals('i', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        
        // Move to position 5 and mark again
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        reader.mark();
        
        // Move to position 10 and rewind to previous mark
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        reader.rewindToMark();
        
        assertEquals(5, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        String consumed = reader.consumeAsString();
        
        assertEquals("T", consumed);
    }
    
    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo('s');
        
        assertEquals("Thi", consumed);
    }
    
    @Test
    public void testConsumeToNotFound() {
        String consumed = reader.consumeTo('!');
        
        assertEquals("This is a test string.", consumed);
    }
    
    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny(' ', 's');
        
        assertEquals("This", consumed);
    }
    
    @Test
    public void testConsumeToEnd() {
        String consumed = reader.consumeToEnd();
        
        assertEquals("This is a test string.", consumed);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();
        
        assertEquals("This", consumed);
    }
    
    @Test
    public void testConsumeHexSequence() {
        // Test with hexadecimal characters from position 10
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        
        String consumed = reader.consumeHexSequence();
        
        assertEquals("74657374", consumed);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        // Test with digits from position 10
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        
        String consumed = reader.consumeDigitSequence();
        
        assertEquals("01234", consumed);
    }
    
    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertTrue(reader.matches('i'));
    }
    
    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("This"));
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertTrue(reader.matches("is a"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("tHIs"));
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertTrue(reader.matchesIgnoreCase("IS A"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'i', 's'));
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertTrue(reader.matchesAny('i', 's', ' '));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        
        // Move to position 10
        for (int i = 0; i < 10; i++) {
            reader.advance();
        }
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("This"));
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertFalse(reader.matchConsume("is A"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("tHIs"));
        
        // Move to position 5
        for (int i = 0; i < 5; i++) {
            reader.advance();
        }
        assertFalse(reader.matchConsumeIgnoreCase("IS A"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("a tEsT"));
        
        assertFalse(reader.containsIgnoreCase("NOt FOUND"));
    }
    
    @Test
    public void testToString() {
        String substring = reader.toString();
        
        assertEquals("This is a test string.", substring);
    }
}