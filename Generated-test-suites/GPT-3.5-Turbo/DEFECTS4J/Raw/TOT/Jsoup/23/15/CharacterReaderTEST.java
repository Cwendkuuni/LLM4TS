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
        char consumedChar = characterReader.consume();
        assertEquals('T', consumedChar);
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.consume();
        assertEquals(2, characterReader.pos());
        characterReader.unconsume();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String consumedString = characterReader.consumeAsString();
        assertEquals("T", consumedString);
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeTo_characterFound() {
        String consumedTo = characterReader.consumeTo('i');
        assertEquals("Th", consumedTo);
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testConsumeTo_characterNotFound() {
        String consumedTo = characterReader.consumeTo('x');
        assertEquals("This is a test string.", consumedTo);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeTo_stringFound() {
        String consumedTo = characterReader.consumeTo("is");
        assertEquals("Th", consumedTo);
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testConsumeTo_stringNotFound() {
        String consumedTo = characterReader.consumeTo("xx");
        assertEquals("This is a test string.", consumedTo);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToAny_characterFound() {
        String consumedToAny = characterReader.consumeToAny('s', 't', 'r');
        assertEquals("Thi", consumedToAny);
        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testConsumeToAny_characterNotFound() {
        String consumedToAny = characterReader.consumeToAny('x', 'y', 'z');
        assertEquals("This is a test string.", consumedToAny);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        String consumedToEnd = characterReader.consumeToEnd();
        assertEquals("This is a test string.", consumedToEnd);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        characterReader.advance();
        String consumedLetterSequence = characterReader.consumeLetterSequence();
        assertEquals("his", consumedLetterSequence);
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader.consume(); // Skip the 'T'
        characterReader.advance(); // Skip the 'h'
        String consumedHexSequence = characterReader.consumeHexSequence();
        assertEquals("69", consumedHexSequence);
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        String consumedDigitSequence = characterReader.consumeDigitSequence();
        assertEquals("", consumedDigitSequence);
        assertEquals(0, characterReader.pos());

        characterReader.consume(); // Skip the 'T'
        String consumedDigitSequence2 = characterReader.consumeDigitSequence();
        assertEquals("", consumedDigitSequence2);
        assertEquals(1, characterReader.pos());

        String consumedDigitSequence3 = characterReader.consumeDigitSequence();
        assertEquals("", consumedDigitSequence3);
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMatches_characterFound() {
        assertTrue(characterReader.matches('T'));
    }

    @Test
    public void testMatches_characterNotFound() {
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatches_stringFound() {
        assertTrue(characterReader.matches("This"));
    }

    @Test
    public void testMatches_stringNotFound() {
        assertFalse(characterReader.matches("NotThis"));
    }

    @Test
    public void testMatchesIgnoreCase_stringFound() {
        assertTrue(characterReader.matchesIgnoreCase("THIS"));
    }

    @Test
    public void testMatchesIgnoreCase_stringNotFound() {
        assertFalse(characterReader.matchesIgnoreCase("NOTTHIS"));
    }

    @Test
    public void testMatchesAny_characterFound() {
        assertTrue(characterReader.matchesAny('s', 't', 'r'));
    }

    @Test
    public void testMatchesAny_characterNotFound() {
        assertFalse(characterReader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter_characterIsLetter() {
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_characterIsNotLetter() {
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit_characterIsDigit() {
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_characterIsNotDigit() {
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume_stringMatches() {
        assertTrue(characterReader.matchConsume("Th"));
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testMatchConsume_stringDoesNotMatch() {
        assertFalse(characterReader.matchConsume("Ha"));
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_stringMatches() {
        assertTrue(characterReader.matchConsumeIgnoreCase("THIS"));
        assertEquals(4, characterReader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_stringDoesNotMatch() {
        assertFalse(characterReader.matchConsumeIgnoreCase("THAT"));
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase_sequenceFound() {
        assertTrue(characterReader.containsIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase_sequenceNotFound() {
        assertFalse(characterReader.containsIgnoreCase("xxx"));
    }

    @Test
    public void testToString() {
        assertEquals("This is a test string.", characterReader.toString());
    }
}