package org.jsoup.parser;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.Reader;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        Reader reader = new StringReader("Hello World!");
        characterReader = new CharacterReader(reader);
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testMarkAndRewind() {
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Hello", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello", characterReader.consumeToAny(' ', '!'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("Hello", characterReader.consumeData());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Hell"));
        assertEquals('o', characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("WORLD"));
    }

}