package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("test input");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('p', reader.consume());
        assertEquals('u', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("test", reader.consumeTo(' '));
        assertEquals(" input", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("t", reader.consumeToAny('a', 'b', 'c'));
        assertEquals("es", reader.consumeToAny('d', 'e', 'f'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("test", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TesT"));
        assertFalse(reader.matchesIgnoreCase("abc"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('t', 'e', 's'));
        assertFalse(reader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TesT"));
        assertFalse(reader.matchConsumeIgnoreCase("abc"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("inPuT"));
        assertFalse(reader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        assertEquals("test input", reader.toString());
    }
}