package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("Test String");
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
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
        assertEquals("s", characterReader.consumeAsString());
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Test S", characterReader.consumeTo(' '));
        assertEquals("tring", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("Test ", characterReader.consumeTo("String"));
        assertEquals("S", characterReader.consumeTo("t"));
        assertEquals("tring", characterReader.consumeTo(" "));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test ", characterReader.consumeToAny('S', 't'));
        assertEquals("S", characterReader.consumeToAny('t'));
        assertEquals("", characterReader.consumeToAny(' '));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
        assertEquals(" ", characterReader.consumeLetterSequence());
        assertEquals("String", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        characterReader.consume(); // consume 'T'
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.consume(); // consume 'T'
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertFalse(characterReader.matchesIgnoreCase("String"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('S'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume(); // consume 'T'
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume(); // consume 'T'
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("String"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("st"));
        assertFalse(characterReader.containsIgnoreCase("TITLE"));
    }

    @Test
    public void testToString() {
        assertEquals("Test String", characterReader.toString());
    }

}