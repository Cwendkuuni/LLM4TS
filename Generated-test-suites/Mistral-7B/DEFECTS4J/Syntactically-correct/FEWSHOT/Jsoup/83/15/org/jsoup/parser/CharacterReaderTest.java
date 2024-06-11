package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello, World!"));
        assertEquals(0, reader.pos());
        assertEquals(Character.EOF, reader.current());

        reader.advance();
        assertEquals(1, reader.pos());
        assertEquals('H', reader.current());

        reader.consume();
        assertEquals(2, reader.pos());
        assertEquals('H', reader.current());

        reader.unconsume();
        assertEquals(2, reader.pos());
        assertEquals('H', reader.current());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());

        reader.advance();
        assertEquals('E', reader.current());

        reader.consumeTo('l');
        assertEquals(5, reader.pos());
        assertEquals("Hello", reader.consumed());

        reader.consumeTo('d');
        assertEquals(6, reader.pos());
        assertEquals("Hello", reader.consumed());

        reader.consumeTo('!');
        assertEquals(11, reader.pos());
        assertEquals("Hello, World!", reader.consumed());

        reader.consumeToEnd();
        assertEquals(11, reader.pos());
        assertEquals("", reader.consumed());

        reader.consumeLetterSequence();
        assertEquals("lllll", reader.consumed());

        reader.consumeLetterThenDigitSequence();
        assertEquals("Word1", reader.consumed());

        reader.consumeHexSequence();
        assertEquals("Hello%20World!", reader.consumed());

        reader.consumeDigitSequence();
        assertEquals("123", reader.consumed());

        reader.matches('H');
        reader.advance();
        reader.matches('e');
        reader.advance();
        reader.matches('l');
        reader.advance();
        reader.matches('l');
        reader.advance();
        reader.matches('o');

        reader.matchesIgnoreCase("ELLO");
        reader.advance();
        reader.matchesIgnoreCase("word");
        reader.advance();

        reader.matchesAny('H', 'e');
        reader.advance();
        reader.matchesAny('e', 'l');
        reader.advance();
        reader.matchesAny('l', 'l');
        reader.advance();
        reader.matchesAny('o');

        reader.matchesAnySorted('l', 'o', 'h');
        reader.advance();
        reader.matchesAnySorted('o', 'l', 'h');

        reader.matchesLetter();
        reader.advance();
        reader.matchesLetter();
        reader.advance();
        reader.matchesLetter();
        reader.advance();
        reader.matchesLetter();
        reader.advance();
        reader.matchesLetter();
        reader.advance();

        reader.matchesDigit();
        reader.advance();
        reader.matchesDigit();
        reader.advance();

        reader.matchConsume("ello");
        assertEquals(11, reader.pos());
        assertEquals("World!", reader.consumed());

        reader.matchConsumeIgnoreCase("ELLO");
        assertEquals(11, reader.pos());
        assertEquals("World!", reader.consumed());

        reader.containsIgnoreCase("ello");
        reader.advance();
        reader.containsIgnoreCase("world");
        reader.advance();

        reader.close();
    }

    @Test
    public void testCharacterReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello, World!")) {
            @Override
            public void bufferUp() {
                throw new IOException("test IOException");
            }
        };

        assertEquals(0, reader.pos());
        assertEquals(Character.EOF, reader.current());

        reader.advance();
        assertEquals(1, reader.pos());
        assertEquals(Character.EOF, reader.current());

        try {
            reader.consume();
        } catch (IOException e) {
            // expected
        }

        assertEquals(1, reader.pos());
        assertEquals(Character.EOF, reader.current());

        reader.mark();
        try {
            reader.rewindToMark();
        } catch (IOException e) {
            // expected
        }

        assertEquals(1, reader.pos());
        assertEquals(Character.EOF, reader.current());

        reader.close();
    }
}