package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test when pos is in the beginning
        CharacterReader reader1 = new CharacterReader("example");
        assertEquals(0, reader1.pos());

        // Test when pos is in the middle
        reader1.advance();
        reader1.advance();
        assertEquals(2, reader1.pos());

        // Test when pos is at the end
        while (!reader1.isEmpty()) {
            reader1.advance();
        }
        assertEquals(7, reader1.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test when the input is not empty
        CharacterReader reader1 = new CharacterReader("example");
        assertFalse(reader1.isEmpty());

        // Test when the input is empty
        CharacterReader reader2 = new CharacterReader("");
        assertTrue(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test when pos is in the beginning
        CharacterReader reader1 = new CharacterReader("example");
        assertEquals('e', reader1.current());

        // Test when pos is in the middle
        reader1.advance();
        reader1.advance();
        assertEquals('a', reader1.current());

        // Test when pos is at the end
        while (!reader1.isEmpty()) {
            reader1.advance();
        }
        assertEquals(CharacterReader.EOF, reader1.current());
    }

    // Implement similar test cases for other methods...

}