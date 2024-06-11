package org.jsoup.parser;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("TestString");
        assert(reader.pos() == 0);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assert(reader.isEmpty());

        reader = new CharacterReader("TestString");
        assert(!reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestString");
        assert(reader.current() == 'T');
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestString");
        assert(reader.consume() == 'T');
        assert(reader.consume() == 'e');
    }

    // Add more test cases for other public methods

}