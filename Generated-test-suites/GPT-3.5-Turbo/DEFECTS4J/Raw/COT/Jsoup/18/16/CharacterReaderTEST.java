
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        characterReader = new CharacterReader("test input");
    }
  
    @Test
    public void testPos() {
        int expected = 0;
        int actual = characterReader.pos();
        assertEquals(expected, actual);
    }
  
    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = characterReader.isEmpty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCurrent() {
        char expected = 't';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsume() {
        char expected = 't';
        char actual = characterReader.consume();
        assertEquals(expected, actual);
    }
  
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        char expected = 't';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
  
    @Test
    public void testAdvance() {
        characterReader.advance();
        char expected = 'e';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
  
    @Test
    public void testMark() {
        characterReader.mark();
        int expected = 0;
        int actual = characterReader.pos();
        assertEquals(expected, actual);
    }
  
    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        char expected = 't';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeAsString() {
        String expected = "t";
        String actual = characterReader.consumeAsString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeTo() {
        String expected = "tes";
        String actual = characterReader.consumeTo('t');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToWithNoOccurrence() {
        String expected = "test input";
        String actual = characterReader.consumeTo('z');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToAny() {
        String expected = "t";
        String actual = characterReader.consumeToAny('e', 's');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToEnd() {
        String expected = " input";
        String actual = characterReader.consumeToEnd();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String expected = "test";
        String actual = characterReader.consumeLetterSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeHexSequence() {
        String expected = "";
        String actual = characterReader.consumeHexSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String expected = "";
        String actual = characterReader.consumeDigitSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        boolean expected = true;
        boolean actual = characterReader.matches('t');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesWithNoMatch() {
        boolean expected = false;
        boolean actual = characterReader.matches('z');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("TEST");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = characterReader.matchesAny('t', 'z');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesAnyWithNoMatch() {
        boolean expected = false;
        boolean actual = characterReader.matchesAny('a', 'b');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = characterReader.matchesLetter();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesDigit() {
        boolean expected = false;
        boolean actual = characterReader.matchesDigit();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsume() {
        boolean expected = true;
        boolean actual = characterReader.matchConsume("test");
        assertEquals(expected, actual);
    }
  
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("TEST");
        assertEquals(expected, actual);
    }
  
    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.containsIgnoreCase("IN");
        assertEquals(expected, actual);
    }
  
    @Test
    public void testToString() {
        String expected = "test input";
        String actual = characterReader.toString();
        assertEquals(expected, actual);
    }
}
