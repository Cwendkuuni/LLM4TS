import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
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
        assertEquals('L', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('L', characterReader.consume());
        assertEquals('o', characterReader.consume());
        assertEquals('r', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('m', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('o', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('o', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('L', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("L", characterReader.consumeAsString());
        assertEquals("o", characterReader.consumeAsString());
        assertEquals("r", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        assertEquals("m", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Lorem ipsum dol", characterReader.consumeTo('l'));
        assertEquals("sit amet, consectetur adipiscin", characterReader.consumeTo('g'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("Lorem ipsum dolor sit amet, consectetu", characterReader.consumeTo("r "));
        assertEquals("adipiscing elit.", characterReader.consumeTo("nonexistent"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Lorem ipsu", characterReader.consumeToAny('m', ' '));
        assertEquals("dolor ", characterReader.consumeToAny('i', 's'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Lorem", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertFalse(characterReader.matches("0x"));
        characterReader.advance();
        assertFalse(characterReader.matches("a"));
        characterReader.advance();
        assertTrue(characterReader.matches("b"));
        assertEquals("b", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1234", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches("Lorem"));
        assertFalse(characterReader.matches("l"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("lorEm"));
        assertFalse(characterReader.matchesIgnoreCase("lorem"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('L', 'o'));
        assertFalse(characterReader.matchesAny('l', 'm'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Lorem"));
        assertEquals(5, characterReader.pos());
        assertFalse(characterReader.matchConsume("ipsum"));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("lorEm"));
        assertEquals(5, characterReader.pos());
        assertFalse(characterReader.matchConsumeIgnoreCase("lorem"));
        assertEquals(5, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("ipsum"));
        assertFalse(characterReader.containsIgnoreCase("amet"));
    }

    @Test
    public void testToString() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", characterReader.toString());
    }
}