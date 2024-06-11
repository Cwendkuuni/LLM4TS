package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World!");
        assertEquals(false, reader.isEmpty());

        CharacterReader readerEmpty = new CharacterReader("");
        assertEquals(true, readerEmpty.isEmpty());
    }
}