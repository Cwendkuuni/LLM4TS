
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertFalse(reader.isEmpty());
        assertEquals('H', reader.current());
    }

    // ... (paste the edge cases and exception handling test cases here)
}
