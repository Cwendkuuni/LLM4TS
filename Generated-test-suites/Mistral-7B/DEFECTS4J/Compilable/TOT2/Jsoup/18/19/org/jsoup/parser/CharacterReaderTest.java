package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderBasic() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        // ...
    }

    // Add test cases for edge cases and exception handling scenarios here

    @Test
    public void testCharacterReaderEnd() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("", reader.consumeToEnd());
        assertEquals(false, reader.isEmpty());
    }

    // Add more test cases here
}