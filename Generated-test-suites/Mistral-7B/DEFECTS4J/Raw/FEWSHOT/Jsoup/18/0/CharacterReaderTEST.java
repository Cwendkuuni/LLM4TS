package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdefghijk", reader.input);
        assertEquals("abcdefghijk", reader.input.substring(0, reader.length));
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
    }

    @Test
    public void testPos() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abcdefghijk");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consumeToEnd();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testUnconsume() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        reader.advance();
        assertEquals(4, reader.pos());
    }

    @Test
    public void testMark() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeAsString());
        reader.consume();
        reader.consume();
        assertEquals("ghi", reader.consumeAsString());
        reader.consumeToEnd();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("cdef", reader.consumeTo('d'));
        reader.consume();
        reader.consume();
        assertEquals("ghi", reader.consumeTo('k'));
        reader.consumeToEnd();
        assertEquals("", reader.consumeTo('k'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("ijk", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("def", reader.consumeLetterSequence());
        reader.consumeLetterSequence();
        reader.consumeLetterSequence();
        assertEquals("hij", reader.consumeLetterSequence());
        reader.consumeToEnd();
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "abc123def456";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("12", reader.consumeHexSequence());
        reader.consume();
        reader.consume();
        assertEquals("45", reader.consumeHexSequence());
        reader.consumeToEnd();
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "abc123def456";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());

        reader = new CharacterReader("1234567");
        assertEquals("567", reader.consumeDigitSequence());

        reader = new CharacterReader("123456");
        assertEquals("456", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matches('z'));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        reader.advance();
        assertTrue(reader.matches('c'));
        reader.consume();
        reader.consume();
        assertTrue(reader.matches('h'));
        reader.consumeToEnd();
        assertFalse(reader.matches('k'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "ABCDEFGHIJ";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("B"));
        reader.advance();
        assertTrue(reader.matchesIgnoreCase("C"));
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("H"));
        reader.consumeToEnd();
        assertFalse(reader.matchesIgnoreCase("K"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesAny('z'));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        reader.advance();
        assertTrue(reader.matchesAny('c'));
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesAny('h'));
        reader.consumeToEnd();
        assertTrue(reader.matchesAny('j'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "1234567890";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "abcdefghijk";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("ab"));
        assertEquals(2, reader.pos());
        reader.matchConsume("cd");
        reader.matchConsume("ef");
        reader.matchConsume("ik");
        reader.consumeToEnd();
        assertFalse(reader.matchConsume("l"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "ABCDEFGHIJ";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertEquals(2, reader.pos());
        reader.matchConsumeIgnoreCase("Cd");
        reader.matchConsumeIgnoreCase("Ef");
        reader.matchConsumeIgnoreCase("Ik");
        reader.consumeToEnd();
        assertFalse(reader.matchConsumeIgnoreCase("L"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "abcdefghijk<title>My Title</title><style>CSS here</style>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/style"));
        assertFalse(reader.containsIgnoreCase("/script"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }
}