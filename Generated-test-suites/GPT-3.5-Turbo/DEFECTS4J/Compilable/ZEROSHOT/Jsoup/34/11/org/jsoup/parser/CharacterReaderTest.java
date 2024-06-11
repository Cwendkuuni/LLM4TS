package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        String input = "abcdef";
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
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("a", reader.consumeAsString());
    }


    @Test
    public void testNextIndexOf() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(3, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOf_WithInvalidChar() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOf_Sequence() {
        String input = "abcdefabcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(6, reader.nextIndexOf("defg"));
    }

    @Test
    public void testNextIndexOf_WithInvalidSequence() {
        String input = "abcdefabcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("a", reader.consumeTo('b'));
    }

    @Test
    public void testConsumeTo_WithInvalidChar() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeTo_Sequence() {
        String input = "abcdefabcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.consumeTo("defg"));
    }

    @Test
    public void testConsumeTo_WithInvalidSequence() {
        String input = "abcdefabcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdefabcdefg", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.consumeToAny('d','e','f'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "123abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123abc";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('a'));
    }

    @Test
    public void testMatches_WithInvalidChar() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatches_Sequence() {
        String input = "abcdefabcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches("abcdef"));
    }

    @Test
    public void testMatches_WithInvalidSequence() {
        String input = "abcdefabcdefg";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "aBcDeF";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("abcdef"));
    }

    @Test
    public void testMatchesIgnoreCase_WithInvalidSequence() {
        String input = "aBcDeF";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('a','x','b'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_WithInvalidChar() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_WithInvalidChar() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("abc"));
        assertEquals('d', reader.current());
    }

    @Test
    public void testMatchConsume_WithInvalidSequence() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchConsume("xyz"));
        assertEquals('a', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "aBcDeF";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("abcdef"));
        assertEquals('F', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase_WithInvalidSequence() {
        String input = "aBcDeF";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
        assertEquals('a', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("bcd"));
    }

    @Test
    public void testContainsIgnoreCase_WithInvalidSequence() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.toString());
    }
}