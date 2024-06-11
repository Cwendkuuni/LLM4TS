package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals("", reader.consumeTo('a').trim());
        assertNull(reader.consumeTo('a'));
        assertEquals(0, reader.pos());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertFalse(reader.isEmpty());
        assertEquals(0, reader.nextIndexOf('a'));
        assertEquals("a", reader.consumeTo('a').trim());
        assertEquals("a", reader.consumeTo('a'));
        assertEquals(1, reader.pos());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ab");
        assertFalse(reader.isEmpty());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals("a", reader.consumeTo('a').trim());
        assertEquals("b", reader.consumeTo('b').trim());
        assertEquals("ab", reader.consumeToEnd().trim());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("LetMeConsumeThisSequenceOfLetters");
        assertEquals("LetMeConsumeThisSequenceOfLetters", reader.consumeToEnd().trim());
        assertEquals("Let", reader.consumeLetterSequence().trim());
        assertEquals("MeConsumeThisSequenceOfLetters", reader.consumeLetterThenDigitSequence().trim());
        assertEquals("Let", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x0F");
        assertEquals("&x0F", reader.consumeData().trim());
        assertEquals("F", reader.consumeHexSequence().trim());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("12345", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        assertEquals("ab", reader.consumeTo('b').trim());
        reader.rewindToMark();
        assertEquals("cd", reader.consumeToEnd().trim());
        assertEquals("cd", reader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderConsumeAny() {
        CharacterReader reader = new CharacterReader("abcd&e&f&g");
        assertEquals("&e", reader.consumeToAny(new char[]{'&', 'e'}).trim());
        assertEquals("&f", reader.consumeToAnySorted(new char[]{'&', 'f'}).trim());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("<tagName>");
        assertEquals("tagName", reader.consumeTagName().trim());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("ef"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(reader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(reader.matchesAnySorted(new char[]{'e', 'a'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }
}