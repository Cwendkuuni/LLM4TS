package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    @Test
    public void testCharacterReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertEquals(0, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertEquals(true, characterReader.isEmpty());

        characterReader.advance();
        assertEquals('a', characterReader.current());
        assertEquals(1, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());

        characterReader.consume();
        assertEquals('b', characterReader.current());
        assertEquals(1, characterReader.pos());

        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
        assertEquals(1, characterReader.pos());

        characterReader.unconsume();
        assertEquals('a', characterReader.current());
        assertEquals(1, characterReader.pos());

        assertEquals(2, characterReader.nextIndexOf('b'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
        assertEquals(-1, characterReader.nextIndexOf("xyz"));

        characterReader.consumeTo('b');
        assertEquals("a", characterReader.consumeToEnd());
        assertEquals("abcdef", characterReader.consumeToEnd());

        characterReader.consumeLetterSequence();
        assertEquals("def", characterReader.consumeToEnd());

        characterReader.consumeLetterThenDigitSequence();
        assertEquals("0", characterReader.consumeToEnd());

        characterReader.consumeHexSequence();
        assertEquals("fefefe", characterReader.consumeToEnd());

        characterReader.consumeDigitSequence();
        assertEquals("123456", characterReader.consumeToEnd());

        characterReader.matches('a');
        characterReader.matches("ab");
        characterReader.matchesIgnoreCase("AB");
        characterReader.matchesAny('a', 'b');
        characterReader.matchesAnySorted('b', 'a');
        characterReader.matchesLetter();
        characterReader.matchesDigit();

        characterReader.matchConsume("ab");
        characterReader.matchConsumeIgnoreCase("Ab");

        characterReader.containsIgnoreCase("ABC");
        characterReader.containsIgnoreCase("abcd");

        characterReader = new CharacterReader(new StringReader(""));
        assertEquals(0, characterReader.pos());
        assertEquals(EOF, characterReader.current());
        assertEquals(true, characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader(null));
        assertNull(characterReader.reader);
    }
}