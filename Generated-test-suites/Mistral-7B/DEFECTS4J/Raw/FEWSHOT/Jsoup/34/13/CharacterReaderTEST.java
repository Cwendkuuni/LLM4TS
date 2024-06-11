package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.input);
        assertEquals(17, reader.length);
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
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals(-1, reader.length());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.advance();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("a", reader.consumeAsString());
        assertEquals("bc", reader.consumeAsString());
        assertEquals("cde", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(11, reader.nextIndexOf('k'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("b", reader.consumeTo('b').toString());
        assertEquals("defg", reader.consumeTo("def").toString());
        assertEquals("hijklmnopqrstuvwxyz", reader.consumeToEnd().toString());
        assertEquals("", reader.consumeToAny().toString());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("a", reader.consumeLetterSequence().toString());
        assertEquals("abc", reader.consumeLetterThenDigitSequence().toString());
        assertEquals("012345", reader.consumeDigitSequence().toString());
        assertEquals("", reader.consumeHexSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("xyz"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('a', 'z'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz12345");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testConsumeMatchingSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("ab", reader.matchConsume("ab").toString());
        assertEquals("def", reader.matchConsume("def").toString());
        assertEquals("", reader. consumptionStringAfterMatching("xyz"));
    }

    @Test
    public void testConsumeMatchingSequenceIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("ab", reader.matchConsumeIgnoreCase("AB").toString());
        assertEquals("def", reader.matchConsumeIgnoreCase("DeF").toString());
        assertEquals("", reader.consumptionStringAfterMatchingIgnoreCase("xyz"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("DeF"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.toString());
    }
}