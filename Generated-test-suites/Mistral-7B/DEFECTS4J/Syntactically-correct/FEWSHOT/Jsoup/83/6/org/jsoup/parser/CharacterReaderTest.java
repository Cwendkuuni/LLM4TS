package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader cr = new CharacterReader(new StringReader("abcdef"));
        assertEquals(0, cr.pos());
        assertEquals(true, cr.isEmpty());
        cr.advance();
        assertEquals('a', cr.current());
        assertEquals(1, cr.pos());
        assertEquals(false, cr.isEmpty());
        cr.unconsume();
        assertEquals('a', cr.current());
        cr.consume();
        assertEquals('b', cr.current());
        cr.mark();
        cr.consume();
        cr.rewindToMark();
        assertEquals('b', cr.current());
        cr.consume();
        assertEquals('c', cr.current());
        cr.nextIndexOf('f');
        assertEquals(5, cr.pos());
        assertEquals('f', cr.current());
        cr.consumeTo('f');
        assertEquals("def", cr.toString());
        cr.consumeToEnd();
        assertEquals("abcdef", cr.toString());
        cr.consumeLetterSequence();
        assertEquals("def", cr.toString());
        cr.consumeLetterThenDigitSequence();
        assertEquals("def0", cr.toString());
        cr.consumeHexSequence();
        assertEquals("abcdef", cr.toString());
        cr.consumeDigitSequence();
        assertEquals("0", cr.toString());
        cr.matches('f');
        assertTrue(cr.matches("ef"));
        cr.matchesIgnoreCase("ef");
        assertTrue(cr.matchesAny('a', 'b'));
        assertTrue(cr.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(cr.matchesLetter());
        assertTrue(cr.matchesDigit());
        cr.matchConsume("ef");
        assertEquals(7, cr.pos());
        cr.matchConsumeIgnoreCase("ef");
        assertEquals(7, cr.pos());
        cr.containsIgnoreCase("ef");
        assertTrue(cr.containsIgnoreCase("EF"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader cr = new CharacterReader(new StringReader("abc"));
        cr.reader.mark(1);
        cr.reader.reset();
        cr.reader.close();
        cr.consume();
    }
}