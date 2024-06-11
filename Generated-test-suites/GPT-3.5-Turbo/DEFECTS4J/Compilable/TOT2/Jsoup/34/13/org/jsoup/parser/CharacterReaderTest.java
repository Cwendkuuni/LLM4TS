package org.jsoup.parser;
import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        for (int i = 0; i < 12; i++) {
            reader.consume();
        }
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    // Add more test cases for the remaining methods...

}