package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('p', reader.consume());
        assertEquals('u', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        reader.unconsume();
        assertEquals('i', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        assertEquals('n', reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeAsString());
        assertEquals("n", reader.consumeAsString());
        assertEquals("p", reader.consumeAsString());
        assertEquals("u", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(1, reader.nextIndexOf('n'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(1, reader.nextIndexOf("n"));
        assertEquals(-1, reader.nextIndexOf("z"));
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeTo('n'));
        assertEquals("npu", reader.consumeTo('t'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeTo("n"));
        assertEquals("npu", reader.consumeTo("t"));
        assertEquals("", reader.consumeTo("z"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeToAny('n'));
        assertEquals("npu", reader.consumeToAny('t', 'z'));
        assertEquals("", reader.consumeToAny('x', 'y'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("input", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("input", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("input", reader.consumeHexSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("input123");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("input", reader.consumeToEnd());
    }

    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches('i'));
        assertFalse(reader.matches('n'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches("i"));
        assertFalse(reader.matches("n"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesIgnoreCase("I"));
        assertFalse(reader.matchesIgnoreCase("N"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesAny('i', 'n'));
        assertFalse(reader.matchesAny('z', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsume("i"));
        assertFalse(reader.matchConsume("n"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsumeIgnoreCase("I"));
        assertFalse(reader.matchConsumeIgnoreCase("N"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.containsIgnoreCase("IN"));
        assertFalse(reader.containsIgnoreCase("NO"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.toString());
    }
}