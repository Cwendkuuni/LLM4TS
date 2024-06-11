package org.jsoup.parser;
import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello World");
    }

    @Test
    public void testPos() {
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        char expected = 'H';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'H';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        char expected = 'H';
        reader.unconsume();
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        reader.advance();
        char expected = 'e';
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
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        char expected = 'H';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "H";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "Hello";
        String actual = reader.consumeTo(' ');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_Sequence() {
        String expected = "HelloWorld";
        String actual = reader.consumeTo(" ");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Hello";
        String actual = reader.consumeToAny(' ', 'W');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Hello World";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Hello";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        boolean expected = true;
        boolean actual = reader.matches('H');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_Sequence() {
        boolean expected = true;
        boolean actual = reader.matches("Hello");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("HELLO");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = reader.matchesAny('H', 'W');
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
        boolean actual = reader.matchConsume("Hello");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("hello");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("LO");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "Hello World";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}