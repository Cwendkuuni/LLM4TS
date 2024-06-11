import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test case for pos() method

        CharacterReader reader = new CharacterReader("input");
        int pos = reader.pos();

        // Check if the returned value is correct
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        // Test case for isEmpty() method
        
        CharacterReader reader = new CharacterReader("input");
        boolean isEmpty = reader.isEmpty();
        
        // Check if the returned value is correct
        assertFalse(isEmpty);
    }

    @Test
    public void testCurrent() {
        // Test case for current() method
        
        CharacterReader reader = new CharacterReader("input");
        char current = reader.current();
        
        // Check if the returned value is correct
        assertEquals('i', current);
    }

    @Test
    public void testConsume() {
        // Test case for consume() method
        
        CharacterReader reader = new CharacterReader("input");
        char consumed = reader.consume();
        
        // Check if the returned value is correct
        assertEquals('i', consumed);
        
        // Check if the position is incremented
        assertEquals(1, reader.pos());
    }

    @Test
    public void testUnconsume() {
        // Test case for unconsume() method
        
        CharacterReader reader = new CharacterReader("input");
        reader.unconsume();
        
        // Check if the position is decremented
        assertEquals(-1, reader.pos());
    }

    @Test
    public void testAdvance() {
        // Test case for advance() method
        
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        
        // Check if the position is incremented
        assertEquals(1, reader.pos());
    }

    @Test
    public void testMark() {
        // Test case for mark() method
        
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        
        // Check if the mark is set correctly
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        // Test case for rewindToMark() method
        
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        reader.advance();
        reader.rewindToMark();

        // Check if the position is reset to the marked position
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        // Test case for consumeAsString() method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeAsString();
        
        // Check if the returned value is correct
        assertEquals("i", consumed);
        
        // Check if the position is incremented
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeTo() {
        // Test case for consumeTo(char c) method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeTo('n');
        
        // Check if the returned value is correct
        assertEquals("i", consumed);
        
        // Check if the position is incremented
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        // Test case for consumeToAny(char... seq) method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeToAny('t', 'u', 'p');
        
        // Check if the returned value is correct
        assertEquals("i", consumed);
        
        // Check if the position is incremented
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        // Test case for consumeToEnd() method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeToEnd();
        
        // Check if the returned value is correct
        assertEquals("input", consumed);
        
        // Check if the position is incremented
        assertEquals(5, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        // Test case for consumeLetterSequence() method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeLetterSequence();
        
        // Check if the returned value is correct
        assertEquals("input", consumed);
        
        // Check if the position is incremented
        assertEquals(5, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        // Test case for consumeHexSequence() method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeHexSequence();
        
        // Check if the returned value is correct
        assertEquals("", consumed);
        
        // Check if the position is incremented
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        // Test case for consumeDigitSequence() method
        
        CharacterReader reader = new CharacterReader("input");
        String consumed = reader.consumeDigitSequence();
        
        // Check if the returned value is correct
        assertEquals("", consumed);
        
        // Check if the position is incremented
        assertEquals(0, reader.pos());
    }

    @Test
    public void testMatches() {
        // Test case for matches(char c) method
        
        CharacterReader reader = new CharacterReader("input");
        boolean matches = reader.matches('i');
        
        // Check if the returned value is correct
        assertTrue(matches);
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Test case for matchesIgnoreCase(String seq) method
        
        CharacterReader reader = new CharacterReader("input");
        boolean matches = reader.matchesIgnoreCase("Input");
        
        // Check if the returned value is correct
        assertTrue(matches);
    }

    @Test
    public void testMatchesAny() {
        // Test case for matchesAny(char... seq) method
        
        CharacterReader reader = new CharacterReader("input");
        boolean matches = reader.matchesAny('i', 'n', 'u', 'p', 'u');
        
        // Check if the returned value is correct
        assertTrue(matches);
    }

    @Test
    public void testMatchesLetter() {
        // Test case for matchesLetter() method
        
        CharacterReader reader = new CharacterReader("input");
        boolean matches = reader.matchesLetter();
        
        // Check if the returned value is correct
        assertTrue(matches);
    }

    @Test
    public void testMatchesDigit() {
        // Test case for matchesDigit() method
        
        CharacterReader reader = new CharacterReader("input");
        boolean matches = reader.matchesDigit();
        
        // Check if the returned value is correct
        assertFalse(matches);
    }
    
    @Test
    public void testMatchConsume() {
        // Test case for matchConsume(String seq) method
        
        CharacterReader reader = new CharacterReader("input");
        boolean result = reader.matchConsume("input");
        
        // Check if the returned value is correct
        assertTrue(result);
        
        // Check if the position is incremented
        assertEquals(5, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // Test case for matchConsumeIgnoreCase(String seq) method
        
        CharacterReader reader = new CharacterReader("input");
        boolean result = reader.matchConsumeIgnoreCase("Input");
        
        // Check if the returned value is correct
        assertTrue(result);
        
        // Check if the position is incremented
        assertEquals(5, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        // Test case for containsIgnoreCase(String seq) method
        
        CharacterReader reader = new CharacterReader("input");
        boolean contains = reader.containsIgnoreCase("Np");
        
        // Check if the returned value is correct
        assertTrue(contains);
    }

    @Test
    public void testToString() {
        // Test case for toString() method
        
        CharacterReader reader = new CharacterReader("input");
        String str = reader.toString();
        
        // Check if the returned value is correct
        assertEquals("input", str);
    }
}