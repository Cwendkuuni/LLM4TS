package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());

        char c = reader.current();
        assertNull(c);

        reader.advance();
        c = reader.current();
        assertEquals('a', c);
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());

        reader.unconsume();
        assertEquals(1, reader.pos());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());

        String str = reader.consumeTo('c');
        assertEquals("ab", str);
        assertEquals(3, reader.pos());

        str = reader.consumeToEnd();
        assertEquals("abcdefg", str);
        assertEquals(7, reader.pos());

        reader.consumeLetterSequence();
        assertEquals("defg", reader.consumeToEnd());

        assertTrue(reader.matches('b'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));

        reader.mark();
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("def"));
        reader.rewindToMark();
        assertFalse(reader.matchesIgnoreCase("def"));

        reader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("bcdefg", reader.consumeToEnd());

        reader.consumeToAnySorted(new char[]{'b', 'a'});
        assertEquals("bcdefg", reader.consumeToEnd());

        reader.consumeData();
        assertEquals("abcdefg", reader.consumeToEnd());

        reader.consumeTagName();
        assertEquals("title", reader.consumeTagName());
        assertFalse(reader.matchesTagName("style"));

        reader.consumeToEnd();
        assertTrue(reader.isEmpty());

        reader.close();
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"), 1);
        reader.bufferUp();
        reader.reader.close();
    }
}