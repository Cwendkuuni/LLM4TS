package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(0, reader.pos());
        assertEquals('h', reader.current());
        assertEquals('h', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('e', reader.current());

        reader.unconsume();
        assertEquals(1, reader.pos());
        assertEquals('h', reader.current());

        reader.advance();
        assertEquals('e', reader.current());

        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());

        assertEquals("ello world", reader.consumeAsString());
        assertEquals("ello", reader.consumeTo(' '));
        assertEquals("ello world", reader.consumeToEnd());
        assertEquals("ello", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeDigitSequence());

        reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMark() {
        CharacterReader reader = new CharacterReader("--foo--bar");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals("--foo", reader.consumeTo('--'));
        assertEquals("bar", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderWithInvalidInput() {
        try {
            new CharacterReader(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        CharacterReader reader = new CharacterReader("");
        reader.advance();
        reader.consume();
        reader.unconsume();
        reader.rewindToMark();
        reader.consume();
        reader.consumeToEnd();
        reader.consumeTo('a');
        reader.consumeToAny('b', 'c');
        reader.consumeToAny();
        reader.consumeLetterSequence();
        reader.consumeHexSequence();
        reader.consumeDigitSequence();
        reader.matches('a');
        reader.matches("ab");
        reader.matchesIgnoreCase("Ab");
        reader.matchesAny('a', 'b');
        reader.matchesLetter();
        reader.matchesDigit();
        reader.matchConsume("ab");
        reader.matchConsumeIgnoreCase("Ab");
        reader.containsIgnoreCase("ab");
        reader.containsIgnoreCase("Ab");
        reader.toString();
    }
}