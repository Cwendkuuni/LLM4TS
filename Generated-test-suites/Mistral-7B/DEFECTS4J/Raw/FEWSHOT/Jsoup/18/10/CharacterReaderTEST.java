package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReader_SingleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacterInput() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeString() {
        CharacterReader reader = new CharacterReader("abc");
        String str = reader.consumeAsString();
        assertEquals("abc", str);
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("abcd");
        String str = reader.consumeTo('c');
        assertEquals("ab", str);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToString() {
        CharacterReader reader = new CharacterReader("abcd");
        String str = reader.consumeTo("bc");
        assertEquals("ab", str);
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        CharacterReader reader = new CharacterReader("0123456789abcd");
        String str = reader.consumeToAny(new char[]{'a', 'c'});
        assertEquals("", str);
        assertEquals('d', reader.current());

        str = reader.consumeToAny(new char[]{'0', '1'});
        assertEquals("01", str);
        assertEquals('2', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        String str = reader.consumeToEnd();
        assertEquals("cd", str);
        assertEquals('\0', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String str = reader.consumeLetterSequence();
        assertEquals("", str);
        assertTrue(reader.isEmpty());

        str = reader.consumeLetterSequence();
        assertEquals("a", str);
        assertEquals('b', reader.current());

        reader.unconsume();
        str = reader.consumeLetterSequence();
        assertEquals("ab", str);
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        String str = reader.consumeHexSequence();
        assertEquals("", str);
        assertTrue(reader.isEmpty());

        str = reader.consumeHexSequence();
        assertEquals("01", str);
        assertEquals('2', reader.current());

        reader.unconsume();
        str = reader.consumeHexSequence();
        assertEquals("01", str);
        assertEquals('2', reader.current());

        str = reader.consumeHexSequence();
        assertEquals("23", str);
        assertEquals('4', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        String str = reader.consumeDigitSequence();
        assertEquals("", str);
        assertTrue(reader.isEmpty());

        str = reader.consumeDigitSequence();
        assertEquals("0", str);
        assertEquals('1', reader.current());

        reader.unconsume();
        str = reader.consumeDigitSequence();
        assertEquals("01", str);
        assertEquals('2', reader.current());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("bc"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCdEf");
        assertTrue(reader.matchesIgnoreCase("ab")); // false as per Java String case-sensitive comparison
        assertTrue(reader.matchesIgnoreCase("Ab")); // true as per Java String case-insensitive comparison
        assertTrue(reader.matchesIgnoreCase("Cd")); // true as per Java String case-insensitive comparison
        assertTrue(reader.matchesIgnoreCase("ef")); // true as per Java String case-insensitive comparison
        assertFalse(reader.matchesIgnoreCase("abcd")); // false as per Java String comparison
        assertFalse(reader.matchesIgnoreCase("ABCDEF")); // false as per Java String comparison
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        assertTrue(reader.matchesAny('0'));
        assertTrue(reader.matchesAny('1'));
        assertTrue(reader.matchesAny('2'));
        assertTrue(reader.matchesAny('3'));
        assertTrue(reader.matchesAny('4'));
        assertTrue(reader.matchesAny('5'));
        assertTrue(reader.matchesAny('6'));
        assertTrue(reader.matchesAny('7'));
        assertTrue(reader.matchesAny('8'));
        assertTrue(reader.matchesAny('9'));
        assertTrue(reader.matchesAny('A'));
        assertTrue(reader.matchesAny('B'));
        assertTrue(reader.matchesAny('C'));
        assertTrue(reader.matchesAny('D'));
        assertTrue(reader.matchesAny('E'));
        assertTrue(reader.matchesAny('F'));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
        assertTrue(reader.matchesAny('d'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny('f'));
        assertFalse(reader.matchesAny('g')); // false as per the given sequence
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.advance();

        assertEquals('c', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ<title></title>");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertFalse(reader.containsIgnoreCase("<TITLE>")); // false as per Java String comparison
    }
}