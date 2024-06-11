package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    // Add more test cases for other methods here...

    @Test
    public void testRangeEquals() {
        char[] input = {'T', 'e', 's', 't'};
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.rangeEquals(0, 4, "Test"));
    }

    @Test
    public void testRangeEqualsDifferentLength() {
        char[] input = {'T', 'e', 's', 't'};
        CharacterReader reader = new CharacterReader("Test");
        assertFalse(reader.rangeEquals(0, 3, "Tes"));
    }
}