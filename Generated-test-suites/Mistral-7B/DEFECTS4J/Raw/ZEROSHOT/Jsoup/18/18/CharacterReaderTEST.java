package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        reader.consume();
        assertFalse(reader.matches('A'));
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        reader.consume();
        reader.consume();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('B', reader.current());
        assertEquals("AB", reader.consumeAsString());
        assertEquals("BC", reader.consumeTo('C').toString());
        assertEquals("ABC", reader.consumeToEnd().toString());
        assertEquals("AB", reader.consumeLetterSequence().toString());
        reader.unconsume();
        assertEquals("AC", reader.consumeLetterSequence().toString());
        assertTrue(reader.matches('B'));
        assertTrue(reader.matches("AB"));
        assertFalse(reader.matches("CD"));
        assertFalse(reader.matchesIgnoreCase("cd"));
        assertTrue(reader.matchesAny(new char[]{'B', 'C'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesLetterOrDigit());
        assertFalse(reader.matchesAny(new char[]{'A', 'Z'}));
        assertFalse(reader.matchesAny(new char[]{'a', 'z'}));
        assertFalse(reader.matchesAny(new char[]{'0', '9'}));
        assertFalse(reader.matchesAny(new char[]{'A', 'z'}));
        assertEquals("", reader.toString());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterReaderNegativeIndex() {
        CharacterReader reader = new CharacterReader("ABC");
        reader.consumeTo(-1);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABC");
        String consumed = reader.consumeToEnd();
        assertEquals("ABC", reader.input().substring(0, reader.pos()));
        assertEquals("ABC", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEndAfterEOF() {
        CharacterReader reader = new CharacterReader("");
        String consumed = reader.consumeToEnd();
        assertEquals("", reader.toString());
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String consumed = reader.consumeToAny(new char[]{'A', 'B'});
        assertEquals("AB", consumed);
        consumed = reader.consumeToAny(new char[]{'E', 'F'});
        assertEquals("EF", consumed);
        consumed = reader.consumeToAny(new char[]{'G', 'H'});
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnyAfterEOF() {
        CharacterReader reader = new CharacterReader("");
        String consumed = reader.consumeToAny(new char[]{'A', 'B'});
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToSeq() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeTo("ab");
        assertEquals("ab", consumed);
        consumed = reader.consumeTo("cd");
        assertEquals("cd", consumed);
        consumed = reader.consumeTo("xy");
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToSeqAfterEOF() {
        CharacterReader reader = new CharacterReader("abcd");
        String consumed = reader.consumeTo("xy");
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToSeqIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdABcd");
        String consumed = reader.consumeToIgnoreCase("Ab");
        assertEquals("Ab", consumed);
        consumed = reader.consumeToIgnoreCase("Cd");
        assertEquals("Cd", consumed);
        consumed = reader.consumeToIgnoreCase("Xy");
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToSeqIgnoreCaseAfterEOF() {
        CharacterReader reader = new CharacterReader("AbCd");
        String consumed = reader.consumeToIgnoreCase("Xy");
        assertEquals("", consumed);
    }
}