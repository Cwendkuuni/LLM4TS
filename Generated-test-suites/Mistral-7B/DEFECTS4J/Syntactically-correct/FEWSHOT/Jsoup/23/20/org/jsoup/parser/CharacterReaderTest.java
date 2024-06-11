package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.input);
        assertEquals(5, reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals('b', reader.current());

        reader.consumeTo('c');
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        assertEquals('b', reader.current());

        reader.consumeTo('c');
        assertEquals('c', reader.current());

        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        assertEquals('c', reader.current());

        reader.advance();
        assertNull(reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();

        reader.consume();
        assertEquals('b', reader.current());

        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.consumeAsString());

        reader.consume();
        reader.consume();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcde");
        assertEquals("cd", reader.consumeTo('d'));

        reader.consume();
        assertEquals("e", reader.consumeTo('e'));

        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeTo_null() {
        CharacterReader reader = new CharacterReader("abc");
        assertNull(reader.consumeTo(null));

        reader.consume();
        assertEquals('b', reader.current());

        reader.consumeTo(null);
        assertNull(reader.current());
    }

    @Test
    public void testConsumeTo_emptyString() {
        CharacterReader reader = new CharacterReader("abc");
        assertNull(reader.consumeTo(""));

        reader.consume();
        assertEquals('b', reader.current());

        reader.consumeTo("");
        assertNull(reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("345", reader.consumeToAny('3', '4', '5'));

        reader.consume();
        reader.consume();
        assertEquals("", reader.consumeToAny('3', '4', '5'));
    }

    @Test
    public void testConsumeToAny_emptyArray() {
        CharacterReader reader = new CharacterReader("abc");
        assertNull(reader.consumeToAny(new char[0]));

        reader.consume();
        assertEquals('b', reader.current());

        reader.consumeToAny(new char[0]);
        assertNull(reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("c", reader.consumeToEnd());

        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeLetterSequence());

        reader.consume();
        assertEquals("ef", reader.consumeLetterSequence());

        reader.consumeToEnd();
        assertNull(reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("345", reader.consumeHexSequence());

        reader.consume();
        assertEquals("45", reader.consumeHexSequence());

        reader.consumeToEnd();
        assertNull(reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("012345");
        assertEquals("345", reader.consumeDigitSequence());

        reader.consume();
        assertEquals("45", reader.consumeDigitSequence());

        reader.consumeToEnd();
        assertNull(reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));

        reader.consume();
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("B"));
        assertTrue(reader.matchesIgnoreCase("C"));

        reader.consume();
        assertTrue(reader.matchesIgnoreCase("B"));
        assertTrue(reader.matchesIgnoreCase("C"));
        assertFalse(reader.matchesIgnoreCase("a"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny('b', 'a', 'c'));
        assertTrue(reader.matchesAny('c', 'a', 'b'));

        reader.consume();
        assertTrue(reader.matchesAny('b', 'a', 'c'));
        assertTrue(reader.matchesAny('c', 'a', 'b'));
        assertFalse(reader.matchesAny('a', 'a', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());

        reader.consume();
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("012345");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consume();
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("a"));
        assertEquals('b', reader.current());

        reader.matchConsume("b");
        assertEquals('c', reader.current());

        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsumeIgnoreCase("A"));
        assertEquals('B', reader.current());

        reader.matchConsumeIgnoreCase("B");
        assertEquals('C', reader.current());

        reader.consumeToEnd();
        assertNull(reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("AB"));
        assertTrue(reader.containsIgnoreCase("C"));

        reader.consume();
        assertTrue(reader.containsIgnoreCase("AB"));
        assertTrue(reader.containsIgnoreCase("C"));
        assertFalse(reader.containsIgnoreCase("a"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("bc", reader.toString());

        reader.consume();
        assertEquals("c", reader.toString());

        reader.consumeToEnd();
        assertNull(reader.toString());
    }
}