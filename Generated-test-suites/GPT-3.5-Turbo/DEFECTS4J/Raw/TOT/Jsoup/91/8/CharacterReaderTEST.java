
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test input string.";
        characterReader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        characterReader.consume();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        consumeToEnd(characterReader);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        characterReader.consume();
        assertEquals('h', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.advance();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testUnconsumeInvalid() {
        characterReader.unconsume();
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
        assertEquals('i', characterReader.current());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        characterReader.consume();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
        assertEquals('T', characterReader.current());
    }
    
    // Other test methods for remaining methods of CharacterReader class

    private void consumeToEnd(CharacterReader reader) {
        while (!reader.isEmpty()) {
            reader.consume();
        }
    }
}

