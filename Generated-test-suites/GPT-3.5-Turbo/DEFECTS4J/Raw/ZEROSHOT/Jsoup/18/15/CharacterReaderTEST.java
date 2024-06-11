import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("Test String");
    }
    
    @Test
    public void testPos() {
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        boolean expectedEmpty = false;
        assertEquals(expectedEmpty, reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        char expectedCurrent = 'T';
        assertEquals(expectedCurrent, reader.current());
    }
    
    @Test
    public void testConsume() {
        char expectedVal = 'T';
        char val = reader.consume();
        assertEquals(expectedVal, val);
        int expectedPos = 1;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testUnconsume() {
        reader.unconsume();
        int expectedPos = -1;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testAdvance() {
        reader.advance();
        int expectedPos = 1;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testMark() {
        reader.mark();
        int expectedMark = 0;
        assertEquals(expectedMark, reader.mark);
    }
    
    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        String expectedString = "T";
        String val = reader.consumeAsString();
        assertEquals(expectedString, val);
        int expectedPos = 1;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeTo() {
        String expectedString = "Test ";
        String val = reader.consumeTo(' ');
        assertEquals(expectedString, val);
        int expectedPos = 5;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeToAny() {
        String expectedString = "Test";
        String val = reader.consumeToAny('S', 't');
        assertEquals(expectedString, val);
        int expectedPos = 10;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeToEnd() {
        String expectedString = "t String";
        String val = reader.consumeToEnd();
        assertEquals(expectedString, val);
        int expectedPos = 10;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String expectedString = "Test";
        String val = reader.consumeLetterSequence();
        assertEquals(expectedString, val);
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeHexSequence() {
        String expectedString = "";
        String val = reader.consumeHexSequence();
        assertEquals(expectedString, val);
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String expectedString = "";
        String val = reader.consumeDigitSequence();
        assertEquals(expectedString, val);
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testMatches() {
        boolean expectedMatch = true;
        boolean match = reader.matches('T');
        assertEquals(expectedMatch, match);
    }
    
    @Test
    public void testMatchesAny() {
        boolean expectedMatch = true;
        boolean match = reader.matchesAny('S', 't');
        assertEquals(expectedMatch, match);
    }
    
    @Test
    public void testMatchesLetter() {
        boolean expectedMatch = true;
        boolean match = reader.matchesLetter();
        assertEquals(expectedMatch, match);
    }
    
    @Test
    public void testMatchesDigit() {
        boolean expectedMatch = false;
        boolean match = reader.matchesDigit();
        assertEquals(expectedMatch, match);
    }
    
    @Test
    public void testMatchConsume() {
        boolean expectedMatch = true;
        boolean match = reader.matchConsume("Test");
        assertEquals(expectedMatch, match);
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expectedMatch = true;
        boolean match = reader.matchConsumeIgnoreCase("TEST");
        assertEquals(expectedMatch, match);
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        boolean expectedContains = true;
        boolean contains = reader.containsIgnoreCase("ES");
        assertEquals(expectedContains, contains);
    }
    
    @Test
    public void testToString() {
        String expectedString = "Test String";
        String val = reader.toString();
        assertEquals(expectedString, val);
    }
}