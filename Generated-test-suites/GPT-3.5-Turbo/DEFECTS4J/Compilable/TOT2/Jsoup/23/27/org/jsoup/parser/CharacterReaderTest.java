package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Hello, World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello, ", characterReader.consumeTo(','));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hel", characterReader.consumeToAny(',', ' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello, World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("A12G4F");
        assertEquals("A12G4F", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("12345A");
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('H'));
        assertFalse(characterReader.matches('W'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("hello"));
        assertFalse(characterReader.matchesIgnoreCase("WORLD"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('H', 'W'));
        assertFalse(characterReader.matchesAny('Z', 'X'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("World"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("hello"));
        assertFalse(characterReader.matchConsumeIgnoreCase("WORLD"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("hello"));
        assertFalse(characterReader.containsIgnoreCase("WORLD"));
    }

    @Test
    public void testToString() {
        characterReader.consume();
        characterReader.consume();
        assertEquals("llo, World!", characterReader.toString());
    }
}