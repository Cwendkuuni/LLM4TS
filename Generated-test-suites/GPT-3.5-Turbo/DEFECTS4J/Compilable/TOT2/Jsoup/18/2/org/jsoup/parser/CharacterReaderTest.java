package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos method
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty method
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader("test");
        assertFalse(characterReader.isEmpty());
    }

    // Add more test methods for each public method in the CharacterReader class

    @Test
    public void testEOF() {
        // Test the EOF constant
        assertEquals((char) -1, CharacterReader.EOF);
    }

    @Test
    public void testUnconsume() {
        // Test the unconsume method
        CharacterReader characterReader = new CharacterReader("test");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        // Test the consumeAsString method
        CharacterReader characterReader = new CharacterReader("test");
        assertEquals("t", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeToAny() {
        // Test the consumeToAny method
        CharacterReader characterReader = new CharacterReader("abcde");
        assertEquals("ab", characterReader.consumeToAny('c', 'd'));
    }

    // Add more test cases for other methods

}