package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        CharacterReader cr = new CharacterReader("abc");
        assertEquals("abc", cr.input);
        assertEquals(3, cr.length);
    }

    @Test
    public void testPos_initialization() {
        CharacterReader cr = new CharacterReader("abc");
        assertEquals(0, cr.pos);
    }

    @Test
    public void testIsEmpty_initialization() {
        CharacterReader cr = new CharacterReader("abc");
        assertFalse(cr.isEmpty());
    }

    @Test
    public void testCurrent_initialization() {
        CharacterReader cr = new CharacterReader("abc");
        assertEquals('a', cr.current());
    }

    @Test
    public void testConsume_firstCharacter() {
        CharacterReader cr = new CharacterReader("abc");
        assertEquals('a', cr.consume());
        assertEquals(1, cr.pos);
    }

    @Test
    public void testConsume_lastCharacter() {
        CharacterReader cr = new CharacterReader("abc");
        cr.advance();
        assertEquals('b', cr.consume());
        assertEquals(2, cr.pos);
    }

    @Test
    public void testConsume_endOfString() {
        CharacterReader cr = new CharacterReader("abc");
        cr.advance();
        cr.advance();
        assertEquals(CharacterReader.EOF, cr.consume());
        assertEquals(3, cr.pos);
    }

    @Test
    public void testConsumeAsString_oneCharacter() {
        CharacterReader cr = new CharacterReader("abc");
        assertEquals("a", cr.consumeAsString());
        assertEquals(1, cr.pos);
    }

    @Test
    public void testConsumeAsString_multipleCharacters() {
        CharacterReader cr = new CharacterReader("abc");
        cr.advance();
        assertEquals("bc", cr.consumeAsString());
        assertEquals(2, cr.pos);
    }

    @Test
    public void testConsumeTo_singleCharacter() {
        CharacterReader cr = new CharacterReader("abcd");
        assertEquals("b", cr.consumeTo('c'));
        assertEquals(2, cr.pos);
    }

    @Test
    public void testConsumeTo_multipleCharacters() {
        CharacterReader cr = new CharacterReader("abcd");
        assertEquals("bc", cr.consumeTo('d'));
        assertEquals(3, cr.pos);
    }

    @Test
    public void testConsumeTo_endOfString() {
        CharacterReader cr = new CharacterReader("abcd");
        assertEquals("d", cr.consumeTo('d'));
        assertEquals("", cr.consumeAsString());
        assertEquals(4, cr.pos);
    }

    @Test
    public void testConsumeToAny_singleCharacter() {
        CharacterReader cr = new CharacterReader("abcdab");
        assertEquals("a", cr.consumeToAny('a'));
        assertEquals(1, cr.pos);
    }

    @Test
    public void testConsumeToAny_multipleCharacters() {
        CharacterReader cr = new CharacterReader("abcdab");
        assertEquals("cd", cr.consumeToAny('d'));
        assertEquals(3, cr.pos);
    }

    @Test
    public void testConsumeToAny_noneFound() {
        CharacterReader cr = new CharacterReader("abcdab");
        assertEquals("", cr.consumeToAny('e'));
        assertEquals(1, cr.pos);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader cr = new CharacterReader("abcd");
        assertEquals("d", cr.consumeToEnd());
        assertEquals("", cr.consumeAsString());
        assertEquals(4, cr.pos);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader cr = new CharacterReader("abcd1efghij");
        assertEquals("abcd", cr.consumeLetterSequence());
        assertEquals(5, cr.pos);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader cr = new CharacterReader("abcd\u0031efghij");
        assertEquals("\u0031", cr.consumeHexSequence());
        assertEquals(6, cr.pos);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader cr = new CharacterReader("abcd123efghij");
        assertEquals("123", cr.consumeDigitSequence());
        assertEquals(6, cr.pos);
    }

    @Test
    public void testMatches_singleCharacter() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matches('a'));
        assertEquals(0, cr.pos);
    }

    @Test
    public void testMatches_multipleCharacters() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matches("ab"));
        assertEquals(2, cr.pos);
    }

    @Test
    public void testMatchesIgnoreCase_singleCharacter() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matchesIgnoreCase("A"));
        assertEquals(0, cr.pos);
    }

    @Test
    public void testMatchesIgnoreCase_multipleCharacters() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matchesIgnoreCase("ab"));
        assertEquals(2, cr.pos);
    }

    @Test
    public void testMatchesAny_singleCharacter() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matchesAny('a'));
        assertEquals(0, cr.pos);
    }

    @Test
    public void testMatchesAny_multipleCharacters() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matchesAny(new char[]{'a', 'b'}));
        assertEquals(0, cr.pos);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader cr = new CharacterReader("abcd");
        assertTrue(cr.matchesLetter());
        assertEquals(0, cr.pos);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader cr = new CharacterReader("abcd1");
        assertTrue(cr.matchesDigit());
        assertEquals(0, cr.pos);
    }

    @Test
    public void testMark_and_rewindToMark() {
        CharacterReader cr = new CharacterReader("abcd");
        cr.mark();
        cr.advance();
        cr.advance();
        assertEquals('c', cr.current());
        cr.rewindToMark();
        assertEquals('a', cr.current());
        assertEquals(1, cr.pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader cr = new CharacterReader("abcd<title>Test</title>");
        assertTrue(cr.containsIgnoreCase("title"));
        assertEquals(6, cr.pos);
    }

    @Test
    public void testContainsIgnoreCase_notFound() {
        CharacterReader cr = new CharacterReader("abcd<title>Test</title>");
        assertFalse(cr.containsIgnoreCase("body"));
        assertEquals(6, cr.pos);
    }

    @Test
    public void testConsumeToEnd_afterMarkAndRewind() {
        CharacterReader cr = new CharacterReader("abcd<title>Test</title>");
        cr.mark();
        cr.advance();
        cr.advance();
        cr.rewindToMark();
        assertEquals("abcd<title>", cr.consumeToEnd());
        assertEquals(11, cr.pos);
    }
}