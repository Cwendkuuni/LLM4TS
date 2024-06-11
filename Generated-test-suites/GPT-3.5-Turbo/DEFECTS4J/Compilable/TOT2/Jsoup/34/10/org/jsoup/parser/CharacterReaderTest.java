package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Testing 123");
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        for (int i = 0; i < 11; i++) {
            characterReader.consume();
        }
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        char beforeUnconsume = characterReader.current();
        characterReader.unconsume();
        assertEquals(beforeUnconsume, characterReader.current());
    }

    // Add more test cases for the remaining methods...

}