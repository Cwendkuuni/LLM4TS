package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Test");
        boolean result = reader.isEmpty();
        assertEquals(false, result);
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        char result = reader.current();
        assertEquals('T', result);
    }

    @Test
    public void testConsumeEdgeCase() {
        CharacterReader reader = new CharacterReader("");
        char result = reader.consume();
        assertEquals(CharacterReader.EOF, result);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        char result = reader.current();
        assertEquals('T', result);
    }

    @Test
    public void testMarkEdgeCase() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        char result = reader.current();
        assertEquals('T', result);
    }
}
