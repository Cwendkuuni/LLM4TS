package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader reader = new StringReader("Test");
        CharacterReader charReader = new CharacterReader(reader);
        assertEquals(0, charReader.pos());
    }

    // Other test cases for public methods...

}