import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test Input");
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
        char expected = 'e';
        char actual = characterReader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        characterReader.advance();
        characterReader.mark();
        int expected = 1;
        int actual = characterReader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        int expected = 1;
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
    public void testConsumeTo_Char() {
        String expected = "Test In";
        String actual = characterReader.consumeTo('p');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_String() {
        String expected = "Te";
        String actual = characterReader.consumeTo("st");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Test ";
        String actual = characterReader.consumeToAny('I', 'n', 'u', 't');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Test Input";
        String actual = characterReader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Test";
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
    public void testMatches_Char() {
        boolean expected = false;
        boolean actual = characterReader.matches('p');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_String() {
        boolean expected = true;
        boolean actual = characterReader.matches("Test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("tEsT");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = characterReader.matchesAny('I', 'n', 'u', 't');
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
    public void testMatchConsume_String() {
        boolean expected = true;
        boolean actual = characterReader.matchConsume("Test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("tEsT");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = false;
        boolean actual = characterReader.containsIgnoreCase("abc");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "Test Input";
        String actual = characterReader.toString();
        assertEquals(expected, actual);
    }
}