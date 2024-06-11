package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals(0, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());
        // other test cases for remaining methods
    }
}