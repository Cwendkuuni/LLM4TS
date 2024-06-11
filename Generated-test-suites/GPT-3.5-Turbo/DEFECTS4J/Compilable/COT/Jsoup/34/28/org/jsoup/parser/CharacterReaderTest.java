package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals(CharacterReader.EOF, reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals(CharacterReader.EOF, reader.consume());
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.advance();
        reader.unconsume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
        reader.consume();
        reader.advance();
        reader.mark();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.consume();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("", reader.consumeAsString());
        reader.advance();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, reader.nextIndexOf('a'));
        reader.consume();
        assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOfWithSeq() {
        assertEquals(-1, reader.nextIndexOf("abc"));
        reader.consume();
        assertEquals(-1, reader.nextIndexOf("def"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", reader.consumeTo('a'));
        reader.advance();
        assertEquals("", reader.consumeTo('a'));
    }

    @Test
    public void testConsumeToWithSeq() {
        assertEquals("", reader.consumeTo("abc"));
        reader.advance();
        assertEquals("", reader.consumeTo("def"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", reader.consumeToAny('a', 'b'));
        reader.advance();
        assertEquals("", reader.consumeToAny('c', 'd'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("", reader.consumeToEnd());
        reader.advance();
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("", reader.consumeLetterThenDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertFalse(reader.matches('a'));
        reader.consume();
        assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesWithSeq() {
        assertFalse(reader.matches("abc"));
        reader.consume();
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("abc"));
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("def"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(reader.matchesAny('a', 'b'));
        reader.consume();
        assertFalse(reader.matchesAny('c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(reader.matchConsume("abc"));
        reader.consume();
        assertFalse(reader.matchConsume("def"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("abc"));
        reader.consume();
        assertFalse(reader.matchConsumeIgnoreCase("def"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("abc"));
        reader.consume();
        assertFalse(reader.containsIgnoreCase("def"));
    }

    @Test
    public void testToString() {
        assertEquals("", reader.toString());
        reader.consume();
        assertEquals("", reader.toString());
    }
}