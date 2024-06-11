
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test the pos method returns the correct position
        // Setup
        CharacterReader reader = new CharacterReader("Test");

        // Execute
        int position = reader.pos();

        // Verify
        assertEquals(0, position);
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty method returns true for an empty reader
        // Setup
        CharacterReader reader = new CharacterReader("");

        // Execute
        boolean empty = reader.isEmpty();

        // Verify
        assertTrue(empty);
    }

    @Test
    public void testCurrent() {
        // Test the current method returns the correct character
        // Setup
        CharacterReader reader = new CharacterReader("Test");

        // Execute
        char currentChar = reader.current();

        // Verify
        assertEquals('T', currentChar);
    }

    @Test
    public void testConsume() {
        // Test the consume method consumes and returns the correct character
        // Setup
        CharacterReader reader = new CharacterReader("Test");

        // Execute
        char consumedChar = reader.consume();

        // Verify
        assertEquals('T', consumedChar);
        assertEquals('e', reader.current()); // Verify the current character after consume
    }

    @Test
    public void testUnconsume() {
        // Test the unconsume method works correctly
        // Setup
        CharacterReader reader = new CharacterReader("Test");
        char firstChar = reader.consume();

        // Execute
        reader.unconsume();

        // Verify
        assertEquals(firstChar, reader.current());
    }

    @Test
    public void testAdvance() {
        // Test the advance method moves the cursor by one
        // Setup
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();

        // Execute
        reader.advance();

        // Verify
        assertEquals('s', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        // Test the mark and rewindToMark methods
        // Setup
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.mark();

        // Execute
        reader.consume(); // consuming more characters
        reader.rewindToMark();

        // Verify
        assertEquals('e', reader.current());
    }

    // More test cases for other methods

}
