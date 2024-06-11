package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("ABCDE");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());

        reader = new CharacterReader("ABCDE");
        assertEquals('A', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals('A', reader.consume());
        assertEquals('B', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("ABCDE");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('A', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("ABCDE");
        reader.advance();
        assertEquals('B', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("ABCDE");
        reader.advance();
        reader.mark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("ABCDE");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('B', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("A", reader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("AB", reader.consumeTo('C'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("A", reader.consumeTo("BC"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("A", reader.consumeToAny('B'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("ABCDE", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("ABCDE", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("ABCDE", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matches('A'));
        assertFalse(reader.matches('B'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matches("AB"));
        assertFalse(reader.matches("BC"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matchesIgnoreCase("AB"));
        assertTrue(reader.matchesIgnoreCase("bc"));
        assertFalse(reader.matchesIgnoreCase("CD"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matchesAny('A', 'B'));
        assertFalse(reader.matchesAny('C', 'D'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matchConsume("AB"));
        assertFalse(reader.matchConsume("CD"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
        assertTrue(reader.matchConsumeIgnoreCase("cd"));
        assertFalse(reader.matchConsumeIgnoreCase("DE"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDE");
        assertFalse(reader.containsIgnoreCase("</title>"));
    }
}