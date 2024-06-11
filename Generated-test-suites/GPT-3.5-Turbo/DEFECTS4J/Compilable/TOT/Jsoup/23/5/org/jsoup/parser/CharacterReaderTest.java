package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        String input = "this is a test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testRewindToMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "test test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test ", reader.consumeTo(' '));
        assertEquals("test", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        String input = "test,test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeToAny(','));
        assertEquals(",test", reader.consumeToAny(','));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "aBc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("a", reader.consumeLetterSequence());
        assertEquals("Bc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "12af";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("12", reader.consumeHexSequence());
        assertEquals("af", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testMatches() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("Te"));
        assertFalse(reader.matchesIgnoreCase("xx"));
    }

    @Test
    public void testMatchesAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('t', 'x'));
        assertFalse(reader.matchesAny('a', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("xx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("te"));
        assertFalse(reader.matchConsumeIgnoreCase("xx"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<title>Test</title>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertFalse(reader.containsIgnoreCase("</style>"));
    }

    @Test
    public void testToString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.toString());
    }
}