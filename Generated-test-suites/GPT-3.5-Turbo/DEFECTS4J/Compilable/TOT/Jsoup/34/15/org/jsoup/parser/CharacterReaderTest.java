package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string.");
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
        
        char expected = 'T';
        characterReader.unconsume();
        char actual = characterReader.current();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        char expected = 'h';
        characterReader.advance();
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
        characterReader.advance();
        characterReader.rewindToMark();
        
        char expected = 'h';
        char actual = characterReader.current();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "T";
        String actual = characterReader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf_char() {
        int expected = 2;
        int actual = characterReader.nextIndexOf('i');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf_sequence() {
        int expected = 5;
        int actual = characterReader.nextIndexOf("is a");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_char() {
        String expected = "This ";
        String actual = characterReader.consumeTo('i');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_sequence() {
        String expected = "This ";
        String actual = characterReader.consumeTo("is a");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "This ";
        String actual = characterReader.consumeToAny('i', 's', 'o');
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
    public void testConsumeLetterThenDigitSequence() {
        String expected = "This";
        String actual = characterReader.consumeLetterThenDigitSequence();
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
    public void testMatches_char() {
        boolean expected = true;
        boolean actual = characterReader.matches('T');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_sequence() {
        boolean expected = true;
        boolean actual = characterReader.matches("This");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("this");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = characterReader.matchesAny('T', 'h', 'i', 's');
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
        boolean actual = characterReader.matchConsume("This");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("this");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.containsIgnoreCase("T");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "This is a test string.";
        String actual = characterReader.toString();
        assertEquals(expected, actual);
    }
}