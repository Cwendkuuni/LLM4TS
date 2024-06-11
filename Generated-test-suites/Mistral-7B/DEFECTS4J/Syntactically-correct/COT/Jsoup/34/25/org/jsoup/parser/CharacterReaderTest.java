package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.input);
        assertEquals(11, reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeAsString();
        assertEquals("Hello", consumed);
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(4, reader.nextIndexOf('l'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(4, reader.nextIndexOf("ll"));
        assertEquals(-1, reader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeTo('l');
        assertEquals("ll", consumed);
        assertEquals('o', reader.current());
    }

    @Test
    public void testConsumeToCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeTo("ll");
        assertEquals("Hello ll", consumed);
        assertEquals('o', reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcd45efgh");
        String consumed = reader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("ab", consumed);
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeToEnd();
        assertEquals("World", consumed);
        assertEquals('\0', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abcdefg", consumed);
        assertEquals('\0', reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def45g");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", consumed);
        assertEquals('d', reader.current());
    }

    @Test
    public void testMatchesChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches("he"));
        assertFalse(reader.matches("xy"));
    }

    @Test
    public void testMatchesCharIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("He"));
        assertTrue(reader.matchesIgnoreCase("hl"));
        assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny(new char[]{'h', 'e'}));
        assertFalse(reader.matchesAny(new char[]{'x', 'y'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello123World");
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsume("he"));
        assertEquals("ll", reader.consumed());
    }

    @Test
    public void testMatchConsumeCharSequenceIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsumeIgnoreCase("He"));
        assertEquals("ll", reader.consumed());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("header"));
    }
}