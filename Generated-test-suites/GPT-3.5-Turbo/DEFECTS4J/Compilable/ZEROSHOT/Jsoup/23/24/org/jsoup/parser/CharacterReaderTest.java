package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("input");
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("input");
        assertEquals('i', reader2.current());
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
        assertEquals('i', reader.current());

        reader.unconsume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("input");

        reader.advance();
        assertEquals('n', reader.current());

        reader.advance();
        assertEquals('p', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("input");

        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("input");

        reader.consume();
        reader.mark();
        reader.advance();

        reader.rewindToMark();
        assertEquals(0, reader.pos());
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
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("", reader.consumeTo('i'));
        assertEquals("i", reader.consumeTo('n'));
        assertEquals("n", reader.consumeTo('p'));
        assertEquals("p", reader.consumeTo('u'));
        assertEquals("u", reader.consumeTo('t'));
        assertEquals("t", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("", reader.consumeTo("i"));
        assertEquals("i", reader.consumeTo("n"));
        assertEquals("n", reader.consumeTo("p"));
        assertEquals("p", reader.consumeTo("u"));
        assertEquals("u", reader.consumeTo("t"));
        assertEquals("t", reader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("i", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("n", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("p", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("u", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
        assertEquals("t", reader.consumeToAny('a', 'e', 'i', 'o', 'u'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("input", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("input", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("input", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
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

        assertTrue(reader.matchesIgnoreCase("i"));
        assertFalse(reader.matchesIgnoreCase("n"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("input");

        assertTrue(reader.matchesAny('a', 'e', 'i', 'o', 'u'));
        assertFalse(reader.matchesAny('b', 'c', 'd', 'f', 'g'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("input");
        assertTrue(reader1.matchesLetter());

        CharacterReader reader2 = new CharacterReader("123");
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("input");
        assertFalse(reader1.matchesDigit());

        CharacterReader reader2 = new CharacterReader("123");
        assertTrue(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("input");

        assertTrue(reader.matchConsume("in"));

        assertFalse(reader.matchConsume("pu"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");

        assertTrue(reader.matchConsumeIgnoreCase("iN"));

        assertFalse(reader.matchConsumeIgnoreCase("PU"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");

        assertTrue(reader.containsIgnoreCase("in"));

        assertFalse(reader.containsIgnoreCase("pu"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");

        assertEquals("input", reader.toString());
    }
}