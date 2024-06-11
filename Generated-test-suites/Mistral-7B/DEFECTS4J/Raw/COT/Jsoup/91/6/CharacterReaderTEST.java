package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals("loWorld", reader.consumeTo('l'));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(6, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals('l', reader.current());
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld")) {
            @Override
            void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        reader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('W', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('d', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();
        reader.unconsume();
        reader.unconsume();
        assertEquals("HelloWorld", reader.toString());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertEquals('W', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals(3, reader.nextIndexOf('l'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals(3, reader.nextIndexOf("ll"));
        assertEquals(-1, reader.nextIndexOf("zz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals("llo", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo('d'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("ld", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        assertEquals("ll", reader.consumeToAny(new char[]{'l'}));
        assertEquals("World", reader.consumeToAnySorted(new char[]{'l', 'W'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;HelloWorld&gt;"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertEquals("&lt;HelloWorld&gt;", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>HelloWorld</title></head><body>"));
        reader.bufferUp();
        assertEquals("title", reader.consumeTagName());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("l"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesAny(new char[]{'l', 'l'}));
        assertFalse(reader.matchesAny(new char[]{'z', 'z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesAnySorted(new char[]{'l', 'W'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'W'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsume("ld"));
        assertFalse(reader.matchConsume("zz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsumeIgnoreCase("ld"));
        assertFalse(reader.matchConsumeIgnoreCase("ZZ"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>HelloWorld</title></head><body>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/body"));
        assertFalse(reader.containsIgnoreCase("/headz"));
    }

}