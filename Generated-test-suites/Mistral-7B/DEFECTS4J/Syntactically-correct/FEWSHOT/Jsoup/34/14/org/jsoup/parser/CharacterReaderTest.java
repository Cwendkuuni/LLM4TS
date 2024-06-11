package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());

        reader.unconsume();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());

        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('b', reader.current());

        String consumed = reader.consumeAsString();
        assertEquals("bc", reader.toString());
        assertEquals("bc", consumed);

        int index = reader.nextIndexOf('c');
        assertEquals(3, index);

        consumed = reader.consumeTo('c');
        assertEquals("bc", reader.toString());
        assertEquals("b", consumed);

        reader.mark();
        reader.consumeToEnd();
        reader.rewindToMark();
        assertEquals("bc", reader.toString());

        reader.consumeLetterSequence();
        assertEquals("bcde", reader.toString());
        assertEquals("b", reader.consumeLetter());
        assertEquals("cd", reader.consumeLetterSequence());

        reader.consumeLetterThenDigitSequence();
        assertEquals("cd12", reader.toString());
        assertEquals('1', reader.consumeDigit());
        assertEquals("cd", reader.toString());

        reader.consumeHexSequence();
        assertEquals("cd12ef", reader.toString());
        assertEquals("cd12", reader.consumeDigitSequence());
        assertEquals("cd", reader.toString());

        reader.matchConsume("bc");
        assertEquals("cd", reader.toString());

        reader.mark();
        reader.matchConsumeIgnoreCase("BC");
        reader.rewindToMark();
        assertEquals("bc", reader.toString());

        reader.containsIgnoreCase("cd");
        assertTrue(reader.containsIgnoreCase("CD"));

        reader.matches('b');
        assertTrue(reader.matches('b'));

        reader.matches("bc");
        assertTrue(reader.matches("bc"));

        reader.matchesAny('b', 'c');
        assertTrue(reader.matchesAny('b', 'c'));

        reader.matchesLetter();
        assertTrue(reader.matchesLetter());

        reader.matchesDigit();
        assertTrue(reader.matchesDigit());

        reader.toString();
        assertEquals("cd", reader.toString());

        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }
}