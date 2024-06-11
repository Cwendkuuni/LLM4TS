package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(5, reader.length);
    }

    @Test
    public void testEmpty_methods() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
        assertFalse(reader.matches('a'));
        assertFalse(reader.matches("ab"));
        assertFalse(reader.matchesAny(new char[]{'a'}));
        assertNull(reader.consume());
        assertNull(reader.consumeAsString());
        assertNull(reader.consumeTo('a'));
        assertNull(reader.consumeTo("ab"));
        assertNull(reader.consumeToAny(new char[]{'a'}));
        assertNull(reader.consumeToEnd());
        assertNull(reader.consumeLetterSequence());
        assertNull(reader.consumeHexSequence());
        assertNull(reader.consumeDigitSequence());
        assertEquals("", reader.toString());
    }

    @Test
    public void testNonEmpty_methods() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);

        // pos()
        assertEquals(0, reader.pos());

        // isEmpty()
        assertFalse(reader.isEmpty());

        // current()
        assertEquals('a', reader.current());

        // consume()
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());

        // unconsume()
        reader.unconsume();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());

        // advance()
        reader.advance();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());

        // mark() and rewindToMark()
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());

        // consumeAsString()
        assertEquals("bc", reader.consumeAsString());
        assertEquals(4, reader.pos());

        // consumeTo()
        assertEquals("cd", reader.consumeTo('e'));
        assertEquals("ef", reader.consumeToEnd());
        assertEquals("ef", reader.toString());

        // consumeTo() with specific string
        assertEquals("cd", reader.consumeTo("cd"));
        assertEquals("ef", reader.consumeTo("ef"));

        // consumeToAny()
        assertEquals("bc", reader.consumeToAny(new char[]{'b', 'c'}));
        assertEquals("ef", reader.consumeToAny(new char[]{'a'}));
        assertEquals("ef", reader.toString());

        // consumeToEnd()
        assertEquals("ef", reader.consumeToEnd());
        assertEquals("", reader.toString());

        // consumeLetterSequence()
        assertEquals("ef", reader.consumeLetterSequence());
        assertEquals("", reader.toString());

        // consumeHexSequence()
        reader.advance(); // pass over 'e'
        assertEquals("f", reader.consumeHexSequence());
        assertEquals("", reader.toString());

        // consumeDigitSequence()
        reader.advance(); // pass over 'f'
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("", reader.toString());

        // matches()
        assertTrue(reader.matches('f'));
        assertFalse(reader.matches('a'));

        // matches() with specific string
        assertFalse(reader.matches("ab"));
        assertTrue(reader.matches("f"));

        // matchesIgnoreCase()
        assertTrue(reader.matchesIgnoreCase("f"));
        assertFalse(reader.matchesIgnoreCase("Ab"));

        // matchesAny()
        assertTrue(reader.matchesAny(new char[]{'f'}));
        assertFalse(reader.matchesAny(new char[]{'a'}));

        // matchesLetter()
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter()); // after advancing once

        // matchesDigit()
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit()); // after advancing once

        // matchConsume()
        assertEquals("ef", reader.matchConsume("ef"));
        assertEquals("", reader.toString());

        // matchConsumeIgnoreCase()
        assertEquals("ef", reader.matchConsumeIgnoreCase("ef"));
        assertEquals("", reader.toString());

        // containsIgnoreCase()
        assertFalse(reader.containsIgnoreCase("Ab"));
        assertTrue(reader.containsIgnoreCase("ef"));
    }
}