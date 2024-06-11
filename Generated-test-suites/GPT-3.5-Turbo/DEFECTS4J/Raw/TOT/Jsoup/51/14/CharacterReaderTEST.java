package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
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
        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
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
    public void testMarkAndRewindToMark() {
        characterReader.consume();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
        assertEquals("e", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        assertEquals(1, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(1, characterReader.nextIndexOf("est"));
        assertEquals(-1, characterReader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo_char() {
        assertEquals("T", characterReader.consumeTo('e'));
        assertEquals("e", characterReader.consumeTo('s'));
    }

    @Test
    public void testConsumeTo_String() {
        assertEquals("T", characterReader.consumeTo("es"));
        assertEquals("est", characterReader.consumeTo("i"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("T", characterReader.consumeToAny('e', 's'));
        assertEquals("es", characterReader.consumeToAny('t'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("T", characterReader.consumeToAnySorted('e', 's'));
        assertEquals("es", characterReader.consumeToAnySorted('t'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("T", characterReader.consumeData());
        assertEquals("est S", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("T", characterReader.consumeTagName());
        assertEquals("est", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("T", characterReader.consumeLetterSequence());
        assertEquals("est", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", characterReader.consumeLetterThenDigitSequence());
        assertEquals("String", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Test", characterReader.consumeHexSequence());
        assertEquals("String", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.unconsume();
        assertEquals("Test", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('e'));
    }

    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("Test"));
        assertFalse(characterReader.matches("String"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("TEST"));
        assertFalse(characterReader.matchesIgnoreCase("java"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'e'));
        assertFalse(characterReader.matchesAny('s'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 'e'));
        assertFalse(characterReader.matchesAnySorted('s'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.consumeToEnd();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Test"));
        assertFalse(characterReader.matchConsume("String"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("TEST"));
        assertFalse(characterReader.matchConsumeIgnoreCase("java"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("EST"));
        assertFalse(characterReader.containsIgnoreCase("java"));
    }

    @Test
    public void testToString() {
        assertEquals("Test String", characterReader.toString());
    }
}