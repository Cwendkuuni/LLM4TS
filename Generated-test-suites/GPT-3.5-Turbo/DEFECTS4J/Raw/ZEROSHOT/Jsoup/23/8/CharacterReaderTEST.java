import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        String input = "This is a test string.";
        characterReader = new CharacterReader(input);
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
        char expected = 'T';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsume() {
        char expected = 'T';
        char actual = characterReader.consume();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        
        char expected = 'T';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        
        char expected = 'h';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.advance();
        
        int expected = 1;
        int actual = characterReader.pos();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        
        int expected = 0;
        int actual = characterReader.pos();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeAsString() {
        String expected = "T";
        String actual = characterReader.consumeAsString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeTo_char() {
        String expected = "This is a";
        String actual = characterReader.consumeTo('s');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeTo_string() {
        String expected = "This is ";
        String actual = characterReader.consumeTo("a");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToAny() {
        String expected = "This ";
        String actual = characterReader.consumeToAny('i', 's', ' ');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToEnd() {
        String expected = "This is a test string.";
        String actual = characterReader.consumeToEnd();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String expected = "This";
        String actual = characterReader.consumeLetterSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeHexSequence() {
        String expected = "";
        String actual = characterReader.consumeHexSequence();
        assertEquals(expected, actual);
        characterReader.advance();
        actual = characterReader.consumeHexSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String expected = "";
        String actual = characterReader.consumeDigitSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches_char() {
        boolean expected = true;
        boolean actual = characterReader.matches('T');
        assertEquals(expected, actual);
        
        characterReader.consume();
        
        expected = false;
        actual = characterReader.matches('T');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches_string() {
        boolean expected = true;
        boolean actual = characterReader.matches("This");
        assertEquals(expected, actual);
        
        characterReader.consume();
        
        expected = false;
        actual = characterReader.matches("This");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesIgnoreCase_string() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("This");
        assertEquals(expected, actual);
        
        characterReader.consume();
        
        expected = false;
        actual = characterReader.matchesIgnoreCase("This");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = characterReader.matchesAny('T', 'h', 'i', 's');
        assertEquals(expected, actual);
        
        characterReader.consume();
        
        expected = false;
        actual = characterReader.matchesAny('T', 'h', 'i', 's');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = characterReader.matchesLetter();
        assertEquals(expected, actual);
        
        characterReader.consume();
        
        expected = false;
        actual = characterReader.matchesLetter();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesDigit() {
        boolean expected = false;
        boolean actual = characterReader.matchesDigit();
        assertEquals(expected, actual);
        
        characterReader.consume();
        
        expected = true;
        actual = characterReader.matchesDigit();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsume() {
        boolean expected = true;
        boolean actual = characterReader.matchConsume("This");
        assertEquals(expected, actual);
        
        expected = false;
        actual = characterReader.matchConsume("This");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("This");
        assertEquals(expected, actual);
        
        expected = false;
        actual = characterReader.matchConsumeIgnoreCase("This");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.containsIgnoreCase("This");
        assertEquals(expected, actual);
        
        characterReader.consume(4);
        
        expected = false;
        actual = characterReader.containsIgnoreCase("This");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        String expected = "This is a test string.";
        String actual = characterReader.toString();
        assertEquals(expected, actual);
        
        characterReader.consume(4);
        
        expected = " is a test string.";
        actual = characterReader.toString();
        assertEquals(expected, actual);
    }
    
}