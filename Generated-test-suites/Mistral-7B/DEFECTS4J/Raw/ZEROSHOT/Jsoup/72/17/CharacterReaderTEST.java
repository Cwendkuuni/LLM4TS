package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(input));
    }

    @After
    public void tearDown() {
        if (characterReader != null) {
            try {
                characterReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCharacterReaderEmpty() {
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.pos());
    }

    @Test
    public void testCharacterReaderNonEmpty() {
        characterReader.advance();
        assertFalse(characterReader.isEmpty());
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testCurrentCharacter() {
        characterReader.advance();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeCharacter() {
        characterReader.advance();
        characterReader.consume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsumeCharacterEOF() {
        characterReader.consume();
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeString() {
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeTo(' ');
        assertEquals("is a test for CharacterReader class.", consumed);
    }

    @Test
    public void testConsumeStringEmpty() {
        String consumed = characterReader.consumeTo(' ');
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeStringEnd() {
        characterReader.consumeToEnd();
        assertEquals("This is a test for CharacterReader class.", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("is a test for CharacterReader class.", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String consumed = characterReader.consumeLetterThenDigitSequence();
        assertEquals("is a test for CharacterReader class.1", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeHexSequence();
        assertEquals("a test for CharacterReader class.", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeDigitSequence();
        assertEquals("test for CharacterReader class.123", consumed);
    }

    @Test
    public void testMatchesCharacter() {
        characterReader.advance();
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('S'));
    }

    @Test
    public void testMatchesString() {
        characterReader.advance();
        characterReader.advance();
        assertTrue(characterReader.matches("is"));
        assertFalse(characterReader.matches("is not"));
        characterReader.consume();
        assertFalse(characterReader.matches("is"));
    }

    @Test
    public void testMatchesIgnoreCaseString() {
        characterReader.advance();
        characterReader.advance();
        assertTrue(characterReader.matchesIgnoreCase("is"));
        assertTrue(characterReader.matchesIgnoreCase("Is"));
        assertFalse(characterReader.matchesIgnoreCase("is not"));
        characterReader.consume();
        assertFalse(characterReader.matchesIgnoreCase("is"));
    }

    @Test
    public void testMatchesAnyCharacter() {
        characterReader.advance();
        assertTrue(characterReader.matchesAny('T', 'S'));
        assertFalse(characterReader.matchesAny('X', 'S'));
    }

    @Test
    public void testMatchesAnySortedCharacter() {
        characterReader.advance();
        assertTrue(characterReader.matchesAnySorted(new char[]{ 'S', 'T' }));
        assertFalse(characterReader.matchesAnySorted(new char[]{ 'X', 'S' }));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMarkAndRewind() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());
    }
}