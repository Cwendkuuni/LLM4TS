package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup(){
        reader = new CharacterReader("Hello World");
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
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.advance();
        reader.advance();
        reader.mark();
        assertEquals('l', reader.current());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello ", reader.consumeTo('W'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello ", reader.consumeToAny('W','o'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("ABC123");
        assertEquals("ABC123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("123ABC");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H','e', 'l'));
        assertFalse(reader.matchesAny('e', 'l'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader = new CharacterReader("123");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        reader = new CharacterReader("ABC");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("H"));
        assertEquals('e', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("h"));
        assertEquals('e', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("LO"));
        assertFalse(reader.containsIgnoreCase("lo"));
    }

    @Test
    public void testToString() {
        reader.advance();
        assertEquals("ello World", reader.toString());
    }

}