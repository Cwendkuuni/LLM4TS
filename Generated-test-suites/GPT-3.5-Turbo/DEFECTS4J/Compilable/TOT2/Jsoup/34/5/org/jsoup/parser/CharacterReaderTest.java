package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test string";
        characterReader = new CharacterReader(input);
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
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
    public void testMarkRewindToMark() {
        characterReader.advance(); // Moves pos to 1
        characterReader.mark(); // Marks position at 1
        characterReader.advance(); // Moves pos to 2
        assertEquals(2, characterReader.pos());
        characterReader.rewindToMark(); // Rewinds back to marked position
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(2, characterReader.nextIndexOf('i'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(5, characterReader.nextIndexOf("is a"));
    }

    // More test cases for other methods can be added here

}