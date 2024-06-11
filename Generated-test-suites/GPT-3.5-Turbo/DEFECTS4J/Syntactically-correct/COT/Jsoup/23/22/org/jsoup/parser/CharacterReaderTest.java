
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello");
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        boolean expected = true;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello");
        char expected = 'H';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        char expected = 'H';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.consume();
        reader.unconsume();
        char expected = 'H';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        char expected = 'e';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.mark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("Hello");
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello");
        String expected = "H";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "Hello ";
        String actual = reader.consumeTo(' ');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "Hello";
        String actual = reader.consumeToAny(' ', 'W');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello");
        String expected = "Hello";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello123");
        String expected = "Hello";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        String expected = "ABC123";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABC");
        String expected = "123";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.matches('H');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("h");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.matchesAny('H', 'h');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        boolean expected = true;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.matchConsume("Hell");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("hell");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("ell");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello");
        String expected = "Hello";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}
