package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;

import org.junit.Test;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<null>";
        CharacterReader reader = new CharacterReader(input);

        // test basic methods
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
        assertEquals(EOF, reader.current());
        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.mark());
        reader.mark();

        // test consume methods
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals("ab", reader.consumeTo('a'));
        assertEquals("bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<null>", reader.consumeToEnd());

        // test consumeAsString methods
        assertEquals("a", reader.consumeAsString());
        assertEquals("bcdef", reader.consumeAsString(5));

        // test nextIndexOf methods
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals("efghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeTo('a').trim());
        assertEquals("efghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeTo('z').trim());

        // test consumeTo methods
        assertEquals("efghij", reader.consumeTo("efghij"));
        assertEquals("efghijklm", reader.consumeTo("efghijklm"));
        assertEquals("efghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeToAny(new char[]{'a', 'z'}));
        assertEquals("efghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<null>", reader.consumeToEnd());

        // test consumeToAnySorted methods
        assertEquals("efghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeToAnySorted(new char[]{'a', 'z'}));

        // test consumeData methods
        assertEquals("&, <, null", reader.consumeData());

        // test consumeTagName methods
        assertEquals("\t\n\r\f ", reader.consumeTagName());

        // test mark and rewindToMark methods
        reader.mark();
        reader.consume('a');
        reader.rewindToMark();
        assertEquals('a', reader.current());

        // test matches methods
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches("gh"));
        assertFalse(reader.matches("xyz"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
        assertFalse(reader.matchesAny(new char[]{'z'}));
        assertTrue(reader.matchesAnySorted(new char[]{'e', 'f'}));

        // test containsIgnoreCase methods
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("STYLE"));
        assertFalse(reader.containsIgnoreCase("script"));

        // test toString methods
        assertEquals("efghij", reader.toString());
    }
}