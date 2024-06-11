package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
    }

    // Add test cases for other methods here

    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("Testing the nextIndexOf method");
        assertEquals(3, reader.nextIndexOf('t'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Testing the consumeTo method");
        assertEquals("Testing the ", reader.consumeTo('c'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Testing the consumeToAny method");
        assertEquals(" ", reader.consumeToAny(' ', 'o'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Testing the consumeToEnd method");
        assertEquals("Testing the consumeToEnd method", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Testing123");
        assertEquals("Testing", reader.consumeLetterSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesIgnoreCase("t"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123");
        assertFalse(reader.matchesDigit());
    }

    // Add additional test cases as needed

}