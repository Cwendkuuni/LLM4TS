package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_construction() {
        String input = "test";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
        assertEquals(input.length(), reader.length);
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_nullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertNull(reader.current());
        reader.consume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
        reader.consume();
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('\0', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(5, reader.pos);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(3, reader.pos);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("st", reader.consumeAsString());
        assertEquals("test", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("es", reader.consumeTo('s'));
        assertEquals("test", reader.input.substring(reader.pos));
        reader.consumeTo('s');
        assertEquals("st", reader.input.substring(reader.pos));

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals("", reader.consumeTo('z'));
        assertEquals("", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("es", reader.consumeToAny('s', 't'));
        assertEquals("test", reader.input.substring(reader.pos));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals("", reader.consumeToAny('z'));
        assertEquals("", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("est", reader.consumeToEnd());
        assertEquals("", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("est", reader.consumeLetterSequence());
        assertEquals("123", reader.input.substring(reader.pos));

        reader = new CharacterReader("Test123");
        assertEquals("Test", reader.consumeLetterSequence());
        assertEquals("123", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test#ff0000");
        assertEquals("ff0000", reader.consumeHexSequence());
        assertEquals("test", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("test", reader.input.substring(reader.pos));
    }

    @Test
    public void testMatches()
    {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matches('!'));
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("t"));
        assertTrue(reader.matchesIgnoreCase("T"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesAny('z'));
        assertTrue(reader.matchesAny('t'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());

        reader = new CharacterReader("test123");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());

        reader = new CharacterReader("testABC");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test123");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());

        reader = new CharacterReader("testABC123");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("st"));
        assertEquals("test", reader.input.substring(reader.pos));

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertFalse(reader.matchConsume("st"));
        assertEquals("", reader.input.substring(reader.pos));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("st"));
        assertEquals("test", reader.input.substring(reader.pos));

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertFalse(reader.matchConsumeIgnoreCase("ST"));
        assertEquals("", reader.input.substring(reader.pos));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test<html>Test</html>");
        assertTrue(reader.containsIgnoreCase("<html>"));
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();

        assertTrue(reader.containsIgnoreCase("test"));
        assertTrue(reader.containsIgnoreCase("Test"));
        assertFalse(reader.containsIgnoreCase("html"));
        assertFalse(reader.containsIgnoreCase("HTML"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}