package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

/**
 * CharacterReaderTest class.
 */
public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() throws IOException {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));
    }

    @After
    public void tearDown() {
        if (characterReader != null) {
            characterReader.close();
        }
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));
        assertEquals(testInput.length(), characterReader.pos());
    }

    /**
     * Test isEmpty method.
     */
    @Test
    public void testIsEmpty() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));
        assertFalse(characterReader.isEmpty());

        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    /**
     * Test current method.
     */
    @Test
    public void testCurrent() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        char expected = testInput.charAt(0);
        assertEquals(expected, characterReader.current());
        assertEquals(expected, characterReader.consume());
    }

    /**
     * Test consume method.
     */
    @Test
    public void testConsume() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        characterReader.consume();
        assertEquals(testInput.charAt(1), characterReader.current());
        assertEquals(testInput.charAt(1), characterReader.consume());
    }

    /**
     * Test unconsume method.
     */
    @Test
    public void testUnconsume() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        characterReader.consume();
        characterReader.unconsume();
        assertEquals(testInput.charAt(0), characterReader.current());
    }

    /**
     * Test advance method.
     */
    @Test
    public void testAdvance() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        characterReader.advance();
        assertEquals(testInput.charAt(1), characterReader.current());
        assertEquals(testInput.charAt(1), characterReader.consume());
    }

    /**
     * Test mark method.
     */
    @Test
    public void testMark() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        int mark = characterReader.pos();
        characterReader.mark();
        characterReader.consume();

        characterReader.rewindToMark();
        assertEquals(mark, characterReader.pos());
    }

    /**
     * Test rewindToMark method.
     */
    @Test
    public void testRewindToMark() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        int mark = characterReader.pos();
        characterReader.consume();
        characterReader.mark();

        characterReader.rewindToMark();
        assertEquals(mark, characterReader.pos());
    }

    /**
     * Test nextIndexOf method with single character.
     */
    @Test
    public void testNextIndexOfSingleChar() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        int index = characterReader.nextIndexOf('s');
        assertEquals(5, index);
    }

    /**
     * Test nextIndexOf method with multiple characters.
     */
    @Test
    public void testNextIndexOfMultipleChars() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        int index = characterReader.nextIndexOf("is");
        assertEquals(3, index);
    }

    /**
     * Test consumeTo method with single character.
     */
    @Test
    public void testConsumeToSingleChar() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeTo('s');
        assertEquals("his", result);
    }

    /**
     * Test consumeTo method with multiple characters.
     */
    @Test
    public void testConsumeToMultipleChars() {    
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeTo("is");
        assertEquals("his", result);
    }

    /**
     * Test consumeToAny method.
     */
    @Test
    public void testConsumeToAny() {
        String testInput = "This is a test for CharacterReader class.<html>";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeToAny(',');
        assertEquals("This is a test for CharacterReader class.<", result);
    }

    /**
     * Test consumeToAnySorted method.
     */
    @Test
    public void testConsumeToAnySorted() {
        String testInput = "This is a test for CharacterReader class.<html>";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeToAnySorted(new char[]{'<', '.'});
        assertEquals("This is a test for CharacterReader class.", result);
    }

    /**
     * Test consumeData method.
     */
    @Test
    public void testConsumeData() {
        String testInput = "This is a test for CharacterReader class.<html>";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeData();
        assertEquals("This is a test for CharacterReader class.<html>", testInput);
    }

    /**
     * Test consumeTagName method.
     */
    @Test
    public void testConsumeTagName() {
        String testInput = "<html><head><title>Test</title></head><body>";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeTagName();
        assertEquals("title", result);
    }

    /**
     * Test consumeToEnd method.
     */
    @Test
    public void testConsumeToEnd() {
        String testInput = "This is a test for CharacterReader class.<html>";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeToEnd();
        assertEquals("This is a test for CharacterReader class.<html>", testInput);
    }

    /**
     * Test consumeLetterSequence method.
     */
    @Test
    public void testConsumeLetterSequence() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeLetterSequence();
        assertEquals("is", result);
    }

    /**
     * Test consumeLetterThenDigitSequence method.
     */
    @Test
    public void testConsumeLetterThenDigitSequence() {
        String testInput = "This is a test 123 for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("is123", result);
    }

    /**
     * Test consumeHexSequence method.
     */
    @Test
    public void testConsumeHexSequence() {
        String testInput = "This is a test for CharacterReader class.#ff0000";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeHexSequence();
        assertEquals("#ff0000", result);
    }

    /**
     * Test consumeDigitSequence method.
     */
    @Test
    public void testConsumeDigitSequence() {
        String testInput = "This is a test for CharacterReader class.1234567890";
        characterReader = new CharacterReader(new StringReader(testInput));

        String result = characterReader.consumeDigitSequence();
        assertEquals("1234567890", result);
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('f'));
    }

    /**
     * Test matchesIgnoreCase method.
     */
    @Test
    public void testMatchesIgnoreCase() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertTrue(characterReader.matchesIgnoreCase("is"));
        assertFalse(characterReader.matchesIgnoreCase("isnot"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertTrue(characterReader.matchesAny(new char[]{'t', 's'}));
        assertFalse(characterReader.matchesAny(new char[]{'z', 's'}));
    }

    /**
     * Test matchesAnySorted method.
     */
    @Test
    public void testMatchesAnySorted() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertTrue(characterReader.matchesAnySorted(new char[]{'t', 's'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'s', 't'}));
    }

    /**
     * Test matchesLetter method.
     */
    @Test
    public void testMatchesLetter() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    /**
     * Test matchesDigit method.
     */
    @Test
    public void testMatchesDigit() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesLetter());
    }

    /**
     * Test matchConsume method.
     */
    @Test
    public void testMatchConsume() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        String expected = "is";
        String result = characterReader.matchConsume("is");
        assertEquals(expected, result);
        assertEquals("for CharacterReader class.", characterReader.consumeToEnd());
    }

    /**
     * Test matchConsumeIgnoreCase method.
     */
    @Test
    public void testMatchConsumeIgnoreCase() {
        String testInput = "This is a test for CharacterReader class.";
        characterReader = new CharacterReader(new StringReader(testInput));

        String expected = "is";
        String result = characterReader.matchConsumeIgnoreCase("Is");
        assertEquals(expected, result);
        assertEquals("for CharacterReader class.", characterReader.consumeToEnd());
    }

    /**
     * Test containsIgnoreCase method.
     */
    @Test
    public void testContainsIgnoreCase() {
        String testInput = "This is a test for CharacterReader class.<html>";
        characterReader = new CharacterReader(new StringReader(testInput));

        assertTrue(characterReader.containsIgnoreCase("html"));
        assertTrue(characterReader.containsIgnoreCase("HEAD"));
        assertFalse(characterReader.containsIgnoreCase("BODY"));
    }
}