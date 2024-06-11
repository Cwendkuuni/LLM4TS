package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
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
        CharacterReader reader = new CharacterReader("test");
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char expected = 't';
        char actual = reader.consume();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        char expected = 'e';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        char expected = 't';
        char actual = reader.current();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "t";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "tes";
        String actual = reader.consumeTo('t');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "te";
        String actual = reader.consumeTo("st");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "te";
        String actual = reader.consumeToAny('s', 't');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123abcd");
        String expected = "0123abcd";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123");
        String expected = "0123";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matches('t');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matches("te");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("Te");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchesAny('s', 't');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("0123");
        boolean expected = true;
        boolean actual = reader.matchesDigit();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.matchConsume("te");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("T");
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("t");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("Te");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        String expected = "test";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}

