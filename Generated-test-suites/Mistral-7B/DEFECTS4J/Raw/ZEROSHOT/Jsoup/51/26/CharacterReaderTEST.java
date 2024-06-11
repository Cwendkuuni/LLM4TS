package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test instantiation.
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader reader = new CharacterReader(input);

        // Test basic methods.
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());

        // Test mark and rewind methods.
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());

        // Test consumeAsString method.
        assertEquals("a", reader.consumeAsString());
        assertEquals(2, reader.pos());
        assertEquals("b", reader.consumeAsString());
        assertEquals(4, reader.pos());
        assertEquals("cdef", reader.consumeTo('f'));
        assertEquals(8, reader.pos());

        // Test nextIndexOf method.
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(9, reader.nextIndexOf('h'));

        // Test consumeTo method.
        assertEquals("defghij", reader.consumeTo('j'));
        assertEquals(13, reader.pos());
        assertEquals("KLMNOPQRSTUVWXYZ", reader.consumeToEnd());
        assertEquals(36, reader.pos());
        assertEquals("abcdef", reader.consumeTo('f'));
        assertEquals(6, reader.pos());

        // Test consumeToAny method.
        assertEquals("eghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeToAny(Arrays.asList('e', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z').toArray(new char[0])));
        assertEquals(36, reader.pos());

        // Test consumeToAnySorted method.
        assertEquals("KLMNOPQRSTUVWXYZ", reader.consumeToAnySorted(new char[]{'K', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}));
        assertEquals(15, reader.pos());

        // Test consumeData method.
        assertEquals("&<\\u0000", reader.consumeData());
        assertEquals(18, reader.pos());

        // Test consumeTagName method.
        assertEquals("title", reader.consumeTagName());
        assertEquals(7, reader.pos());

        // Test consumeLetterSequence method.
        assertEquals("abcdef", reader.consumeLetterSequence());
        assertEquals(6, reader.pos());

        // Test consumeLetterThenDigitSequence method.
        assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
        assertEquals(13, reader.pos());

        // Test consumeHexSequence method.
        assertEquals("123456", reader.consumeHexSequence());
        assertEquals(6, reader.pos());

        // Test consumeDigitSequence method.
        assertEquals("123456", reader.consumeDigitSequence());
        assertEquals(6, reader.pos());

        // Test matches methods.
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("xyz"));
        assertFalse(reader.matches("XYZ"));
        assertFalse(reader.matches("123"));
        assertTrue(reader.matches("123456"));
        assertFalse(reader.matches("789"));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny(Arrays.asList('e', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z').toArray(new char[0])));
        assertFalse(reader.matchesAny(new char[]{'z', 'x'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());

        // Test matchConsume methods.
        assertEquals("abc", reader.matchConsume("abc"));
        assertEquals("def", reader.matchConsume("def"));
        assertEquals(21, reader.pos());

        // Test containsIgnoreCase method.
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("style"));
        assertFalse(reader.containsIgnoreCase("xyz"));

        // Test toString method.
        assertEquals("123456", reader.toString());
    }

    @Test
    public void testCharacterReaderEmptyInput() {
        // Test instantiation.
        CharacterReader reader = new CharacterReader("");

        // Test basic methods.
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.pos());
    }
}