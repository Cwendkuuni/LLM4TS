import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test input";
        characterReader = new CharacterReader(input);
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
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.advance();
        characterReader.mark();
        assertEquals(1, characterReader.pos());
        characterReader.consume();
        assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToNotFound() {
        assertEquals("This is a test input", characterReader.consumeTo('?'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("This", characterReader.consumeToAny(' ', 'i'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test input", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "AB23EF";
        CharacterReader hexReader = new CharacterReader(input);
        assertEquals("AB23EF", hexReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "12345";
        CharacterReader digitReader = new CharacterReader(input);
        assertEquals("12345", digitReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('A'));
    }

    @Test
    public void testMatchesSeq() {
        assertTrue(characterReader.matches("This"));
        assertFalse(characterReader.matches("Hello"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("this"));
        assertFalse(characterReader.matchesIgnoreCase("HELLO"));
    }

    @Test
    public void testMatchesAnySeq() {
        assertTrue(characterReader.matchesAny('T', 'A'));
        assertFalse(characterReader.matchesAny('A', 'B'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("This"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("this"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("this"));
        assertFalse(characterReader.containsIgnoreCase("HELLO"));
    }
}