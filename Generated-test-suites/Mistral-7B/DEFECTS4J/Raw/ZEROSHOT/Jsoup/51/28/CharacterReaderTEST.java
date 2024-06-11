package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "abcdefghijklmnopqrst";
        CharacterReader reader = new CharacterReader(input);

        // test basic methods
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));

        // test consume methods
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals("defghijklmnopqrst", reader.consumeToEnd());
        assertEquals("def", reader.consumeToAny(new char[]{'d', 'e', 'f'}));
        assertEquals("def", reader.consumeToAnySorted(new char[]{'d', 'e', 'f'}));
        assertEquals("bcdef", reader.consumeData());
        assertEquals("title", reader.consumeTagName());

        // test matching methods
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
        assertFalse(reader.matches("CD"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesLetter('5'));

        // test consumeLetterSequence and consumeLetterThenDigitSequence
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("ab123", reader.consumeLetterThenDigitSequence());

        // test mark and rewindToMark
        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        reader.rewindToMark();
        assertEquals(1, reader.pos());

        // test isEmpty
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }
}