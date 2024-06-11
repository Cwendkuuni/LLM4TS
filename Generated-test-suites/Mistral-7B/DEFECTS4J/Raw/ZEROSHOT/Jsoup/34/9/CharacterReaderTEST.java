package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "abcdef";
        CharacterReader characterReader = new CharacterReader(input);

        // Test basics
        assertEquals(0, characterReader.pos());
        assertTrue(characterReader.isEmpty());
        assertEquals(EOF, characterReader.current());
        assertEquals(EOF, characterReader.consume());

        // Test position
        characterReader.advance();
        assertEquals(1, characterReader.pos());
        assertFalse(characterReader.isEmpty());
        assertEquals('a', characterReader.current());
        assertEquals('a', characterReader.consume());

        // Test mark and rewind
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(1, characterReader.pos());

        // Test consumeAsString
        assertEquals("a", characterReader.consumeAsString());
        assertEquals(2, characterReader.pos());

        // Test nextIndexOf
        assertEquals(1, characterReader.nextIndexOf('b'));
        assertEquals(-1, characterReader.nextIndexOf('z'));

        // Test nextIndexOf with CharSequence
        CharSequence sequence = "def";
        assertEquals(5, characterReader.nextIndexOf(sequence));

        // Test consumeTo
        assertEquals("bcdef", characterReader.consumeTo('f'));
        assertEquals(6, characterReader.pos());

        // Test consumeTo with CharSequence
        assertEquals("abcdef", characterReader.consumeTo(sequence));

        // Test consumeToAny
        assertNotNull(characterReader.consumeToAny('a', 'b', 'c'));
        assertNotNull(characterReader.consumeToAny('a', 'b'));
        assertEquals("", characterReader.consumeToAny('z'));

        // Test consumeToEnd
        assertEquals("def", characterReader.consumeToEnd());
        assertEquals(EOF, characterReader.current());
        assertTrue(characterReader.isEmpty());

        // Test consumeLetterSequence
        assertEquals("abc", characterReader.consumeLetterSequence());
        assertEquals(5, characterReader.pos());

        // Test consumeLetterThenDigitSequence
        assertEquals("ab1cd", characterReader.consumeLetterThenDigitSequence());
        assertEquals(7, characterReader.pos());

        // Test consumeHexSequence
        assertEquals("abcdef", characterReader.consumeHexSequence());
        assertEquals(7, characterReader.pos());

        // Test consumeDigitSequence
        assertEquals("12345", characterReader.consumeDigitSequence());
        assertEquals(7, characterReader.pos());

        // Test matches
        assertTrue(characterReader.matches('e'));
        assertFalse(characterReader.matches('z'));

        // Test matchesIgnoreCase
        assertTrue(characterReader.matchesIgnoreCase("def"));
        assertFalse(characterReader.matchesIgnoreCase("ABC"));

        // Test matchesAny
        assertTrue(characterReader.matchesAny('a', 'b', 'c'));
        assertFalse(characterReader.matchesAny('z'));

        // Test matchesLetter
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter('0'));

        // Test matchesDigit
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit('5'));

        // Test matchConsume
        assertTrue(characterReader.matchConsume("def"));
        assertFalse(characterReader.matchConsume("abc"));

        // Test matchConsumeIgnoreCase
        assertTrue(characterReader.matchConsumeIgnoreCase("def"));
        assertFalse(characterReader.matchConsumeIgnoreCase("ABC"));

        // Test containsIgnoreCase
        assertTrue(characterReader.containsIgnoreCase("def"));
        assertFalse(characterReader.containsIgnoreCase("ABC"));
    }
}