package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    // Add more test cases for other methods as required

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("Cache Test");
        String cachedString = reader.cacheString(0, 5);
        assertEquals("Cache", cachedString);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("Range Test");
        boolean result = reader.rangeEquals(0, 5, "Range");
        assertTrue(result);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Next Index Test");
        int result = reader.nextIndexOf('I');
        assertEquals(5, result);
    }

    // Add more test cases for other methods as required

}