package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        int expectedPos = 0;

        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        boolean expectedEmpty = true;

        assertEquals(expectedEmpty, reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        char expectedCurrent = 't';

        assertEquals(expectedCurrent, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char expectedConsumed = 't';
        reader.consume();

        assertEquals(expectedConsumed, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        char expectedUnconsumed = 't';
        reader.consume();
        reader.unconsume();

        assertEquals(expectedUnconsumed, reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        char expectedCurrent = 'e';
        reader.advance();

        assertEquals(expectedCurrent, reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        int expectedMark = 0;
        reader.mark();

        assertEquals(expectedMark, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        int expectedPos = 0;
        reader.mark();
        reader.rewindToMark();

        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "t";

        assertEquals(expectedConsumed, reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("test");
        int expectedOffset = 2;

        assertEquals(expectedOffset, reader.nextIndexOf('s'));
    }

    @Test
    public void testNextIndexOf_Sequence() {
        CharacterReader reader = new CharacterReader("test");
        int expectedOffset = 1;

        assertEquals(expectedOffset, reader.nextIndexOf("es"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "t";

        assertEquals(expectedConsumed, reader.consumeTo('e'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        String expectedConsumed = "test";

        assertEquals(expectedConsumed, reader.consumeToEnd());
    }

}