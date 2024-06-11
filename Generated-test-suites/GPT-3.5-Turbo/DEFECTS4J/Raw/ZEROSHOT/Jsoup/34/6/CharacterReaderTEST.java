package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

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
        boolean expected = false;
        boolean actual = reader.isEmpty();
        assertEquals(expected, actual);
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
        reader.unconsume();
        char expected = 'H';
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
        reader.rewindToMark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "H";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfChar() {
        int expected = 6;
        int actual = reader.nextIndexOf('W');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfSequence() {
        int expected = 6;
        int actual = reader.nextIndexOf("World");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToChar() {
        String expected = "Hello ";
        String actual = reader.consumeTo('W');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToSequence() {
        String expected = "Hello ";
        String actual = reader.consumeTo("World");
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
    public void testConsumeLetterThenDigitSequence() {
        String expected = "Hello";
        String actual = reader.consumeLetterThenDigitSequence();
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
    public void testMatchesWithSequence() {
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
        boolean actual = reader.matchesAny('H');
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
        boolean actual = reader.matchConsumeIgnoreCase("HELLO");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = false;
        boolean actual = reader.containsIgnoreCase("Test");
        assertEquals(expected, actual);
    }
}