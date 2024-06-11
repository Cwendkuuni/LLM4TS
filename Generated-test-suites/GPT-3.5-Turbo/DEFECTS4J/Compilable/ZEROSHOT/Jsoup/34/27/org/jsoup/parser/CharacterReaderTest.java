package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string.");
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
        assertEquals(1, characterReader.pos());
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
        characterReader.mark();
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(2, characterReader.nextIndexOf('i'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(2, characterReader.nextIndexOf("is"));
        assertEquals(-1, characterReader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Th", characterReader.consumeTo('i'));
        assertEquals("is", characterReader.consumeTo(' '));
        assertEquals("", characterReader.consumeTo('x'));
    }

    @Test
    public void testConsumeToCharSequence() {
        assertEquals("Th", characterReader.consumeTo("is"));
        assertEquals("is ", characterReader.consumeTo(" a"));
        assertEquals("", characterReader.consumeTo("xx"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Th", characterReader.consumeToAny('i', 's'));
        assertEquals("is", characterReader.consumeToAny(' '));
        assertEquals("", characterReader.consumeToAny('x', 'y'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string.", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        assertEquals("is", characterReader.consumeLetterSequence());
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Th", characterReader.consumeLetterThenDigitSequence());
        assertEquals("is", characterReader.consumeLetterThenDigitSequence());
        assertEquals("a", characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        characterReader = new CharacterReader("123ABC456");
        assertEquals("123ABC", characterReader.consumeHexSequence());
        assertEquals("456", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader = new CharacterReader("123ABC456");
        assertEquals("123", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("t"));
        assertTrue(characterReader.matchesIgnoreCase("tH"));
        assertFalse(characterReader.matchesIgnoreCase("T"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 't'));
        assertFalse(characterReader.matchesAny('A', 'B'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Th"));
        assertFalse(characterReader.matchConsume("is"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("th"));
        assertFalse(characterReader.matchConsumeIgnoreCase("IS"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("is"));
        assertFalse(characterReader.containsIgnoreCase("aB"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
    }
}