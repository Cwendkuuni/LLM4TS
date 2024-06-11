
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello");
        int pos = reader.pos();
        assertEquals(0, pos);
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        boolean empty = reader.isEmpty();
        assertTrue(empty);
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello");
        char current = reader.current();
        assertEquals('H', current);
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        char consumed = reader.consume();
        assertEquals('H', consumed);
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        reader.unconsume();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello");
        String consumed = reader.consumeAsString();
        assertEquals("H", consumed);
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello");
        String consumed = reader.consumeTo('o');
        assertEquals("Hell", consumed);
        assertEquals(4, reader.pos());
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello");
        String consumed = reader.consumeToAny('a', 'o');
        assertEquals("Hell", consumed);
        assertEquals(4, reader.pos());
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello");
        String consumed = reader.consumeToEnd();
        assertEquals("Hello", consumed);
        assertEquals(5, reader.pos());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        String consumed = reader.consumeLetterSequence();
        assertEquals("Hello", consumed);
        assertEquals(5, reader.pos());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        String consumed = reader.consumeHexSequence();
        assertEquals("ABC", consumed);
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean matches = reader.matches('H');
        assertTrue(matches);
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean matches = reader.matchesAny('H', 'e', 'l', 'o');
        assertTrue(matches);
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean matches = reader.matchesLetter();
        assertTrue(matches);
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        boolean matches = reader.matchesDigit();
        assertTrue(matches);
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean matchConsume = reader.matchConsume("Hell");
        assertTrue(matchConsume);
        assertEquals(4, reader.pos());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean matchConsume = reader.matchConsumeIgnoreCase("hell");
        assertTrue(matchConsume);
        assertEquals(4, reader.pos());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean containsIgnoreCase = reader.containsIgnoreCase("hell");
        assertTrue(containsIgnoreCase);
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        String string = reader.toString();
        assertEquals("Hello", string);
    }
}

