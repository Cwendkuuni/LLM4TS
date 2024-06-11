package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.input.toString());
        assertEquals(11, reader.length);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.pos = 5;
        assertEquals(5, reader.pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.pos = 0;
        assertEquals('H', reader.current());

        reader.pos = 1;
        assertEquals('e', reader.current());

        reader.pos = 11;
        assertEquals('\u0000', reader.current()); // EOF
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.pos = 0;

        assertEquals('H', reader.current());
        assertEquals('H', reader.consume());
        assertEquals('e', reader.current());

        reader.pos = 10;
        assertEquals('\u0000', reader.consume()); // EOF
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.pos = 1;
        reader.unconsume();
        assertEquals('H', reader.current());

        reader.pos = 1;
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());

        reader.pos = 0;
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.pos = 5;
        reader.advance();
        assertEquals(6, reader.pos);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.pos = 5;
        reader.mark();

        reader.pos = 9;
        assertEquals(5, reader.mark);

        reader.rewindToMark();
        assertEquals(5, reader.pos);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("H", reader.consumeAsString());
        assertEquals("ello W", reader.consumeAsString());
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(6, reader.nextIndexOf(' '));
        assertEquals(-1, reader.nextIndexOf('Z'));
        reader.pos = 1;
        assertEquals(5, reader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(6, reader.nextIndexOf(" "));
        assertEquals(-1, reader.nextIndexOf("ZZ"));
        reader.pos = 1;
        assertEquals(5, reader.nextIndexOf("ll"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.consumeTo('d'));
        assertEquals("World", reader.consumeTo("ld"));
        assertEquals("llo", reader.consumeTo("ll"));
        assertEquals("", reader.consumeTo("z"));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.consumeToAny(new char[]{'o', 'r', 'd'}));
        assertEquals("Hello", reader.consumeToAny(new char[]{'H', 'e'}));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.consumeToAnySorted(new char[]{'o', 'r', 'd'}));
        assertEquals("Hello", reader.consumeToAnySorted(new char[]{'H', 'e'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null>");
        assertEquals("&<null>", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<html><head><title>Title</title></head>");
        assertEquals("<title>", reader.consumeTagName());
        assertEquals("<head>", reader.consumeTagName());
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("&<null>");
        assertEquals("&<null>", reader.toString());
    }

    @Test(expected = AssertionError.class)
    public void testConsumeToEndWhenAtEOF() {
        CharacterReader reader = new CharacterReader("");
        reader.consumeToEnd();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeOffset() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume(-1, "consume");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }
}