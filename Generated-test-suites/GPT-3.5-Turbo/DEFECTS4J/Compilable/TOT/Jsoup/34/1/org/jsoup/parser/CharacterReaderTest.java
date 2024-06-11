package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test123");
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
        assertEquals('T', characterReader.consume());
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
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
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        characterReader.mark();
        assertEquals('T', characterReader.current());
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
    public void testNextIndexOf_char() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(2, characterReader.nextIndexOf('s'));
        assertEquals(3, characterReader.nextIndexOf('t'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(1, characterReader.nextIndexOf("es"));
        assertEquals(2, characterReader.nextIndexOf("st"));
        assertEquals(3, characterReader.nextIndexOf("t1"));
    }

    @Test
    public void testConsumeTo_char() {
        assertEquals("Test", characterReader.consumeTo('1'));
        assertEquals("123", characterReader.consumeTo('X'));
    }

    @Test
    public void testConsumeTo_CharSequence() {
        assertEquals("Test", characterReader.consumeTo("1"));
        assertEquals("123", characterReader.consumeTo("X"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Test", characterReader.consumeToAny('1', '2'));
        assertEquals("3", characterReader.consumeToAny('X'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test123", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", characterReader.consumeLetterSequence());
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test123", characterReader.consumeLetterThenDigitSequence());
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertFalse(characterReader.matches('1'));
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testMatches_CharSequence() {
        assertFalse(characterReader.matches("abc"));
        assertTrue(characterReader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(characterReader.matchesIgnoreCase("abc"));
        assertTrue(characterReader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('a', 'b', 'c'));
        assertFalse(characterReader.matchesAny('X', 'Y', 'Z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Te"));
        assertFalse(characterReader.matchConsume("X"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("TE"));
        assertFalse(characterReader.matchConsumeIgnoreCase("X"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("Test"));
        assertFalse(characterReader.containsIgnoreCase("123"));
    }

    @Test
    public void testToString() {
        assertEquals("Test123", characterReader.toString());
    }
}