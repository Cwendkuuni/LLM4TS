package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("test string");
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
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 't';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        reader.unconsume();
        char expected = 't';
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
        reader.advance();
        reader.rewindToMark();
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "t";
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
    public void testNextIndexOf_InvalidChar() {
        int expected = -1;
        int actual = reader.nextIndexOf('x');
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf_Sequence() {
        int expected = 2;
        int actual = reader.nextIndexOf("st");
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf_Sequence_NotFound() {
        int expected = -1;
        int actual = reader.nextIndexOf("test123");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "te";
        String actual = reader.consumeTo('s');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo_Sequence() {
        String expected = "te";
        String actual = reader.consumeTo("st");
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "test ";
        String actual = reader.consumeToAny('s', 't');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "test string";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "test";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "test";
        String actual = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "test";
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
        boolean actual = reader.matches('t');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_InvalidChar() {
        boolean expected = false;
        boolean actual = reader.matches('x');
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_Sequence() {
        boolean expected = true;
        boolean actual = reader.matches("test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_Sequence_NotFound() {
        boolean expected = false;
        boolean actual = reader.matches("test123");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase_Sequence_NotFound() {
        boolean expected = false;
        boolean actual = reader.matchesIgnoreCase("test123");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = reader.matchesAny('t', 'x');
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
        boolean actual = reader.matchConsume("test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume_NotFound() {
        boolean expected = false;
        boolean actual = reader.matchConsume("test123");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("test");
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase_NotFound() {
        boolean expected = false;
        boolean actual = reader.matchConsumeIgnoreCase("test123");
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = false;
        boolean actual = reader.containsIgnoreCase("123");
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "test string";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}