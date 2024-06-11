package org.jsoup.parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals(0, cr.pos());
        cr.consume();
        assertEquals(1, cr.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader cr = new CharacterReader("");
        assertTrue(cr.isEmpty());
        cr.consume();
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader cr = new CharacterReader("t");
        assertEquals('t', cr.current());
        cr.consume();
        assertEquals(CharacterReader.EOF, cr.current());
    }

    @Test
    public void testConsume() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals('t', cr.consume());
        assertEquals('e', cr.consume());
        assertEquals('s', cr.consume());
        assertEquals('t', cr.consume());
        assertEquals(CharacterReader.EOF, cr.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals('t', cr.consume());
        assertEquals('e', cr.consume());
        cr.unconsume();
        assertEquals('e', cr.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader cr = new CharacterReader("test");
        cr.advance();
        assertEquals('e', cr.current());
    }

    @Test
    public void testMark() {
        CharacterReader cr = new CharacterReader("test");
        cr.mark();
        assertEquals(0, cr.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader cr = new CharacterReader("test");
        cr.advance();
        cr.mark();
        cr.consume();
        cr.rewindToMark();
        assertEquals('t', cr.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("t", cr.consumeAsString());
        assertEquals("e", cr.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals(1, cr.nextIndexOf('e'));
        assertEquals(-1, cr.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals(1, cr.nextIndexOf("es"));
        assertEquals(-1, cr.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("t", cr.consumeTo('e'));
        assertEquals("e", cr.consumeTo('s'));
    }

    @Test
    public void testConsumeToSeq() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("t", cr.consumeTo("es"));
        assertEquals("e", cr.consumeTo("st"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("t", cr.consumeToAny('e', 'x'));
        assertEquals("es", cr.consumeToAny('t', 'x'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("test", cr.consumeToEnd());
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("test", cr.consumeLetterSequence());
        cr.consume();
        cr.consume();
        assertEquals("", cr.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader cr = new CharacterReader("t1");
        assertEquals("t1", cr.consumeLetterThenDigitSequence());
        cr.consume();
        assertEquals("", cr.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader cr = new CharacterReader("t1");

        assertEquals("", cr.consumeHexSequence());
        cr.consume();
        assertEquals("1", cr.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader cr = new CharacterReader("t1");

        assertEquals("", cr.consumeDigitSequence());
        cr.consume();
        assertEquals("1", cr.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matches('t'));
        assertFalse(cr.matches('x'));
    }

    @Test
    public void testMatchesSeq() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matches("te"));
        assertFalse(cr.matches("ex"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchesIgnoreCase("Te"));
        assertFalse(cr.matchesIgnoreCase("Ex"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchesAny('e', 'x'));
        assertFalse(cr.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchesLetter());
        cr.consume();
        assertFalse(cr.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader cr = new CharacterReader("t1");
        assertFalse(cr.matchesDigit());
        cr.consume();
        assertTrue(cr.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchConsume("te"));
        assertEquals('s', cr.current());
        assertFalse(cr.matchConsume("ex"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader cr = new CharacterReader("test");
        assertTrue(cr.matchConsumeIgnoreCase("tE"));
        assertEquals('s', cr.current());
        assertFalse(cr.matchConsumeIgnoreCase("ex"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader cr = new CharacterReader("This is a test");
        assertTrue(cr.containsIgnoreCase("ThIs"));
        assertFalse(cr.containsIgnoreCase("Tex"));
    }

    @Test
    public void testToString() {
        CharacterReader cr = new CharacterReader("test");
        assertEquals("test", cr.toString());
    }
}