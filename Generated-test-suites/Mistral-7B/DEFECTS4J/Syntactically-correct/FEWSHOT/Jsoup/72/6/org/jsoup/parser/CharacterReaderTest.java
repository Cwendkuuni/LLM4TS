package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Reader() {
        char[] charBuf = new char[1024 * 32];
        Reader reader = new StringReader("abcdefghijklmnopqrstuvwxyz");
        CharacterReader characterReader = new CharacterReader(reader);

        assertEquals('a', characterReader.current());
        assertEquals('a', characterReader.consume());

        assertEquals('b', characterReader.current());
        assertEquals('b', characterReader.consume());

        // test isEmpty
        characterReader.unconsume();
        characterReader.unconsume();
        assertTrue(characterReader.isEmpty());

        // test pos
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());

        // test mark and rewindToMark
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(2, characterReader.pos());

        // test nextIndexOf
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        assertEquals(3, characterReader.nextIndexOf('c'));

        // test nextIndexOf with a sequence
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        assertEquals(5, characterReader.nextIndexOf("ef"));

        // test consumeTo
        String str = characterReader.consumeTo('g');
        assertEquals("def", str);

        // test consumeTo with a sequence
        str = characterReader.consumeTo("ijkl");
        assertEquals("mno", str);

        // test consumeToAny
        str = characterReader.consumeToAny(new char[]{'d', 'e'});
        assertEquals("def", str);

        // test consumeToAnySorted
        str = characterReader.consumeToAnySorted(new char[]{'d', 'e'});
        assertEquals("def", str);

        // test consumeData
        str = characterReader.consumeData();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeTagName
        str = characterReader.consumeTagName();
        assertEquals("abc", str);

        // test consumeToEnd
        str = characterReader.consumeToEnd();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeLetterSequence
        str = characterReader.consumeLetterSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeLetterThenDigitSequence
        str = characterReader.consumeLetterThenDigitSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz0123456789", str);

        // test consumeHexSequence
        str = characterReader.consumeHexSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeDigitSequence
        str = characterReader.consumeDigitSequence();
        assertEquals("0123456789", str);

        // test matches
        assertTrue(characterReader.matches('d'));
        assertFalse(characterReader.matches('x'));

        // test matches with a sequence
        assertTrue(characterReader.matches(Character.toString('i').toCharArray()));
        assertFalse(characterReader.matches("xyz".toCharArray()));

        // test matchesIgnoreCase
        assertTrue(characterReader.matchesIgnoreCase("ef".toCharArray()));
        assertTrue(characterReader.matchesIgnoreCase("GH".toCharArray()));
        assertFalse(characterReader.matchesIgnoreCase("xyz".toCharArray()));

        // test matchesAny
        assertTrue(characterReader.matchesAny(Character.toString('d').toCharArray()[0]));
        assertTrue(characterReader.matchesAny(Character.toString('e').toCharArray()));
        assertFalse(characterReader.matchesAny(Character.toString('x').toCharArray()[0]));

        // test matchesAnySorted
        assertFalse(characterReader.matchesAnySorted(Character.toString('x').toCharArray()));
        assertTrue(characterReader.matchesAnySorted(Character.toString('d').toCharArray()[0]));

        // test matchesLetter
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter(Character.MIN_VALUE));

        // test matchesDigit
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit(Character.forDigit(1, Character.getDefaultLocale())));

        // test matchConsume
        assertTrue(characterReader.matchConsume("ef"));

        // test matchConsumeIgnoreCase
        assertTrue(characterReader.matchConsumeIgnoreCase("EF"));

        // test containsIgnoreCase
        assertTrue(characterReader.containsIgnoreCase("ef"));
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_String() {
        char[] charBuf = new char[1024 * 32];
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));

        assertEquals('a', characterReader.current());
        assertEquals('a', characterReader.consume());

        // test isEmpty
        characterReader.unconsume();
        characterReader.unconsume();
        assertTrue(characterReader.isEmpty());

        // test pos
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());

        // test mark and rewindToMark
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(2, characterReader.pos());

        // test nextIndexOf
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        assertEquals(3, characterReader.nextIndexOf('c'));

        // test nextIndexOf with a sequence
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        assertEquals(5, characterReader.nextIndexOf("ef"));

        // test consumeTo
        String str = characterReader.consumeTo('g');
        assertEquals("def", str);

        // test consumeTo with a sequence
        str = characterReader.consumeTo("ijkl");
        assertEquals("mno", str);

        // test consumeToAny
        str = characterReader.consumeToAny(new char[]{'d', 'e'});
        assertEquals("def", str);

        // test consumeToAnySorted
        str = characterReader.consumeToAnySorted(new char[]{'d', 'e'});
        assertEquals("def", str);

        // test consumeData
        str = characterReader.consumeData();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeTagName
        str = characterReader.consumeTagName();
        assertEquals("abc", str);

        // test consumeToEnd
        str = characterReader.consumeToEnd();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeLetterSequence
        str = characterReader.consumeLetterSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeLetterThenDigitSequence
        str = characterReader.consumeLetterThenDigitSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz0123456789", str);

        // test consumeHexSequence
        str = characterReader.consumeHexSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeDigitSequence
        str = characterReader.consumeDigitSequence();
        assertEquals("0123456789", str);

        // test matches
        assertTrue(characterReader.matches('d'));
        assertFalse(characterReader.matches('x'));

        // test matches with a sequence
        assertTrue(characterReader.matches(Character.toString('i').toCharArray()));
        assertFalse(characterReader.matches("xyz".toCharArray()));

        // test matchesIgnoreCase
        assertTrue(characterReader.matchesIgnoreCase("ef".toCharArray()));
        assertTrue(characterReader.matchesIgnoreCase("GH".toCharArray()));
        assertFalse(characterReader.matchesIgnoreCase("xyz".toCharArray()));

        // test matchesAny
        assertTrue(characterReader.matchesAny(Character.toString('d').toCharArray()[0]));
        assertTrue(characterReader.matchesAny(Character.toString('e').toCharArray()));
        assertFalse(characterReader.matchesAny(Character.toString('x').toCharArray()[0]));

        // test matchesAnySorted
        assertFalse(characterReader.matchesAnySorted(Character.toString('x').toCharArray()));
        assertTrue(characterReader.matchesAnySorted(Character.toString('d').toCharArray()[0]));

        // test matchesLetter
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter(Character.MIN_VALUE));

        // test matchesDigit
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit(Character.forDigit(1, Character.getDefaultLocale())));

        // test matchConsume
        assertTrue(characterReader.matchConsume("ef"));

        // test matchConsumeIgnoreCase
        assertTrue(characterReader.matchConsumeIgnoreCase("EF"));

        // test containsIgnoreCase
        assertTrue(characterReader.containsIgnoreCase("ef"));
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_StringWithLength() {
        char[] charBuf = new char[1024 * 32];
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"), 17);

        assertEquals('a', characterReader.current());
        assertEquals('a', characterReader.consume());

        // test isEmpty
        characterReader.unconsume();
        characterReader.unconsume();
        assertTrue(characterReader.isEmpty());

        // test pos
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());

        // test mark and rewindToMark
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(2, characterReader.pos());

        // test nextIndexOf
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        assertEquals(3, characterReader.nextIndexOf('c'));

        // test nextIndexOf with a sequence
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        assertEquals(5, characterReader.nextIndexOf("ef"));

        // test consumeTo
        String str = characterReader.consumeTo('g');
        assertEquals("def", str);

        // test consumeTo with a sequence
        str = characterReader.consumeTo("ijkl");
        assertEquals("mno", str);

        // test consumeToAny
        str = characterReader.consumeToAny(new char[]{'d', 'e'});
        assertEquals("def", str);

        // test consumeToAnySorted
        str = characterReader.consumeToAnySorted(new char[]{'d', 'e'});
        assertEquals("def", str);

        // test consumeData
        str = characterReader.consumeData();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeTagName
        str = characterReader.consumeTagName();
        assertEquals("abc", str);

        // test consumeToEnd
        str = characterReader.consumeToEnd();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeLetterSequence
        str = characterReader.consumeLetterSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeLetterThenDigitSequence
        str = characterReader.consumeLetterThenDigitSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz0123456789", str);

        // test consumeHexSequence
        str = characterReader.consumeHexSequence();
        assertEquals("abcdefghijklmnopqrstuvwxyz", str);

        // test consumeDigitSequence
        str = characterReader.consumeDigitSequence();
        assertEquals("0123456789", str);

        // test matches
        assertTrue(characterReader.matches('d'));
        assertFalse(characterReader.matches('x'));

        // test matches with a sequence
        assertTrue(characterReader.matches(Character.toString('i').toCharArray()));
        assertFalse(characterReader.matches("xyz".toCharArray()));

        // test matchesIgnoreCase
        assertTrue(characterReader.matchesIgnoreCase("ef".toCharArray()));
        assertTrue(characterReader.matchesIgnoreCase("GH".toCharArray()));
        assertFalse(characterReader.matchesIgnoreCase("xyz".toCharArray()));

        // test matchesAny
        assertTrue(characterReader.matchesAny(Character.toString('d').toCharArray()[0]));
        assertTrue(characterReader.matchesAny(Character.toString('e').toCharArray()));
        assertFalse(characterReader.matchesAny(Character.toString('x').toCharArray()[0]));

        // test matchesAnySorted
        assertFalse(characterReader.matchesAnySorted(Character.toString('x').toCharArray()));
        assertTrue(characterReader.matchesAnySorted(Character.toString('d').toCharArray()[0]));

        // test matchesLetter
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter(Character.MIN_VALUE));

        // test matchesDigit
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit(Character.forDigit(1, Character.getDefaultLocale())));

        // test matchConsume
        assertTrue(characterReader.matchConsume("ef"));

        // test matchConsumeIgnoreCase
        assertTrue(characterReader.matchConsumeIgnoreCase("EF"));

        // test containsIgnoreCase
        assertTrue(characterReader.containsIgnoreCase("ef"));
        assertFalse(characterReader.containsIgnoreCase("xyz"));
    }
}