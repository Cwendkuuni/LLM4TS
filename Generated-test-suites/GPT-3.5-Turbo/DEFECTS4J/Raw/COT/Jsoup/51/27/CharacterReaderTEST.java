
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test Input");
    }

    @Test
    public void testPos() {
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        char expected = 'T';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'T';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        char expected = 'T';
        reader.consume();
        reader.unconsume();
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        char expected = 'e';
        reader.advance();
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        reader.mark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        int expected = 1;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "T";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf() {
        int expected = 2;
        int actual = reader.nextIndexOf('s');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfWithSequence() {
        int expected = 2;
        int actual = reader.nextIndexOf("st");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "Tes";
        String actual = reader.consumeTo('t');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToWithSequence() {
        String expected = "Te";
        String actual = reader.consumeTo("st");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Test ";
        String actual = reader.consumeToAny('I', 'n', 'p', 'u', 't');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = " Test";
        String actual = reader.consumeToAnySorted(' ', 'I', 'T', 'n', 'p', 'u');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        String expected = "Test Input";
        String actual = reader.consumeData();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "Test";
        String actual = reader.consumeTagName();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Test Input";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Test";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "Test1";
        String actual = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "Test1F";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "1";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        boolean expected = true;
        boolean actual = reader.matches('T');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesWithSequence() {
        boolean expected = true;
        boolean actual = reader.matches("Test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("TEST");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = reader.matchesAny('T', 'e', 's', 't', ' ', 'I', 'n', 'p', 'u');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAnySorted() {
        boolean expected = true;
        boolean actual = reader.matchesAnySorted(' ', 'I', 'T', 'n', 'p', 'u');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        boolean expected = false;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        boolean expected = true;
        boolean actual = reader.matchConsume("Test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("TEST");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("TN");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "Test Input";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRangeEquals() {
        boolean expected = false;
        boolean actual = reader.rangeEquals(0, 4, "Test");
        assertEquals(expected, actual);
    }
}
