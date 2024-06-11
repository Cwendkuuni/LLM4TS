package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReader_SingleCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        String expected = reader.consumeAsString();
        assertEquals("ab", expected);
        assertEquals('c', reader.current());
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo('d');
        assertEquals("abcd", consumed);
        assertEquals('e', reader.current());
        assertEquals(6, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeTo_End() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeTo(CharacterReader.EOF);
        assertEquals("abcdef", consumed);
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(11, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeTo_Sequence() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        String consumed = reader.consumeTo("ef");
        assertEquals("abcd", consumed);
        assertEquals('g', reader.current());
        assertEquals(11, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabc");
        String consumed = reader.consumeToAny('a', 'b');
        assertEquals("ab", consumed);
        assertEquals('c', reader.current());
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefghijk");
        String consumed = reader.consumeToEnd();
        assertEquals("abcdefghijk", consumed);
        assertEquals(CharacterReader.EOF, reader.current());
        assertEquals(11, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcd", consumed);
        assertEquals('1', reader.current());
        assertEquals(5, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd%23efgAbC");
        String consumed = reader.consumeHexSequence();
        assertEquals("%23", consumed);
        assertEquals('e', reader.current());
        assertEquals(8, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals('e', reader.current());
        assertEquals(8, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('%'));
        assertFalse(reader.matches('.'));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("%"));
        assertFalse(reader.matchesIgnoreCase("."));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('%'));
        assertFalse(reader.matchesAny('.'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReader_MatchConsume() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matchConsume("ef"));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        assertTrue(reader.matchConsumeIgnoreCase("EF"));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<title>efgAbC</title>");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("TITLE"));
        assertFalse(reader.containsIgnoreCase("TF"));
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd123efgAbC");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
    }
}