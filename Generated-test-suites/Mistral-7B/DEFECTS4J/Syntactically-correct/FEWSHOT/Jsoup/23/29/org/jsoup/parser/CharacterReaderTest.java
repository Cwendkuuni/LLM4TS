package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.input);
        assertEquals(3, reader.length);
    }

    @Test
    public void testCharacterReader_IsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader.advance();
        assertFalse(reader.isEmpty());

        reader.consume();
        reader.unconsume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_Current() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_Consume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consume();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReader_Unconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_Advance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_Mark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();

        reader.consume();
        assertEquals('b', reader.current());

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeAsString());
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("cd", reader.consumeTo('d'));
    }

    @Test
    public void testCharacterReader_ConsumeTo_EmptySequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("", reader.consumeToAny((char) 0));
    }

    @Test
    public void testCharacterReader_ConsumeTo_SingleCharacter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("c", reader.consumeToAny('c'));
    }

    @Test
    public void testCharacterReader_ConsumeTo_MultipleCharacters() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeToAny('c', 'b'));
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("d", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1a2b3c");
        assertEquals("1a", reader.consumeHexSequence());
        assertEquals("2b", reader.consumeHexSequence());
        assertEquals("3c", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("b"));
        assertTrue(reader.matchesIgnoreCase("c"));
        assertTrue(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('b', 'c'));
        assertTrue(reader.matchesAny('c', 'd'));
        assertFalse(reader.matchesAny('e', 'f'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_MatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchConsume("ab"));
        assertEquals("cd", reader.input.substring(reader.pos));
    }

    @Test
    public void testCharacterReader_MatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals("CD", reader.input.substring(reader.pos));
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<efg>hijklmno");
        assertTrue(reader.containsIgnoreCase("<efg>"));
        assertFalse(reader.containsIgnoreCase("<EFG>"));
        assertFalse(reader.containsIgnoreCase("<efg>h"));
    }

    @Test
    public void testCharacterReader_ToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.toString());
    }
}