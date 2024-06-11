package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;

public class CharacterReaderTest {

    private static final int TEST_STRING_LENGTH = 100;

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        reader.advance();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('b', reader.current());
        reader.mark();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.consume();
        assertEquals('b', reader.current());
        reader.advance();
        assertEquals('c', reader.current());
        reader.bufferUp();
        reader.consumeTo('a');
        String result = reader.consumeToEnd();
        assertEquals("bcdefghijklmnopqrstuvwxyz", result);
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.matchConsume("abc");
        reader.matchConsumeIgnoreCase("AbC");
        reader.containsIgnoreCase("ABC");
        reader.close();
    }

    @Test
    public void testCharacterReaderWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz")) {
            @Override
            void bufferUp() throws IOException {
                throw new IOException("IO exception");
            }
        };
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        try {
            reader.advance();
        } catch (IOException e) {
            // expected
        }
        assertTrue(reader.isEmpty());
        try {
            reader.consume();
        } catch (IOException e) {
            // expected
        }
        assertTrue(reader.isEmpty());
        try {
            reader.matchConsume("abc");
        } catch (IOException e) {
            // expected
        }
        reader.close();
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderWithUncheckedIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz")) {
            @Override
            void bufferUp() throws IOException {
                throw new UncheckedIOException("IO exception");
            }
        };
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        reader.advance();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.unconsume();
        reader.mark();
        reader.rewindToMark();
        reader.advance();
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.bufferUp();
        reader.consumeTo('a');
        String result = reader.consumeToEnd();
        assertEquals("bcdefghijklmnopqrstuvwxyz", result);
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.matchConsume("abc");
        reader.matchConsumeIgnoreCase("AbC");
        reader.containsIgnoreCase("ABC");
        reader.close();
    }

    @Test
    public void testCharacterReaderWithLimit() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"), TEST_STRING_LENGTH);
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        reader.advance();
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.bufferUp();
        reader.consumeTo('z');
        String result = reader.consumeToEnd();
        assertEquals("ab", result);
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.matchConsume("ab");
        reader.matchConsumeIgnoreCase("Ab");
        reader.containsIgnoreCase("AB");
        reader.close();
    }
}