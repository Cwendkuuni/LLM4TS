package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {
    @Test
    public void testConstructorAndIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
    }
}