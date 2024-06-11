package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello World!");
    }

    // Test for pos() method
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    // Test for isEmpty() method
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    // Test for current() method
    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
    }

    // Test for consume() method
    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    // Test for unconsume() method
    @Test
    public void testUnconsume() {
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    // Test for advance() method
    @Test
    public void testAdvance() {
        assertEquals('H', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    // Test for mark() and rewindToMark() methods
    @Test
    public void testMarkAndRewindToMark() {
        reader.advance(); // Advances to 'e'
        reader.mark(); // Sets mark position
        reader.advance(); // Advances to 'l'
        assertEquals('l', reader.current()); // Current is 'l'
        reader.rewindToMark(); // Rewinds to previous mark position
        assertEquals('e', reader.current()); // Current is 'e'
    }

    // Test for consumeAsString() method
    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
    }

    // Test for consumeTo(char) method
    @Test
    public void testConsumeToChar() {
        assertEquals("Hello W", reader.consumeTo('r'));
    }

    // Test for consumeTo(String) method
    @Test
    public void testConsumeToString() {
        assertEquals("Hello ", reader.consumeTo("World"));
    }

    // Test for consumeToAny(char...) method
    @Test
    public void testConsumeToAny() {
        assertEquals("Hello ", reader.consumeToAny('o', 'r'));
    }

    // Test for consumeToEnd() method
    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World!", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    // Test for consumeLetterSequence() method
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    // Test for consumeHexSequence() method
    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("ABC123");
        assertEquals("ABC123", reader.consumeHexSequence());
    }

    // Test for consumeDigitSequence() method
    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("123ABC");
        assertEquals("123", reader.consumeDigitSequence());
    }

    // Test for matches(char) method
    @Test
    public void testMatchesChar() {
        assertTrue(reader.matches('H'));
        assertFalse(reader.matches('h'));
    }

    // Test for matches(String) method
    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("Hello"));
        assertFalse(reader.matches("hello"));
    }

    // Test for matchesIgnoreCase(String) method
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("hello"));
    }

    // Test for matchesAny(char...) method
    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'h'));
        assertFalse(reader.matchesAny('e', 'l'));
    }

    // Test for matchesLetter() method
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    // Test for matchesDigit() method
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    // Test for matchConsume(String) method
    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("H"));
        assertFalse(reader.matchConsume("h"));
    }

    // Test for matchConsumeIgnoreCase(String) method
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
    }

    // Test for containsIgnoreCase(String) method
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("llo"));
        assertFalse(reader.containsIgnoreCase("ll"));
    }
}