package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        String input = "testing";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.consume());
        reader.unconsume();
        assertEquals('t', reader.consume());
    }

    @Test
    public void testAdvance() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('t', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "test,example";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeTo(','));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "test123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesAny() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('t', 'e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "0987";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("st"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "TEST";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("te"));
        assertFalse(reader.matchConsumeIgnoreCase("st"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "test example";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("EXAMPLE"));
        assertFalse(reader.containsIgnoreCase("other"));
    }
}