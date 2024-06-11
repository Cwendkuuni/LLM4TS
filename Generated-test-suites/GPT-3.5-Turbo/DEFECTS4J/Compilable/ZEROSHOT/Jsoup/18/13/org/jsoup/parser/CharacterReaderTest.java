package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test Input");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.advance();
        reader.mark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test Inp", reader.consumeTo('u'));
        assertEquals("t", reader.consumeTo('s'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test Inp", reader.consumeToAny('u', 's'));
        assertEquals("t", reader.consumeToAny('o', 'p'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        reader = new CharacterReader("Test123");
        assertEquals("Test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("0123456789ABCDEFabcdef");
        assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("0123456789");
        assertEquals("0123456789", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TeST"));
        assertFalse(reader.matchesIgnoreCase("TeSt"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'e'));
        assertFalse(reader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader = new CharacterReader("123");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        reader = new CharacterReader("abc");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Te"));
        assertFalse(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("Te"));
        assertFalse(reader.matchConsumeIgnoreCase("te"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("TeSt"));
    }

    @Test
    public void testToString() {
        assertEquals("Test Input", reader.toString());
    }
}